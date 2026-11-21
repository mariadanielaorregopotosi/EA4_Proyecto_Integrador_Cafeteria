
package vista;

import modelo.Producto;
import modelo.Bebida;
import modelo.Comida;
import descuento.ReglaDescuento;
import descuento.CalculadoraDescuento;
import persistencia.ConexionBD;
import persistencia.ArchivoCSV;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class VentanaCafeteria extends JFrame {

    private JTextField campoCliente;
    private JTextField campoProducto;
    private JTextField campoMonto;

    private JComboBox<String> tipoProducto;

    private JButton botonCalcular;

    private JTextArea areaResultado;

    public VentanaCafeteria() {

        setTitle("Cafeteria Digital - Gestion de Descuentos");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                20, 20, 20, 20));

        JLabel titulo = new JLabel(
                "SISTEMA DE DESCUENTOS - CAFETERIA",
                SwingConstants.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 19));

        JPanel formulario = new JPanel(new GridLayout(5, 2, 10, 10));

        campoCliente = new JTextField();
        campoProducto = new JTextField();
        campoMonto = new JTextField();

        tipoProducto = new JComboBox<>(
                new String[]{"Bebida", "Comida"});

        botonCalcular = new JButton("Calcular Descuento");

        formulario.add(new JLabel("Nombre del cliente:"));
        formulario.add(campoCliente);

        formulario.add(new JLabel("Nombre del producto:"));
        formulario.add(campoProducto);

        formulario.add(new JLabel("Tipo de producto:"));
        formulario.add(tipoProducto);

        formulario.add(new JLabel("Monto del pedido ($):"));
        formulario.add(campoMonto);

        formulario.add(new JLabel(""));
        formulario.add(botonCalcular);

        areaResultado = new JTextArea(10, 40);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane desplazamiento =
                new JScrollPane(areaResultado);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
        panel.add(desplazamiento, BorderLayout.SOUTH);

        add(panel);

        // Intentar la conexion a la base de datos al iniciar.
        areaResultado.append(
                "ESTADO DE LA BASE DE DATOS:\n"
                + ConexionBD.probarConexion()
                + "\n\n");

        // Evento del boton.
        botonCalcular.addActionListener(e -> calcularPedido());
    }

    private void calcularPedido() {

        try {

            String cliente = campoCliente.getText().trim();
            String nombre = campoProducto.getText().trim();
            String tipo = (String) tipoProducto.getSelectedItem();

            if (cliente.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar el cliente y el producto.",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double monto = Double.parseDouble(
                    campoMonto.getText().trim().replace(",", "."));

            if (!Double.isFinite(monto) || monto <= 0) {
                throw new IllegalArgumentException(
                        "El monto debe ser positivo y valido.");
            }

            // Aplicacion de herencia y polimorfismo.
            Producto producto;

            if ("Bebida".equals(tipo)) {
                producto = new Bebida(nombre, monto, "Mediano");
            } else {
                producto = new Comida(nombre, monto, false);
            }

            // Expresion lambda para calcular el descuento.
            ReglaDescuento regla = total ->
                    total > 10000 ? total * 0.15 : 0;

            CalculadoraDescuento calculadora =
                    new CalculadoraDescuento(regla);

            double descuento = calculadora.calcular(
                    producto.getPrecio());

            double totalPagar = monto - descuento;

            // Guardar en la base de datos.
            String mensajeBD = ConexionBD.guardarPedido(
                    cliente,
                    producto.getNombre(),
                    producto.getTipo(),
                    monto,
                    descuento,
                    totalPagar);

            // Guardar en CSV.
            String mensajeCSV;

            try {
                ArchivoCSV.guardar(
                        cliente,
                        producto.getNombre(),
                        producto.getTipo(),
                        monto,
                        descuento,
                        totalPagar);

                mensajeCSV = "Pedido registrado en datos.csv.";

            } catch (IOException ex) {
                mensajeCSV = "Error al guardar CSV: "
                        + ex.getMessage();
            }

            // Mostrar resultados.
            areaResultado.append(
                    "----------------------------------\n"
                    + "CLIENTE: " + cliente + "\n"
                    + "PRODUCTO: " + producto.getNombre() + "\n"
                    + "TIPO: " + producto.getTipo() + "\n"
                    + "MONTO: $" + String.format("%.2f", monto) + "\n"
                    + "DESCUENTO: $" + String.format("%.2f", descuento) + "\n"
                    + "TOTAL A PAGAR: $" + String.format("%.2f", totalPagar) + "\n"
                    + mensajeBD + "\n"
                    + mensajeCSV + "\n");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un monto numerico valido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error en los datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
