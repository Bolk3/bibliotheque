package com.bibliotheque.erreurs;

public class RegexFormatError extends Exception{

    public RegexFormatError(String errorMessage) {
        super(errorMessage);
    }
}
