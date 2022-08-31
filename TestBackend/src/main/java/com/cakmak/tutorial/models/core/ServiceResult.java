package com.cakmak.tutorial.models.core;

import org.springframework.http.HttpStatus;

public class ServiceResult<E> {
    private HttpStatus status = HttpStatus.OK;
    private E value;
    private String code = "0";
    private String message = "";
    private Throwable exception;

    public ServiceResult() {
        //
    }

    public ServiceResult(ServiceResult<?> result) {
        this.value = (E) result.getValue();
        this.status = result.getStatus();
        this.code = result.getCode();
        this.message = result.getMessage();
        this.exception = result.getException();
    }

    @Override
    public String toString() {
        return " ServiceResult{" +
                "status=" + status +
                ", value=" + value +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", exception=" + exception +
                '}';
    }

    public ServiceResult(E value) {
        this.value = value;
    }
    public ServiceResult(HttpStatus httpStatus,E value) {
        this.value = value;
        this.status = httpStatus;
    }


    public ServiceResult(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ServiceResult(HttpStatus status, String code, String message, Throwable exception) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.exception = exception;
    }

    public ServiceResult(HttpStatus status, String code, Throwable exception) {
        this.status = status;
        this.code = code;
        this.exception = exception;
    }

    public ServiceResult(HttpStatus status, Throwable exception) {
        this.status = status;
        this.exception = exception;
    }

    public ServiceResult(HttpStatus status, Throwable exception, String message) {
        this.status = status;
        this.exception = exception;
        this.message = message;
    }

    public E getValue() {
        return value;
    }

    public ServiceResult<E> setValue(E value) {
        this.value = value;
        return this;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ServiceResult<E> setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ServiceResult<E> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResult<E> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return status == HttpStatus.OK && exception == null;
    }

    public boolean isSuccessAndExistValue() {
        return status == HttpStatus.OK && exception == null && value != null;
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
