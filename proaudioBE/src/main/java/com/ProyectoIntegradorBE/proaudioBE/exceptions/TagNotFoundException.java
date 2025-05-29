package com.ProyectoIntegradorBE.proaudioBE.exceptions;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long id) {
        super("Tag not found with id: " + id);
    }
}
