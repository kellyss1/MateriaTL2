package org.uce;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FiltrosGraficos {

    static void main() throws IOException {
        File file = new File("images/paisaje.jpg");
        BufferedImage original = ImageIO.read(file);

        File filtroBits = new File("images/escalarBits.jpg");
        filtroEstirarBits(original);

    }

    public static void filtroEstirarBits(BufferedImage original) throws IOException {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        // Lienzo para pintar el resultado final
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int a, r, g, b, pixel, pixelNuevo;

        // 1. Configuramos nuestra máscara y el valor máximo para la regla de 3
        // Vamos a rescatar los 3 bits de más a la derecha (00000111)
        int mascaraRecorteBit = 0x07; // Puede ser 7 o 0b00000111
        int valorMaximo = 7;          // El número más grande posible con 3 bits

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                // Obtenemos el píxel original
                pixel = original.getRGB(x, y);

                // Desempaquetamos los 4 canales
                a = (pixel >> 24) & 0xFF;
                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = (pixel) & 0xFF;

                // 2. RECORTAR: Aplicamos el operador AND bit a bit (&)
                // Esto destruye los detalles brillantes de la izquierda y deja solo lo oculto
                int rRecortado = r & mascaraRecorteBit;
                int gRecortado = g & mascaraRecorteBit;
                int bRecortado = b & mascaraRecorteBit;

                // 3. ESTIRAR: Aplicamos la Regla de 3 para hacer visibles esos bits
                int rEstirado = (rRecortado * 255) / valorMaximo;
                int gEstirado = (gRecortado * 255) / valorMaximo;
                int bEstirado = (bRecortado * 255) / valorMaximo;

                // Aseguramos que no se pase de 255 por si acaso, y sobrescribimos
                r = Math.min(255, rEstirado);
                g = Math.min(255, gEstirado);
                b = Math.min(255, bEstirado);

                // 4. Volvemos a empaquetar el píxel completo con el pegamento OR (|)
                pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }

        // Guardamos el resultado en la carpeta images
        File nuevaImg = new File("images/bits-estirados.png");
        ImageIO.write(nuevoBuffer, "png", nuevaImg);

        System.out.println("¡Filtro de extracción de bits completado!");
    }
}
