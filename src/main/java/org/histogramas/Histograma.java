package org.histogramas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Histograma {

    public static void main() throws IOException {
        File file = new File("imgHist/universo.jpg");
        BufferedImage original = ImageIO.read(file);

        int width = original.getWidth();
        int height = original.getHeight();

        File file1 = new File("imgHist/universoHistG.jpg");
        int anchoHisto = 800;
        int altoHisto = 600;
        BufferedImage buffer2 = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);

        int r, g, b, pixel;
        int[] histoRed = new int[256];

        try {
            Graphics2D gr = buffer2.createGraphics();
            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, anchoHisto, altoHisto);

            gr.setStroke(new BasicStroke(2));

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixel = original.getRGB(x, y);
                    r = (pixel >> 16) & 0xFF;
                    g = (pixel >> 8) & 0xFF;
                    b = (pixel) & 0xFF;
                    histoRed[g]++;
                }
            }

            float escalaX = (float) anchoHisto / 256;
            int max = maximo(histoRed);
            float escalaY = (max > 0) ? (float) (altoHisto - 20) / max : 1.0f;

            gr.setColor(Color.GREEN);
            for (int i = 1; i < histoRed.length; i++) {
                int x1 = (int) ((i - 1) * escalaX);
                int y1 = (altoHisto - 10) - (int) (histoRed[i - 1] * escalaY);

                int x2 = (int) (i * escalaX);
                int y2 = (altoHisto - 10) - (int) (histoRed[i] * escalaY);
                gr.drawLine(x1, y1, x2, y2);
            }
            gr.dispose();

            ImageIO.write(buffer2, "jpg", file1);
            System.out.print("Imagen creada correctamente¡\n");
        } catch (Exception e) {
            System.out.printf("Error al procesar la imagen: %s\n", e.getMessage());
            e.printStackTrace();
        }

    }

    public static void imprimir(int[] h) {
        for (int i = 0; i < h.length; i++) {
            System.out.println(i + " : " + h[i]);
        }
    }

    public static int maximo2(int[] h) {
        return Arrays.stream(h).max().getAsInt();
    }

    public static int maximo(int[] h) {
        int max = 0;
        int indice = 0;
        for (int i = 0; i < h.length; i++) {
            if (h[i] > max) {
                max = h[i];
                indice = i;
            }
        }
        System.out.println("El indice maximo es: " + indice + " y el valor es: " + max);
        return max;
    }
}
