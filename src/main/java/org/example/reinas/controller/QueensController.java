package org.example.reinas.controller;

import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Controller MVC para el juego de N-Reinas con interfaz gráfica.
 */
public class QueensController {
    private QueensModel model;
    private QueensView view;
    private List<int[]> solutions;
    private int currentIndex;

    public QueensController(QueensModel model, QueensView view) {
        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        view.addSolveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = view.getN();
                model = new QueensModel(n);
                solutions = model.solve();
                currentIndex = 0;
                if (!solutions.isEmpty()) {
                    view.showSolution(solutions.get(0), 0, solutions.size());
                }
            }
        });
        view.addPrevListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    view.showSolution(solutions.get(currentIndex), currentIndex, solutions.size());
                }
            }
        });
        view.addNextListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < solutions.size() - 1) {
                    currentIndex++;
                    view.showSolution(solutions.get(currentIndex), currentIndex, solutions.size());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QueensView qv = new QueensView();
            new QueensController(new QueensModel(8), qv);
            JFrame frame = new JFrame("N-Reinas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(qv);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}