
package persistencia;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class ArchivoCSV {

    private static final Path RUTA = Path.of("datos.csv");

    public static void guardar(
            String cliente,
            String producto,
            String tipo,
            double monto,
            double descuento,
            double total) throws IOException {

        boolean nuevoArchivo = !Files.exists(RUTA)
                || Files.size(RUTA) == 0;

        try (BufferedWriter escritor = Files.newBufferedWriter(
                RUTA,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            if (nuevoArchivo) {
                escritor.write(
                    "Cliente;Producto;Tipo;Monto;Descuento;Total"
                );
                escritor.newLine();
            }

            escritor.write(
                limpiar(cliente) + ";" +
                limpiar(producto) + ";" +
                limpiar(tipo) + ";" +
                String.format(Locale.US, "%.2f", monto) + ";" +
                String.format(Locale.US, "%.2f", descuento) + ";" +
                String.format(Locale.US, "%.2f", total)
            );

            escritor.newLine();
        }
    }

    private static String limpiar(String texto) {
        return texto.replace(";", ",")
                    .replace("\n", " ")
                    .replace("\r", " ");
    }
}
