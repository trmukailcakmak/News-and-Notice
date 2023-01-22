package com.cakmak.tutorial.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyKafkaDto implements Serializable {
    private String name;
    private String message;
}
