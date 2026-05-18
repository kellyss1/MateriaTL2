package org.practica.dos;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class NuevaP {

    static void main() throws IOException {
        File file = new File("imgP/paisaje2.jpg");
        BufferedImage original = ImageIO.read(file);
        rgb(original, 10, 0.5f);

        hsb(original, 1.2f, 1.4f);

        histograma(original);
    }

    public static void rgb (BufferedImage original, int brillo, float alpha) throws IOException {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int a, r, g, b, pixel, pixelNuevo;

        for(int y=0; y < alto; y++){
            for( int x=0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                //reducir y extender bits 3
                int mascaraRecorte = 0b111;  //tres unos
                r = (r >> 5) & mascaraRecorte;  // como estoy recortando 3 solo debo leer 5
                g = (g >> 5) & mascaraRecorte;
                b = (b >> 5) & mascaraRecorte;

                //ahora como recortamos tenemos que estirar, elevado a la potencia del exponente y menos 1
                int exponente = 5;
                r = (r * 255) /(int) Math.pow(2, exponente) -1;
                g = (g * 255) / (int) Math.pow(2, exponente) -1;
                b = (b *255)/ (int) Math.pow(2, exponente) -1;

                // aplicar filtro de brillo
                r = Math.min(255, r+brillo);
                g = Math.min(255, g+brillo);
                b = Math.min(255, b+ brillo);

                //aplicar el filtro de transparencia
                a = (int) Math.min(255, a*alpha);

                pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b);
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        File file = new File("imgP/rgb.png");
        ImageIO.write(buffer, "png", file);
    }

    public static void hsb (BufferedImage original, float saturacion, float brillo) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int r, g, b, pixel, pixelNuevo;

        for(int y=0; y < alto; y++){
            for( int x=0; x < ancho; x++) {
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
                s = Math.min(1, s*saturacion);
                v = Math.min(1, v*brillo);

                pixelNuevo = Color.HSBtoRGB(h,s,v);
                buffer.setRGB(x,y,pixelNuevo);
            }
        }
        File file = new File("imgP/hsv.jpg");
        ImageIO.write(buffer, "jpg", file);
    }

    public static void histograma(BufferedImage original) throws IOException {

        File histograma = new File("imgP/histograma.jpg");

        int ancho = original.getWidth();
        int alto = original.getHeight();

        // Configuración del lienzo del histograma
        int anchoHisto = 800;
        int altoHisto = 600;
        int margen = 20;
        BufferedImage lienzo = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = lienzo.createGraphics();

        // 1. Fondo
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, anchoHisto, altoHisto);

        // 2. Arreglos para frecuencias
        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];

        // 3. Conteo de píxeles
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                histoR[(pixel >> 16) & 0xFF]++;
                histoG[(pixel >> 8) & 0xFF]++;
                histoB[pixel & 0xFF]++;
            }
        }

        // 4. Calcular el máximo global para el escalado
        int maxR = obtenerMaximo(histoR);
        int maxG = obtenerMaximo(histoG);
        int maxB = obtenerMaximo(histoB);
        int maxGlobal = Math.max(maxR, Math.max(maxG, maxB));

        float escalaX = (float) anchoHisto / 256;
        float escalaY = (maxGlobal > 0) ? (float) (altoHisto - margen) / maxGlobal : 1.0f;

        // 5. Dibujar los canales
        g2d.setStroke(new BasicStroke(2));

        // Dibujamos en orden para que se vean las superposiciones
        dibujarCanal(g2d, histoR, Color.RED, escalaX, escalaY, altoHisto);
        dibujarCanal(g2d, histoG, Color.GREEN, escalaX, escalaY, altoHisto);
        dibujarCanal(g2d, histoB, Color.BLUE, escalaX, escalaY, altoHisto);

        g2d.dispose();
        ImageIO.write(lienzo, "jpg", histograma);
        System.out.println("Histograma RGB creado con éxito");
    }

    private static void dibujarCanal(Graphics2D g, int[] datos, Color color, float ex, float ey, int alto) {
        g.setColor(color);
        for (int i = 1; i < datos.length; i++) {
            int x1 = (int) ((i - 1) * ex);
            int y1 = (alto - 10) - (int) (datos[i - 1] * ey);
            int x2 = (int) (i * ex);
            int y2 = (alto - 10) - (int) (datos[i] * ey);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private static int obtenerMaximo(int[] h) {
        int max = 0;
        for (int val : h) if (val > max) max = val;
        return max;
    }



}
