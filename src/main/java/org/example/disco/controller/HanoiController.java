package org.example.disco.controller;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.example.disco.model.HanoiModel;
import org.example.disco.view.HanoiView;
import org.example.unificado.db.DBManager;
import java.sql.SQLException;

public class HanoiController {
    private HanoiModel modelo;
    private HanoiView vista;
    private int nivel;
    private JTextField campo_nivel;
    private JButton boton_reset, boton_resolver;
    private DBManager db;

    public HanoiController(JPanel contenedor) {
        nivel = 1;
        modelo = new HanoiModel(nivel);
        vista  = new HanoiView(nivel);

        try {
            db = new DBManager(); // conexión a base de datos
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(contenedor, "Error al conectar a la base de datos.");
        }

        contenedor.setLayout(new BorderLayout());
        contenedor.add(vista, BorderLayout.CENTER);

        // Panel de controles
        JPanel top = new JPanel();
        top.add(new JLabel("Nivel:"));
        campo_nivel = new JTextField(String.valueOf(nivel), 3);
        campo_nivel.setEditable(true);
        top.add(campo_nivel);

        boton_reset = new JButton("Reset o Jugar");
        boton_resolver = new JButton("Resolver");
        top.add(boton_reset);
        top.add(boton_resolver);
        contenedor.add(top, BorderLayout.NORTH);

        // Movimiento manual
        vista.setMoveListener((from, to) -> {
            if (modelo.moveDisk(from, to)) {
                vista.setPegs(modelo.getPegs());
                vista.logMovimiento("Movido manualmente de " + from + " a " + to);
                if (modelo.getPegs().get(2).size() == nivel) {
                    JOptionPane.showMessageDialog(contenedor,
                            "¡Nivel " + nivel + " completado! Avanzando al siguiente.");
                    nivel++;
                    campo_nivel.setText(String.valueOf(nivel));
                    modelo.reset(nivel);
                    vista.setPegs(modelo.getPegs());
                    vista.logMovimiento("Reiniciado a nivel " + nivel);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(contenedor,
                        "No se puede colocar un disco grande encima de uno pequeño.",
                        "Movimiento inválido",
                        JOptionPane.WARNING_MESSAGE);
                vista.setPegs(modelo.getPegs());
            }
        });

        // Reset manual
        boton_reset.addActionListener(e -> {
            try {
                nivel = Integer.parseInt(campo_nivel.getText());
                modelo.reset(nivel);
                vista.setPegs(modelo.getPegs());
                vista.logMovimiento("Tablero reiniciado con nivel " + nivel);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(contenedor, "Nivel inválido.");
            }
        });

        // Resolver automáticamente
        boton_resolver.addActionListener(e -> {
            try {
                nivel = Integer.parseInt(campo_nivel.getText());
                modelo.reset(nivel);
                vista.setPegs(modelo.getPegs());
                vista.logMovimiento("Resolviendo automáticamente el nivel " + nivel + "...");
                solveHanoiAnimated(nivel, 0, 2, 1);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(contenedor, "Nivel inválido.");
            }
        });
    }

    /**
     * Resuelve el juego paso a paso visualmente.
     */
    private void solveHanoiAnimated(int n, int from, int to, int aux) {
        List<int[]> moves = new ArrayList<>();
        generateMoves(n, from, to, aux, moves);

        Timer timer = new Timer(500, null);
        Iterator<int[]> it = moves.iterator();

        timer.addActionListener(e -> {
            if (it.hasNext()) {
                int[] move = it.next();
                modelo.moveDisk(move[0], move[1]);
                vista.setPegs(modelo.getPegs());
                vista.logMovimiento("Automático: mover de " + move[0] + " a " + move[1]);
            } else {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(vista, "¡Solución completada!");
                vista.logMovimiento("✅ Solución finalizada.");

                // Guardar resultado en la base de datos
                try {
                    if (db != null) {
                        db.resultado_hanoi_guardado(n, moves.size());
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(vista, "Error al guardar el resultado en la base de datos.");
                }
            }
        });
        timer.start();
    }

    private void generateMoves(int n, int from, int to, int aux, List<int[]> moves) {
        if (n == 1) {
            moves.add(new int[]{from, to});
        } else {
            generateMoves(n - 1, from, aux, to, moves);
            moves.add(new int[]{from, to});
            generateMoves(n - 1, aux, to, from, moves);
        }
    }
}
