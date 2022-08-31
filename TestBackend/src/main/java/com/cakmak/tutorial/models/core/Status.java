package com.cakmak.tutorial.models.core;

public enum Status {
    SUCCESS(0),
    FAIL(1);

    private int value;

    private Status(Integer value){
        this.value = value;
    }
}
