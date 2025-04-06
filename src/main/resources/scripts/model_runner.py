import sys
import json
import pickle
import numpy as np
import os
from tensorflow.keras.models import load_model
import logging

# Настройка логов TensorFlow
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # Отключаем все логи TensorFlow
logging.getLogger('tensorflow').setLevel(logging.ERROR)

def load_models():
    try:
        # Перенаправляем stderr в devnull для TensorFlow
        sys.stderr = open(os.devnull, 'w')
        model = load_model("rating_predictor_finetuned.h5")
        with open("vectorizer_finetuned.pkl", "rb") as f:
            vectorizer = pickle.load(f)
        with open("brand_encoder_finetuned.pkl", "rb") as f:
            brand_encoder = pickle.load(f)
        with open("scaler_finetuned.pkl", "rb") as f:
            scaler = pickle.load(f)
        sys.stderr = sys.__stderr__  # Восстанавливаем stderr
        return model, vectorizer, brand_encoder, scaler
    except Exception as e:
        sys.stderr = sys.__stderr__
        print(f"Error loading models: {e}", file=sys.stderr)
        raise

def main():
    try:
        input_json = sys.stdin.read()
        if not input_json.strip():
            raise ValueError("Received empty input JSON.")
        
        data = json.loads(input_json)
        
        product_name = data["productName"]
        description = data["description"]
        brand_name = data["brandName"]
        price = float(data["price"])

        model, vectorizer, brand_encoder, scaler = load_models()

        text_features = f"{product_name} {description}"
        X_text = vectorizer.transform([text_features]).toarray()

        try:
            X_brand = brand_encoder.transform([[brand_name]]).reshape(-1, 1)
        except ValueError:
            X_brand = np.array([[0]])

        X_price = scaler.transform(np.array([[price]]))

        # Перенаправляем stdout временно, чтобы избежать вывода прогресса
        old_stdout = sys.stdout
        sys.stdout = open(os.devnull, 'w')
        rating_pred = model.predict([X_text, X_price, X_brand])
        sys.stdout = old_stdout
        
        rating_pred_clipped = float(np.clip(rating_pred[0], 0, 5))

        result = {
            "status": "SUCCESS",
            "predictedRating": round(rating_pred_clipped, 2),
            "error": None
        }

        # Очищаем stdout перед выводом JSON
        print(json.dumps(result), file=sys.stdout, flush=True)

    except Exception as e:
        result = {
            "status": "ERROR",
            "predictedRating": None,
            "error": str(e)
        }
        print(json.dumps(result), file=sys.stdout, flush=True)

if __name__ == "__main__":
    main()