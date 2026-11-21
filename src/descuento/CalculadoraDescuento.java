package descuento;

public class CalculadoraDescuento {

    private final ReglaDescuento regla;

    public CalculadoraDescuento(ReglaDescuento regla) {
        this.regla = regla;
    }

    public double calcular(double total) {
        if (total <= 0) {
            throw new IllegalArgumentException(
                "El valor del pedido debe ser mayor que cero."
            );
        }

        return regla.calcularDescuento(total);
    }
}
