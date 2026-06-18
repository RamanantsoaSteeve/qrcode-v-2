package com.example.demo.exception;

// On la renomme pour que son nom exprime ce qu'elle est (une exception)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}