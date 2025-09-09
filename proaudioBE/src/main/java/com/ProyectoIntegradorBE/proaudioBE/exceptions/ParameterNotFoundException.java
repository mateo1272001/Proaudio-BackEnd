package com.ProyectoIntegradorBE.proaudioBE.exceptions;

public class ParameterNotFoundException extends RuntimeException {
    public ParameterNotFoundException(Long id) {
        super("Parametro no encontrado: " + id);
    }
}
