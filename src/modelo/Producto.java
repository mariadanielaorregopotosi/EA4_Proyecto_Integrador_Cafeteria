
package modelo;

// Clase base para los productos de la cafeteria.
public abstract class Producto {

    protected String nombre;
    protected double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    // Metodo que implementaran las clases hijas.
    public abstract String getTipo();

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}

