package org.practica;

import java.awt.image.BufferedImage;

public class Filtros {
    static void main() {

    }

    public static BufferedImage radial(BufferedImage original) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        int a, r, g, b, pixel, pixelNuevo, distancia;

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int centroX = ancho/2;
        int centroY = alto/2;

        int distanciaMax = (int) Math.sqrt((centroX*centroX)+(centroY*centroY));

        for (int y =0; y < alto; y++) {
            for (int x=0; x < ancho; x++) {

                pixel = original.getRGB(x, y);

                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = pixel & 0xFF;

                // efecto
                int dx = centroX -x;
                int dy = centroY -y;

                distancia = (int) Math.sqrt((dx*dx)+(dy*dy));

                float lejania = 1 - ( (float) distancia/ distanciaMax);
                a = (int) Math.min(255, a * lejania);

                pixelNuevo = (a << 24)|(r << 16)|(g << 8)| (b);
                buffer2.setRGB(x,y,pixelNuevo);

            }
        }

        return buffer2;
    }
}
