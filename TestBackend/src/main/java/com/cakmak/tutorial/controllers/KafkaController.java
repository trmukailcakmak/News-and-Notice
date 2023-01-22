package com.cakmak.tutorial.controllers;

import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.payload.response.kafka.MyKafkaGeneralResponse;
import com.cakmak.tutorial.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;
    @GetMapping("/my-topic")
    public ResponseEntity<MyKafkaGeneralResponse> getAllNews(@RequestParam(required = false) String title) {
        try {
            ServiceResult<MyKafkaGeneralResponse> serviceResult = kafkaService.sendGeneralKafkaData();
            return new ResponseEntity<>(serviceResult.getValue(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
