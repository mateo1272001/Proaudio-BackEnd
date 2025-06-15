package com.ProyectoIntegradorBE.proaudioBE.exceptions;

public class ImagesNotFoundException extends RuntimeException {
    public ImagesNotFoundException(Long id) {
        super("Imágenes no encontradas para producto con ID: " + id);
    }
}