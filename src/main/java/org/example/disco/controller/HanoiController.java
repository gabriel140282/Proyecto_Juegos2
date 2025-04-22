package org.example.disco.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


import org.example.disco.model.HanoiModel;
import org.example.disco.view.HanoiView;

public class HanoiController {
    private HanoiModel model;
    private HanoiView view;
    private int level;
    private JTextField levelField;
    private JButton resetBtn, resolverBtn;

    public HanoiController(JPanel container) {
        level = 1;
        model = new HanoiModel(level);
        view  = new HanoiView(level);

        container.setLayout(new BorderLayout());
        container.add(view, BorderLayout.CENTER);

        // Panel de controles
        JPanel top = new JPanel();
        top.add(new JLabel("Nivel:"));
        levelField = new JTextField(String.valueOf(level), 3);
        levelField.setEditable(true); // Ahora editable
        top.add(levelField);

        resetBtn = new JButton("Reset");
        resolverBtn = new JButton("Resolver");
        top.add(resetBtn);
        top.add(resolverBtn);
        container.add(top, BorderLayout.NORTH);

        // Movimiento manual
        view.setMoveListener((from, to) -> {
            if (model.moveDisk(from, to)) {
                view.setPegs(model.getPegs());
                if (model.getPegs().get(2).size() == level) {
                    JOptionPane.showMessageDialog(container,
                            "¡Nivel " + level + " completado! Avanzando al siguiente.");
                    level++;
                    levelField.setText(String.valueOf(level));
                    model.reset(level);
                    view.setPegs(model.getPegs());
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(container,
                        "No se puede colocar un disco grande encima de uno pequeño.",
                        "Movimiento inválido",
                        JOptionPane.WARNING_MESSAGE);
                view.setPegs(model.getPegs());
            }
        });

        // Reset manual
        resetBtn.addActionListener(e -> {
            try {
                level = Integer.parseInt(levelField.getText());
                model.reset(level);
                view.setPegs(model.getPegs());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(container, "Nivel inválido.");
            }
        });

        // Resolver automáticamente
        resolverBtn.addActionListener(e -> {
            try {
                level = Integer.parseInt(levelField.getText());
                model.reset(level);
                view.setPegs(model.getPegs());
                solveHanoiAnimated(level, 0, 2, 1); // origen, destino, auxiliar
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(container, "Nivel inválido.");
            }
        });
    }

    /**
     * Resuelve el juego paso a paso visualmente.
     */
    private void solveHanoiAnimated(int n, int from, int to, int aux) {
        // ✅ Se declara correctamente con tipo explícito
        List<int[]> moves = new ArrayList<int[]>();
        generateMoves(n, from, to, aux, moves);

        Timer timer = new Timer(500, null);
        Iterator<int[]> it = moves.iterator();

        timer.addActionListener(e -> {
            if (it.hasNext()) {
                int[] move = it.next();
                model.moveDisk(move[0], move[1]);
                view.setPegs(model.getPegs());
            } else {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(view, "¡Solución completada!");
            }
        });
        timer.start();
    }

    /**
     * Algoritmo recursivo que genera los movimientos necesarios para resolver Hanoi.
     */
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
