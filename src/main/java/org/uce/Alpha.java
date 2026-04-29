package org.uce;

import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;

public class Alpha {
    static void main() {

        int mascaraRecorteBit =  0b11; //15; //hexadecimal

    }

    public static BufferedImage filtroTransparencia(BufferedImage original, float factorAlfa) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int pixel, r, g, b, a, pixelNuevo;

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++){
                pixel = original.getRGB(x,y);

                //Es un jpg no soporta transparencias
                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // Filtro Transparencia (Alpha)
                a = (int) Math.min(255, a*factorAlfa);
                pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b);

                int mascaraRecorteBit =  0b11; //15; //hexadecimal

                //Recorte a 4 bits
                r = (r >> 6) & mascaraRecorteBit;
                g = (g >> 6) & mascaraRecorteBit;
                b = (b >> 6) & mascaraRecorteBit;

                //Estirar
//                r = (r*255)/15;
//                g = (g*255)/15;
//                b = (b*255)/15;
                //Dos canales
                r = (r*255)/3;
                g = (g*255)/3;
                b = (b*255)/3;



                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }
}
