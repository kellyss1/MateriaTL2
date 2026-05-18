package org.practica.dos;

public class Kernels {
    public static final float[] normal = {
            0f,0f,0f,
            0f,1f,0f,
            0f,0f,0f
    };

    //La suma total es 1 (5 - 4 = 1)
    // la imagen mantiene su brillo general, pero el contraste entre los píxeles vecinos se vuelve más agresivo
    public static final float[] enfoque = {
            0f,-1f,0f,
            -1f,5f,-1f,
            0f,-1f,0f
    };

    //Suma es igual a 1. Promedio de los vecinos y toma prestado brillo del otro
    public static final float[] desenfoque = {
            1f/9, 1f/9, 1f/9,
            1f/9, 1f/9, 1f/9,
            1f/9, 1f/9, 1f/9
    };

    //(8 veces -0.5 es -4, más el 4 del centro = 0)
    // Esto hace que las zonas planas de la imagen (donde no hay bordes)
    // se vuelvan totalmente negras, iluminando únicamente las siluetas.
    public static final float[] bordes = {
            -0.5f, -0.5f, -0.5f,
            -0.5f,4f, -0.5f,
            -0.5f,-0.5f,-0.5f
    };

    //La suma de este kernel es 1.8. Como es mayor a 1, efectivamente aclarará la foto.
    // Sin embargo, al multiplicar los valores casi al doble, los píxeles llegarán muy rápido al límite de 255
    //sino que también estaba desenfocando la imagen porque tomaba luz prestada de los vecinos (los 0.1 en los bordes)
    public static final float[] aclaracion = {
            0.1f,0.1f,0.1f,
            0.1f,1f,0.1f,
            0.1f,0.1f,0.1f
    };

    // Aclarar (Aumenta el brillo en un 20% sin desenfocar)
    public static final float[] aclaracion2 = {
            0.0f, 0.0f, 0.0f,
            0.0f, 1.2f, 0.0f,
            0.0f, 0.0f, 0.0f
    };

    // Oscurecer (reduce el brillo a la mitad)
    public static final float[] oscurecer = {
            0.0f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.0f
    };



}
