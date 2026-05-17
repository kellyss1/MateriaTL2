package org.practica;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Convolucion {

    static void main() throws IOException {

        File perro = new File("images/perro.jpg");
        BufferedImage original = ImageIO.read(perro);

        File perroCV = new File("images/perroCVFor.jpg");
        ImageIO.write(desenfoque(original), "jpg", perroCV);

        File perroCVBi = new File("images/perroBi.jpg");
        ImageIO.write(convolucionBi(original), "jpg", perroCVBi);

        // Generar 10 imágenes con efecto amanecer
        OscuroClaro.generarAmanecer(original);

    }

    public static BufferedImage desenfoque(BufferedImage original) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int pixel, pixelNuevo, r, g, b;
        int sumaR, sumaG, sumaB, indice=0;

//        float[] matriz ={
//                1f/9,1f/9,1f/9,
//                1f/9,1f/9,1f/9,
//                1f/9,1f/9,1f/9
//        };

        float[] matriz = Kernels.aclaracion2;


        for (int y = 1; y < alto -1; y++) {
            for (int x =1; x < ancho-1; x++){

                sumaR = sumaG = sumaB = indice =0;

                for (int i = -1; i < 2; i++) {
                    for (int j= -1; j< 2; j++) {
                        pixel = original.getRGB(x+j,y+i);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = pixel & 0xFF;

                        sumaR += (r * matriz[indice]);
                        sumaG += (g * matriz[indice]);
                        sumaB += (b * matriz[indice]);

                        indice++;
                    }
                }

                r = Math.min(255, Math.max(0, Math.round(sumaR)));
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | b;
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }

        return buffer2;
    }

    public static BufferedImage convolucionBi(BufferedImage original) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        int r, g, b, pixel, pixelNuevo;
        int sumaR, sumaG, sumaB;

        float[][] matriz = {
                {0f,-1f,0f},
                {-1f,5f,-1f},
                {0f,-1f,0f}
        };

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y=1; y < alto -1; y++) {
            for (int x = 1; x < ancho-1; x ++) {

                sumaR = sumaG = sumaB = 0;

                for (int i = -1; i < 2; i++) {
                    for(int j = -1; j < 2; j++) {
                        pixel = original.getRGB(x+i, y+j);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = (pixel) & 0xFF;

                        sumaR += (int) (r * matriz[i+1][j+1]);
                        sumaG += (int) (g * matriz[i+1][j+1]);
                        sumaB += (int) (b * matriz[i+1][j+1]);

                    }
                }

                r = Math.clamp(Math.round(sumaR), 0,255);
                g = Math.clamp(Math.round(sumaG), 0,255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | b;
                buffer2.setRGB(x, y, pixelNuevo);

            }
        }

        return buffer2;
    }

}
