package br.com.fiap.hoteldurmabem.service;


public class ReservaInvalidaException extends RuntimeException {
    public ReservaInvalidaException(String message) {
        super(message);
    }
}
