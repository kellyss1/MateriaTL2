package org.uce;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FiltrosClase {

    public static void main(String[] args) throws IOException {

        File file = new File("images/paisaje.jpg");
        BufferedImage original = ImageIO.read(file);

        File filtro = new File("images/filtro.jpg");
        ImageIO.write(aplicarColorAzul(original),"jpg", filtro);

    }

    public static BufferedImage aplicarColorAzul(BufferedImage original) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        int r, g, b, pixel, pixelNuevo;

        for(int y=0; y < alto; y++) {
            for(int x=0; x < ancho; x++) {
                // 1. Extraer el píxel
                pixel = original.getRGB(x,y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // 2. Calcular el brillo (promedio) para mantener luces y sombras
                int brillo = (r + g + b) / 3;

                // 3. Aplicar el color (Azul). Apagamos rojo y verde.
                int nuevoR = 0;
                int nuevoG = 0;
                int nuevoB = brillo; // El azul toma la intensidad original de la imagen

//                r = Math.max(0, r-100);
//                g = Math.max(0, g +100);
//                b = Math.min(255, b -100);



                // 4. Volver a empaquetar empujando los bits a la izquierda (<<)
                // Se suma (o se usa el operador lógico OR |) cada canal en su posición
                pixelNuevo = (nuevoR << 16) | (nuevoG << 8) | nuevoB;

                nuevoBuffer.setRGB(x,y,pixelNuevo);
            }
        }

        return nuevoBuffer;
    }
}