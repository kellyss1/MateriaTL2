package org.convolucion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

public class ConvolucionOp {

    static void main() {
        File file = new File("images/perro.jpg");
        File file2 = new File("images/perroJavaOp.jpg");

        try {
            BufferedImage buffer = ImageIO.read(file);
            BufferedImage buffer2 = buffer;

            for (int i = 0; i <1; i++) {
                buffer2 = convolucion(buffer2);
            }

            ImageIO.write(buffer2, "jpg", file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage convolucion(BufferedImage original) {
        //Normal
        //float[] matriz = Kernels.kNormal;

        float[] matriz = Kernels.kOscurecer;

        Kernel kernel = new Kernel((int)Math.sqrt(matriz.length) , (int)Math.sqrt(matriz.length), matriz);

        //NO considerar los bordes de la imagen
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage buffer2 = op.filter(original, null);

        return buffer2;
    }
}
