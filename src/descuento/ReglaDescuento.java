package descuento;

@FunctionalInterface
public interface ReglaDescuento {

    // Calcula el valor del descuento.
    double calcularDescuento(double total);
}
