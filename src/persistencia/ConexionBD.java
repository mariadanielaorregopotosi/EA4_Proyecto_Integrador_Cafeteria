
package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:cafeteria.db";

    public static String probarConexion() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            return "ERROR: No se encontro el controlador SQLite JDBC.";
        }

        String sql = """
            CREATE TABLE IF NOT EXISTS pedidos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente TEXT NOT NULL,
                producto TEXT NOT NULL,
                tipo TEXT NOT NULL,
                monto REAL NOT NULL,
                descuento REAL NOT NULL,
                total_pagar REAL NOT NULL,
                fecha TEXT NOT NULL
            )
            """;

        try (Connection conexion =
                 DriverManager.getConnection(URL);
             Statement sentencia = conexion.createStatement()) {

            sentencia.execute(sql);

            return "Conexion SQLite exitosa. Tabla pedidos disponible.";

        } catch (SQLException e) {
            return "ERROR de conexion SQLite: " + e.getMessage();
        }
    }

    public static String guardarPedido(
            String cliente,
            String producto,
            String tipo,
            double monto,
            double descuento,
            double totalPagar) {

        String sql = """
            INSERT INTO pedidos
            (cliente, producto, tipo, monto,
             descuento, total_pagar, fecha)
            VALUES (?, ?, ?, ?, ?, ?, datetime('now','localtime'))
            """;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            return "No se pudo guardar: falta el controlador JDBC.";
        }

        try (Connection conexion =
                 DriverManager.getConnection(URL);
             PreparedStatement sentencia =
                 conexion.prepareStatement(sql)) {

            sentencia.setString(1, cliente);
            sentencia.setString(2, producto);
            sentencia.setString(3, tipo);
            sentencia.setDouble(4, monto);
            sentencia.setDouble(5, descuento);
            sentencia.setDouble(6, totalPagar);

            sentencia.executeUpdate();

            return "Pedido guardado correctamente en SQLite.";

        } catch (SQLException e) {
            return "Error al guardar el pedido: " + e.getMessage();
        }
    }
}
