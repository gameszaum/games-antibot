package com.gameszaum.antibot.exception;

public class InvalidCheckException extends Exception {

    public InvalidCheckException(String checker) {
        super("Impossible to check proxy from this IP: " + checker);
    }

}
