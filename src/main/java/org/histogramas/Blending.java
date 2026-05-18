package org.histogramas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Blending {

    static void main() throws IOException {
        File file = new File("imgHist/paisaje2.jpg");
        BufferedImage buffer1 = ImageIO.read(file);

        File file1 = new File("imgHist/castillo.jpg");
        BufferedImage buffer2 = ImageIO.read(file1);

        //el ancho y alto de la imagen mas pequeña
        int ancho = buffer1.getWidth();
        int alto = buffer1.getHeight();


        Image imgTemp = buffer2.getScaledInstance(ancho, alto, BufferedImage.SCALE_FAST);
        BufferedImage bufferTemp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        Graphics2D grTemp = bufferTemp.createGraphics();
        grTemp.drawImage(imgTemp, 0,0, null);

        buffer2 = bufferTemp;

        BufferedImage bufferBlend = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        File result = new File("imgHist/blend.jpg");

        int r, g,b, pixel, pixel2, pixelBlend;
        int r1, g1, b1, r2, g2,b2;

        //Blending
        float alpha = 0.5f;

        try {

            for(int y = 0; y < alto; y++){
                for(int x = 0; x < ancho; x++){
                    if (x < 800 && y < 409) {
                        pixel = buffer1.getRGB(x, y);
                        pixel2 = buffer2.getRGB(x,y);

                        r1 = (pixel >> 16) & 0xFF;
                        g1 = (pixel >> 8) & 0xFF;
                        b1 = (pixel) & 0xFF;

                        r2 = (pixel2 >> 16) & 0xFF;
                        g2 = (pixel2 >> 8) & 0xFF;
                        b2 = (pixel2) & 0xFF;

                        //alpha blending
                        r = (int) ((1-alpha) * r1 + alpha *r2);
                        g = (int) ((1-alpha) * g1 + alpha *g2);
                        b = (int) ((1-alpha) * b1 + alpha *b2);

                        // aditive blending
//                        r = Math.min(255, r1+r2);
//                        g = Math.min(255, g1+g2);
//                        b = Math.min(255, b1+b2);

                        // Multiplicative blending
//                        r = (r1 * r2) / 255;
//                        g = (g1 * g2) / 255;
//                        b = (b1 * b2) / 255;


                        pixelBlend = (r << 16)|(g << 8)|b;
                        bufferBlend.setRGB(x, y, pixelBlend);
                    }

                }
            }

            ImageIO.write(bufferBlend, "jpg", result);
            System.out.print("La imagen se guardo con exito!\n");
        } catch (Exception e) {
            System.out.printf("Error en la imagen: %s\n", e.getMessage());
        }

    }
}