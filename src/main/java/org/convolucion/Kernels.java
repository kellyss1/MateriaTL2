package org.convolucion;

public class Kernels {

    //NO modifica la imagen
    public static final float[] kNormal = {
      0f,0f,0f,
      0f,1f,0f,
      0f,0f,0f
    };

    //Kernel de enfoque (sharpen)
    public static final float[] kEnfoque = {
            0,-1f,0f,
            -1f,5f,-1f,
            0f,-1f,0f
    };

    //Desenfoque (blur)
    public final float[] kDesenfoque = {
            1f/9,1f/9,1f/9,
            1f/9,1f/9,1f/9,
            1f/9,1f/9,1f/9
    };

    //Deteccion de Bordes
    public static final float[] kBordes = {
      -0.5f, -0.5f,  -0.5f,
      -0.5f, 4f,  -0.5f,
      -0.5f, -0.5f,  -0.5f,
    };

    //Aclarar
    public static final float[] kAclaracion = {
      0.1f,0.1f,0.1f,
      0.1f,1f,0.1f,
      0.1f,0.1f,0.1f,
    };

    // Oscurecer (reduce el brillo a la mitad)
    public static final float[] kOscurecer = {
            0.0f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.0f
    };

}
