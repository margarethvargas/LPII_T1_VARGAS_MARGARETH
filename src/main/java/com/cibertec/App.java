package com.cibertec;

import com.cibertec.peliculas.ui.AlquilerUI;
import com.cibertec.peliculas.util.DataInitializer;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Inicializando base de datos...");
        DataInitializer.main(args);

        // Iniciar la interfaz gráfica
        System.out.println("Iniciando aplicación...");
        SwingUtilities.invokeLater(() -> {
            AlquilerUI alquilerUI = new AlquilerUI();
            alquilerUI.setVisible(true);
        });
    }
}