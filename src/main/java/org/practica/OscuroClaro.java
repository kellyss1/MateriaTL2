package org.practica;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OscuroClaro {

    static void main() throws IOException {
        File file = new File("imagenes/paisaje.jpg");
        BufferedImage original = ImageIO.read(file);
        generarAmanecer(original);
    }

    public static void generarAmanecer(BufferedImage original) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        // 1 sola matriz de convolución (identidad que escala el brillo)
        // Usaremos un factor que aumentará en cada imagen.
        float[][] matrizBase = {
                {0f, 0f, 0f},
                {0f, 1f, 0f},
                {0f, 0f, 0f}
        };

        for (int k = 1; k <= 10; k++) {
            BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            float factor = k * 0.1f; // De 0.2 a 2.0 para efecto amanecer

            for (int y = 1; y < alto - 1; y++) {
                for (int x = 1; x < ancho - 1; x++) {
                    int sumaR = 0, sumaG = 0, sumaB = 0;

                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int pixel = original.getRGB(x + i, y + j);
                            int r = (pixel >> 16) & 0xFF;
                            int g = (pixel >> 8) & 0xFF;
                            int b = pixel & 0xFF;

                            // Aplicamos la matriz con el factor
                            sumaR += (int) (r * matrizBase[i + 1][j + 1] * (1-factor));
                            sumaG += (int) (g * matrizBase[i + 1][j + 1] * (1-factor));
                            sumaB += (int) (b * matrizBase[i + 1][j + 1] * (1-factor));
                        }
                    }

                    int r = Math.clamp(sumaR, 0, 255);
                    int g = Math.clamp(sumaG, 0, 255);
                    int b = Math.clamp(sumaB, 0, 255);

                    int pixelNuevo = (r << 16) | (g << 8) | b;
                    buffer.setRGB(x, y, pixelNuevo);
                }
            }
            File output = new File("imagenes/amanecer_" + k + ".jpg");
            ImageIO.write(buffer, "jpg", output);
            System.out.println("Generada: " + output.getName() + " con factor de brillo: " + factor);
        }
    }
}
