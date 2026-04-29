package org.convolucion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Convolucion {

    public static void main(String[] args) {

        File file = new File("images/perro.jpg");
        File file2 = new File("images/perro2.jpg");

        try {
            BufferedImage buffer = ImageIO.read(file);
            BufferedImage buffer2 = buffer;

            for (int i = 0; i < 20; i++) {
                buffer2 = convolucion9x9(buffer2);
            }

            ImageIO.write(buffer2, "jpg", file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage convolucion(BufferedImage original) {
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;

        float sumaR, sumaG, sumaB;
        int indice = 0;

        float[] matrizConv = {
                1f / 9, 1f / 9, 1f / 9,
                1f / 9, 1f / 9, 1f / 9,
                1f / 9, 1f / 9, 1f / 9
        };

        ancho = original.getWidth();
        alto = original.getHeight();

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                //Reinicio del pixel
                sumaR = sumaG = sumaB = indice = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {

                        pixel = original.getRGB(x + j, y + i);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = pixel & 0xFF;

                        sumaR += r * matrizConv[indice];
                        sumaG += g * matrizConv[indice];
                        sumaB += b * matrizConv[indice];

                        indice++;
                    }
                }

                r = Math.clamp(Math.round(sumaR), 0, 255);
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | (b);
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer2;
    }

    public static BufferedImage convolucionBi(BufferedImage original) {
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;

        float sumaR, sumaG, sumaB;
        int indice = 0;

        float[][] matrizConv = {
                {1f / 9, 1f / 9, 1f / 9}, //fila 0
                {1f / 9, 1f / 9, 1f / 9}, //fila 1
                {1f / 9, 1f / 9, 1f / 9}
        };

        ancho = original.getWidth();
        alto = original.getHeight();

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                //Reinicio del pixel
                sumaR = sumaG = sumaB = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {

                        pixel = original.getRGB(x + j, y + i);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = pixel & 0xFF;

                        sumaR += r * matrizConv[i+1][j+1];
                        sumaG += g * matrizConv[i+1][j+1];
                        sumaB += b * matrizConv[i+1][j+1];

                        indice++;
                    }
                }

                r = Math.clamp(Math.round(sumaR), 0, 255);
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | (b);
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer2;
    }

    public static BufferedImage convolucion9x9(BufferedImage original) {
        int ancho, alto, pixel, pixelNuevo;
        int r, g, b;

        float sumaR, sumaG, sumaB;

        float[][] matrizConv = new float[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrizConv[i][j] = 1f / 81f;
            }
        }

        ancho = original.getWidth();
        alto = original.getHeight();

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Ajustamos los límites para evitar salirnos de la imagen con una matriz 9x9 (radio 4)
        for (int y = 4; y < alto - 4; y++) {
            for (int x = 4; x < ancho - 4; x++) {
                sumaR = sumaG = sumaB = 0;

                for (int i = -4; i <= 4; i++) {
                    for (int j = -4; j <= 4; j++) {
                        pixel = original.getRGB(x + j, y + i);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = pixel & 0xFF;

                        sumaR += r * matrizConv[i + 4][j + 4];
                        sumaG += g * matrizConv[i + 4][j + 4];
                        sumaB += b * matrizConv[i + 4][j + 4];
                    }
                }

                r = Math.clamp(Math.round(sumaR), 0, 255);
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | (b);
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer2;
    }
}
