package modelo;

// Comida hereda de Producto.
public class Comida extends Producto {

    private boolean vegetariana;

    public Comida(String nombre, double precio, boolean vegetariana) {
        super(nombre, precio);
        this.vegetariana = vegetariana;
    }

    public boolean isVegetariana() {
        return vegetariana;
    }

    @Override
    public String getTipo() {
        return "Comida";
    }
}

