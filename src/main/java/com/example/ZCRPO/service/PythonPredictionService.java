package com.example.ZCRPO.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.ZCRPO.model.ProductRequest;
import com.example.ZCRPO.model.PredictionResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class PythonPredictionService {

    private static final String PYTHON_SCRIPT = "python"; // Или путь к интерпретатору, например, "C:\\Python\\python.exe" для Windows
    private static final String SCRIPT_NAME = "model_runner.py"; // Название скрипта

    public PredictionResponse predict(ProductRequest request) {
        try {
            // Преобразуем объект ProductRequest в JSON
            ObjectMapper mapper = new ObjectMapper();
            String inputJson = mapper.writeValueAsString(request);

            // Устанавливаем рабочую директорию
            File scriptDirectory = new File("src/main/resources/scripts");

            // Передаем путь к скрипту как аргумент в команду
            String scriptPath = Paths.get(scriptDirectory.getAbsolutePath(), SCRIPT_NAME).toString();

            // Создаем ProcessBuilder с указанием интерпретатора Python и скрипта
            ProcessBuilder pb = new ProcessBuilder(PYTHON_SCRIPT, scriptPath);

            // Устанавливаем рабочую директорию для выполнения процесса
            pb.directory(scriptDirectory);

            // Настройка переменных окружения для CUDA и cuDNN
            Map<String, String> environment = pb.environment();
            environment.put("CUDA_HOME", "C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v11.0");
            environment.put("PATH", environment.get("PATH") + ";C:\\Program Files\\NVIDIA GPU Computing Toolkit\\CUDA\\v11.0\\bin");

            // Запускаем процесс Python
            Process process = pb.start();

            // Отправляем JSON в stdin Python-скрипта
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(inputJson);
                writer.flush();
            }

            // Читаем ответ из stdout Python-скрипта
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            // Читаем ошибки из stderr Python-скрипта
            StringBuilder errorOutput = new StringBuilder();
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorOutput.append(line);
                }
            }

            // Если есть ошибки, выводим их
            if (errorOutput.length() > 0) {
                System.err.println("Python error output: " + errorOutput.toString());
            }

            // Если вывод пуст, то это ошибка
            if (output.length() == 0) {
                return new PredictionResponse("ERROR", null, "Python script returned empty output.");
            }

            // Обрабатываем полученный ответ в формате JSON
            String resultJson = output.toString().trim();  // Убираем лишние пробелы в начале и конце
            System.out.println("Python output: " + resultJson); // Логируем вывод Python

            // Обрабатываем JSON как ответ
            ObjectMapper jsonMapper = new ObjectMapper();

            // Пробуем распарсить ответ
            try {
                // Если результат содержит статус ошибки
                if (resultJson.contains("\"status\":\"ERROR\"")) {
                    return jsonMapper.readValue(resultJson, PredictionResponse.class);
                } else {
                    return jsonMapper.readValue(resultJson, PredictionResponse.class);
                }
            } catch (IOException e) {
                return new PredictionResponse("ERROR", null, "Error parsing Python output JSON: " + e.getMessage());
            }

        } catch (IOException e) {
            // Если возникла ошибка ввода-вывода, возвращаем ошибку с её сообщением
            return new PredictionResponse("ERROR", null, "IOException: " + e.getMessage());
        } catch (Exception e) {
            // Обработка любых других исключений
            return new PredictionResponse("ERROR", null, "Exception: " + e.getMessage());
        }
    }
}
