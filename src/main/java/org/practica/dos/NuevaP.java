package org.practica.dos;

import org.practica.dos.Kernels;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NuevaP {

    static void main() throws IOException {
        File file = new File("imgP/paisaje2.jpg");
        BufferedImage original = ImageIO.read(file);
        rgb(original, 10, 0.5f);

        hsb(original, 1.2f, 1.4f);

        histograma(file);

        convolucion(original);

        convolucionBi(original);

        matrizColores(original);

        File file2 = new File("imgP/castillo.jpg");
        BufferedImage buffer2 = ImageIO.read(file2);
        blending(original,buffer2);


    }

    public static void rgb(BufferedImage original, int brillo, float alpha) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int a, r, g, b, pixel, pixelNuevo;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                //reducir y extender bits 3  (usamos solo 3, recortamos 5)
                int mascaraRecorte = 0b111;  //tres unos
                r = (r >> 5) & mascaraRecorte;  // como estoy recortando 3 solo debo leer 5
                g = (g >> 5) & mascaraRecorte;
                b = (b >> 5) & mascaraRecorte;

                //ahora como recortamos tenemos que estirar, elevado a la potencia del exponente y menos 1
                int exponente = 3;
                r = (r * 255) / (int) Math.pow(2, exponente) - 1;
                g = (g * 255) / (int) Math.pow(2, exponente) - 1;
                b = (b * 255) / (int) Math.pow(2, exponente) - 1;

                // aplicar filtro de brillo
                r = Math.min(255, r + brillo);
                g = Math.min(255, g + brillo);
                b = Math.min(255, b + brillo);

                //aplicar el filtro de transparencia
                a = (int) Math.min(255, a * alpha);

                pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b);
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        File file = new File("imgP/rgb.png");
        ImageIO.write(buffer, "png", file);
    }

    public static void hsb(BufferedImage original, float saturacion, float brillo) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int r, g, b, pixel, pixelNuevo;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                float h, s, v;
                h = hsb[0];
                s = hsb[1];
                v = hsb[2];

                //aplicar filtros
                // Aumenta la saturación un 20% y el brillo un 50%
                //aplicarHSV(original, 1.2f, 1.5f);
                s = Math.min(1, s * saturacion);
                v = Math.min(1, v * brillo);

                pixelNuevo = Color.HSBtoRGB(h, s, v);
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        File file = new File("imgP/hsv.jpg");
        ImageIO.write(buffer, "jpg", file);
    }

    public static void convolucion(BufferedImage original) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int pixel, pixelNuevo, r, g, b;
        int sumaR, sumaG, sumaB, indice = 0;


        float[] matriz = Kernels.bordes;


        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {

                sumaR = sumaG = sumaB = indice = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        pixel = original.getRGB(x + j, y + i);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = pixel & 0xFF;

                        sumaR += (int) (r * matriz[indice]);
                        sumaG += (int) (g * matriz[indice]);
                        sumaB += (int) (b * matriz[indice]);

                        indice++;
                    }
                }

                r = Math.clamp(Math.round(sumaR), 0, 255);
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | b;
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        File file = new File("imgP/convolucion.jpg");
        ImageIO.write(buffer, "jpg", file);

    }

    public static void convolucionBi(BufferedImage original) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        int r, g, b, pixel, pixelNuevo;
        int sumaR, sumaG, sumaB;

        float[][] matriz = {
                {0f, -1f, 0f},
                {-1f, 5f, -1f},
                {0f, -1f, 0f}
        };

        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {

                sumaR = sumaG = sumaB = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        pixel = original.getRGB(x + i, y + j);

                        r = (pixel >> 16) & 0xFF;
                        g = (pixel >> 8) & 0xFF;
                        b = (pixel) & 0xFF;

                        sumaR += (int) (r * matriz[i + 1][j + 1]);
                        sumaG += (int) (g * matriz[i + 1][j + 1]);
                        sumaB += (int) (b * matriz[i + 1][j + 1]);

                    }
                }

                r = Math.clamp(Math.round(sumaR), 0, 255);
                g = Math.clamp(Math.round(sumaG), 0, 255);
                b = Math.clamp(Math.round(sumaB), 0, 255);

                pixelNuevo = (r << 16) | (g << 8) | b;
                buffer2.setRGB(x, y, pixelNuevo);

            }
        }
        File file = new File("imgP/convolucionBi.jpg");
        ImageIO.write(buffer2, "jpg", file);
    }

    public static void histograma(File file) throws IOException {

        BufferedImage original = ImageIO.read(file);

        int ancho = original.getWidth();
        int alto = original.getHeight();

        int r,g,b, pixel;
        int maxR, maxG, maxB;
        float escalaYR, escalaYG, escalaYB;

        // Configuración del lienzo del histograma
        int anchoHisto = 800;
        int altoHisto = 600;

        BufferedImage bufferHisto = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);

        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];

        Graphics2D gr = bufferHisto.createGraphics();
        gr.setStroke(new BasicStroke(2));

        gr.setColor(Color.WHITE);
        gr.fillRect(0,0,anchoHisto, altoHisto);

        // recorrer la imagen
        for (int y =0; y < alto; y++) {
            for (int x=0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = pixel & 0xFF;

                histoR[r]++;
                histoG[g]++;
                histoB[b]++;
            }
        }

        // escala
        float escalaX = anchoHisto / 256.0f;

        // dibujar histogramas, dibujar rojo
        maxR = maximo(histoR);
        escalaYR = (float) altoHisto / maxR;
        gr.setColor(Color.RED);
        for(int i=1; i < histoR.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYR * histoR[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYR * histoR[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        //dibujar verde
        maxG = maximo(histoG);
        escalaYG = (float) altoHisto / maxG;
        gr.setColor(Color.GREEN);
        for(int i=1; i < histoG.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYG * histoG[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYG * histoG[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        //dibujar azul
        maxB = maximo(histoB);
        escalaYB = (float) altoHisto / maxB;
        gr.setColor(Color.BLUE);
        for(int i=1; i < histoB.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYB * histoB[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYB * histoB[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        File histograma = new File("imgP/histograma.jpg");
        ImageIO.write(bufferHisto, "jpg", histograma);
    }

    public static int maximo(int[] h) {
        int max =0;
        for (int val : h) if (val > max) max = val;
        System.out.println(Arrays.stream(h).max().getAsInt());
        return max;
    }

    public static void matrizColores(BufferedImage original) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        float[][] matrizSepia = {
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f},
        };

        int r,g,b, pixel, pixelNuevo;
        int r1, g1, b1;

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for(int y=0; y < alto; y++) {
            for(int x=0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = pixel & 0xFF;

                r1 = (int) (matrizSepia[0][0] * r + matrizSepia[0][1] * g + matrizSepia[0][2] * b);
                g1 = (int) (matrizSepia[1][0] * r + matrizSepia[1][1] * g + matrizSepia[1][2] * b);
                b1 = (int) (matrizSepia[2][0] * r + matrizSepia[2][1] * g + matrizSepia[2][2] * b);

                r1 = Math.clamp(r1, 0, 255);
                g1 = Math.clamp(g1, 0, 255);
                b1 = Math.clamp(b1, 0, 255);

                pixelNuevo = (r1 << 16) |(g1 << 8) |(b1);
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        File file = new File("imgP/sepiaMatriz.jpg");
        ImageIO.write(buffer, "jpg", file);
    }


    public static void blending(BufferedImage bufferimg1, BufferedImage bufferimg2) throws IOException {
        //el ancho y alto de la imagen más pequeña
        int ancho = bufferimg1.getWidth();
        int alto = bufferimg1.getHeight();

        Image imgTemp = bufferimg2.getScaledInstance(ancho, alto, Image.SCALE_FAST);
        BufferedImage bufferTemp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        Graphics2D grTemp = bufferTemp.createGraphics();
        grTemp.drawImage(imgTemp, 0, 0, null);

        bufferimg2 = bufferTemp;

        BufferedImage bufferBlend = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int r, g, b, pixel, pixel2, pixelBlend;
        int r1, g1, b1, r2, g2, b2;

        //Blending
        float alpha = 0.5f;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (x < 800 && y < 409) {
                    pixel = bufferimg1.getRGB(x, y);
                    pixel2 = bufferimg2.getRGB(x, y);

                    r1 = (pixel >> 16) & 0xFF;
                    g1 = (pixel >> 8) & 0xFF;
                    b1 = (pixel) & 0xFF;

                    r2 = (pixel2 >> 16) & 0xFF;
                    g2 = (pixel2 >> 8) & 0xFF;
                    b2 = (pixel2) & 0xFF;

                    //alpha blending
                    r = (int) ((1 - alpha) * r1 + alpha * r2);
                    g = (int) ((1 - alpha) * g1 + alpha * g2);
                    b = (int) ((1 - alpha) * b1 + alpha * b2);

                    // aditive blending
//                        r = Math.min(255, r1+r2);
//                        g = Math.min(255, g1+g2);
//                        b = Math.min(255, b1+b2);

                    // Multiplicative blending
//                        r = (r1 * r2) / 255;
//                        g = (g1 * g2) / 255;
//                        b = (b1 * b2) / 255;


                    pixelBlend = (r << 16) | (g << 8) | b;
                    bufferBlend.setRGB(x, y, pixelBlend);
                }

            }
        }
        File result = new File("imgP/blend.jpg");
        ImageIO.write(bufferBlend, "jpg", result);
    }

    public static void blending3(BufferedImage buffer, BufferedImage buffer2, BufferedImage buffer3) throws IOException {
        int alto = buffer2.getHeight();
        int ancho = buffer2.getWidth();

        Image imgTemp1 = buffer.getScaledInstance(ancho, alto, Image.SCALE_FAST);

        BufferedImage bufferTemp1 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D grTemp1 = bufferTemp1.createGraphics();
        grTemp1.drawImage(imgTemp1, 0, 0, null);
        grTemp1.dispose();

        buffer = bufferTemp1;


        Image imgTemp3 = buffer3.getScaledInstance(ancho, alto, Image.SCALE_FAST);

        BufferedImage bufferTemp3 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D grTemp3 = bufferTemp3.createGraphics();
        grTemp3.drawImage(imgTemp3, 0, 0, null);
        grTemp3.dispose();

        buffer3 = bufferTemp3;


        BufferedImage bufferBlend = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int r,g,b, pixel1, pixel2, pixel3, pixelBlend;
        int r1, g1, b1, r2, g2, b2, r3, g3, b3;

        float alpha1 = 0.33f;
        float alpha2 = 0.33f;
        float alpha3 = 0.34f;

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                pixel1 = buffer.getRGB(x, y);
                pixel2 = buffer2.getRGB(x, y);
                pixel3 = buffer3.getRGB(x, y);

                // Imagen 1
                r1 = (pixel1 >> 16) & 0xFF;
                g1 = (pixel1 >> 8) & 0xFF;
                b1 = pixel1 & 0xFF;

                // Imagen 2
                r2 = (pixel2 >> 16) & 0xFF;
                g2 = (pixel2 >> 8) & 0xFF;
                b2 = pixel2 & 0xFF;

                // Imagen 3
                r3 = (pixel3 >> 16) & 0xFF;
                g3 = (pixel3 >> 8) & 0xFF;
                b3 = pixel3 & 0xFF;

                r = (int) (alpha1 * r1 + alpha2 * r2 + alpha3 * r3);

                g = (int) (alpha1 * g1 + alpha2 * g2 + alpha3 * g3);

                b = (int) (alpha1 * b1 + alpha2 * b2 + alpha3 * b3);

                pixelBlend = (r << 16) | (g << 8) | b;

                bufferBlend.setRGB(x, y, pixelBlend);
            }
        }
        File salida = new File("imgP/blending3.png");
        ImageIO.write(bufferBlend, "png", salida);
    }

}
