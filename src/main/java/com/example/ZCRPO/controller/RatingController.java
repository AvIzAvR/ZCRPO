package com.example.ZCRPO.controller;

import com.example.ZCRPO.model.ProductRequest;
import com.example.ZCRPO.model.PredictionResponse;
import com.example.ZCRPO.service.PythonPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    private PythonPredictionService predictionService;

    @PostMapping
    public PredictionResponse getRating(@RequestBody ProductRequest request) {
        return predictionService.predict(request);
    }
}
