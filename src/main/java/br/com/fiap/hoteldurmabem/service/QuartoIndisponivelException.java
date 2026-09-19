package br.com.fiap.hoteldurmabem.service;


public class QuartoIndisponivelException extends RuntimeException {
    public QuartoIndisponivelException(String message) {
        super(message);
    }
}
