package org.practica.uno;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class PracticaConvolucionOp {
    static void main() throws IOException {

        File paisaje = new File("images/paisaje.jpg");
        BufferedImage original = ImageIO.read(paisaje);

        File resultado = new File("images/paisajeCV.jpg");
        ImageIO.write(normalCV(original), "jpg", resultado);

        //---
        File file2 = new File("images/paisajeDES.jpg");
        ImageIO.write(desenfoqueCV(original), "jpg", file2);


    }

    public static BufferedImage normalCV(BufferedImage original) {
        float[] matriz = Kernels.normal;

        Kernel kernel = new Kernel((int)Math.sqrt(matriz.length),(int) Math.sqrt(matriz.length),matriz);

        //No considerar los bordes de la imagen
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        return op.filter(original, null);
    }

    public static BufferedImage desenfoqueCV(BufferedImage original) {
        float[] matriz = Kernels.desenfoque;

        Kernel kernel = new Kernel((int)Math.sqrt(matriz.length), (int)Math.sqrt(matriz.length),matriz);

        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);

        return op.filter(original, null);
    }
}
