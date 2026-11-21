package modelo;

// Bebida hereda de Producto.
public class Bebida extends Producto {

    private String tamano;

    public Bebida(String nombre, double precio, String tamano) {
        super(nombre, precio);
        this.tamano = tamano;
    }

    public String getTamano() {
        return tamano;
    }

    @Override
    public String getTipo() {
        return "Bebida";
    }
}
