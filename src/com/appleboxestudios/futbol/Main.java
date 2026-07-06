package com.appleboxestudios.futbol;

import com.appleboxestudios.futbol.service.JugadorService;
import com.appleboxestudios.futbol.ui.VentanaPrincipal;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 * Inicializa el servicio y la ventana principal en el hilo de Swing (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Establecer Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el L&F por defecto
            System.err.println("No se pudo establecer el Look and Feel del sistema: " + e.getMessage());
        }

        // Ejecutar la UI en el Event Dispatch Thread (buena práctica de Swing)
        SwingUtilities.invokeLater(() -> {
            JugadorService servicio = new JugadorService();
            VentanaPrincipal ventana = new VentanaPrincipal(servicio);
            ventana.setVisible(true);
        });
    }
}
