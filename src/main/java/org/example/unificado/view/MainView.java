package org.example.unificado.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JTabbedPane pestanas;

    public MainView() {
        setTitle("Juegos Clásicos MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setLocationRelativeTo(null);
        pestanas = new JTabbedPane();
        setLayout(new BorderLayout());
        add(pestanas, BorderLayout.CENTER);
    }

    public void addTab(String title, JPanel panel) {
        pestanas.addTab(title, panel);
    }
}
