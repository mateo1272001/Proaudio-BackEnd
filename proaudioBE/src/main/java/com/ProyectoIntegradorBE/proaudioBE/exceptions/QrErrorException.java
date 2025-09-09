package com.ProyectoIntegradorBE.proaudioBE.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class QrErrorException extends RuntimeException {
    public QrErrorException(String message) {
        super(message);
    }
}
