
import javax.swing.SwingUtilities;
import vista.VentanaCafeteria;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaCafeteria ventana =
                    new VentanaCafeteria();

            ventana.setVisible(true);
        });
    }
}

