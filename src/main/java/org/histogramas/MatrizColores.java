package org.histogramas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MatrizColores {

    static void main() throws IOException {
        File file = new File("images/balon.jpg");
        BufferedImage original = ImageIO.read(file);

        int width = original.getWidth();
        int height = original.getHeight();

        File file1 = new File("images/balonGris.jpg");
        BufferedImage buffer2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int a, r, g,b, pixel, pixelNuevo;
        int r1, g1, b1;
        int mascara = 0xFF;

        try {
            float [][] colores = {
                    {0.299f, 0.587f, 0.114f, 0.0f},
                    {0.299f, 0.587f, 0.114f, 0.0f},
                    {0.299f, 0.587f, 0.114f, 0.0f},
                    {0.0f, 0.0f, 0.0f, 1.0f},
            };
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    pixel = original.getRGB(x, y);

                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel) & 0xFF;

                    r1 = (int) (colores[0][0] *r + colores[0][1]*g + colores[0][2]*b);
                    g1 = (int) (colores[1][0] *r + colores[1][1]*g + colores[1][2]*b);
                    b1 = (int) (colores[2][0] *r + colores[2][1]*g + colores[2][2]*b);

                    r1 = Math.clamp(r1, 0, 255);
                    g1 = Math.clamp(g1, 0, 255);
                    b1 = Math.clamp(b1, 0, 255);

                    pixelNuevo = (r1 << 16)|(g1 << 8)|b1;
                    buffer2.setRGB(x, y, pixelNuevo);
                }
            }

            ImageIO.write(buffer2, "jpg", file1);
            System.out.print("Matriz colores salvo com sucesso!\n");
        } catch (Exception e) {
            System.out.printf("Erro ao ler imagem: %s\n", e.getMessage());
        }

    }
}
