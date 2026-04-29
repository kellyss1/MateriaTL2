package org.uce;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Filtros {

    static void main() throws IOException {

        File file = new File("images/paisaje.jpg");
        BufferedImage original = ImageIO.read(file);

        // Filtro gris
        File filtroGris = new File("images/escalaGrises.jpg");
        ImageIO.write(aplicarEscalaGrises(original),"jpg", filtroGris);

        // Filtro Inverso
        File filtroInverso = new File("images/inverso.jpg");
        ImageIO.write(aplicarInverso(original), "jpg", filtroInverso);

        // Filtro Brillo
        File filtroBrillo = new File("images/brillo.jpg");
        ImageIO.write(aplicarBrillo(original,45),"jpg",filtroBrillo);

        // Filtro HSV
        File filtroHSV = new File("images/hsb.jpg");
        ImageIO.write(aplicarHSV(original, 0.8f, 1.5f), "jpg", filtroHSV);

        // Filtro Transparencia
        File filtroAlfa = new File("images/transparencia.png");
        ImageIO.write(aplicarTransparencia(original, 0.5f),"png",filtroAlfa);
    }

    public static BufferedImage aplicarEscalaGrises(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel, r, g, b, gris, pixelNuevo;

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++){
                pixel = original.getRGB(x,y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // Escala de Grises
                gris = (r+g+b)/3;
                pixelNuevo = (gris << 16) | (gris << 8) | (gris);

                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    public static BufferedImage aplicarInverso(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel, r, g, b, pixelNuevo;

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++){
                pixel = original.getRGB(x,y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // Filtro Inverso: un color muy claro se vuelve oscuro y viseversa
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                pixelNuevo = (r << 16) | (g << 8) | (b);

                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    public static BufferedImage aplicarBrillo(BufferedImage original, int brillo) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel, r, g, b, pixelNuevo;

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++){
                pixel = original.getRGB(x,y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // Filtro Brillo
                r = Math.min(255, r+brillo);
                g = Math.min(255, g+brillo);
                b = Math.min(255, b+brillo);
                pixelNuevo = (r << 16) | (g << 8) | (b);

                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    public static BufferedImage aplicarHSV(BufferedImage original, float factorSaturation, float factorValue) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pixel, r, g, b, pixelNuevo, a;
        float h,s,v;

        for(int x=0; x<width; x++) {
            for(int y=0; y<height; y++){
                pixel = original.getRGB(x,y);

                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // Filtro HSV significa Hue (Tono), Saturation (Saturación) y Value (Valor/Brillo).
                // En Java, a esto se le conoce como HSB (Brightness en lugar de Value), pero es exactamente lo mismo.
                float[] hsb = Color.RGBtoHSB(r,g,b,null);
                h = hsb[0];
                s = hsb[1];
                v = hsb[2];

                s = Math.min(1, s*factorSaturation);
                v = Math.min(1, v*factorValue);

                pixelNuevo = Color.HSBtoRGB(h,s,v);
                pixelNuevo = (a << 24) | (Color.HSBtoRGB(h,s,v) & 0x00FFFFFF);
                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    public static BufferedImage aplicarTransparencia(BufferedImage original, float factorAlfa) {
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

                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

}
