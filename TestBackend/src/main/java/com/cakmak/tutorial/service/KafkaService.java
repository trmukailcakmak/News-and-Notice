package com.cakmak.tutorial.service;

import com.cakmak.tutorial.models.MyKafkaDto;
import com.cakmak.tutorial.models.core.ServiceResult;
import com.cakmak.tutorial.payload.response.kafka.MyKafkaGeneralResponse;
import com.cakmak.tutorial.util.KafkaProducerUtil;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaProducerUtil kafkaProducerUtil;

    public KafkaService(KafkaProducerUtil kafkaProducerUtil) {
        this.kafkaProducerUtil = kafkaProducerUtil;
    }

    public ServiceResult<MyKafkaGeneralResponse> sendGeneralKafkaData() {
        try {
            MyKafkaGeneralResponse myKafkaGeneralResponse = new MyKafkaGeneralResponse();
            MyKafkaDto myKafkaDto = new MyKafkaDto();
            myKafkaDto.setName("TestName1");
            myKafkaDto.setMessage("TestMessage1");
            kafkaProducerUtil.sendMyKafkaGeneral(myKafkaDto, "myKey");
            return new ServiceResult<>(myKafkaGeneralResponse);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.general.name}", containerFactory = "myKafkaDtoListenerContainerFactory")
    public void canNotificationListener(MyKafkaDto myKafkaDto) {
        System.out.println("Gelen data : "+myKafkaDto.toString());
        System.out.println("Gelen data : "+myKafkaDto.getName());
    }
}
