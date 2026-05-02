package com.bibliotheque.errors;

public class RegexFormatError extends Exception{

    public RegexFormatError(String errorMessage) {
        super(errorMessage);
    }
}
