package org.example.unificado.view;

import javax.swing.*;
import java.awt.*;

/**
 * Vista principal con pestañas para cada juego.
 */
public class MainView extends JFrame {
    private JTabbedPane tabs;

    public MainView() {
        setTitle("Juegos Clásicos MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
        tabs = new JTabbedPane();
        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    public void addTab(String title, JPanel panel) {
        tabs.addTab(title, panel);
    }
}
