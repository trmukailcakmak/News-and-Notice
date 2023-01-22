package com.cakmak.tutorial.util;

import com.cakmak.tutorial.models.MyKafkaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerUtil {
    @Value("${kafka.topic.general.name1}")
    private String generalNameTopic;
    @Autowired
    private KafkaTemplate<String, MyKafkaDto> kafkaTemplateProducerMyKafkaDto;

    public void sendMyKafkaGeneral(MyKafkaDto myKafkaDto, String key) {
        kafkaTemplateProducerMyKafkaDto.send(generalNameTopic, key, myKafkaDto);
    }
}
