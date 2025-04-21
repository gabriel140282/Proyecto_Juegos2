package org.example.reinas.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Vista gráfica de N-Reinas con tablero interactivo.
 */
public class QueensView extends JPanel {
    private JTextField nField;
    private JButton solveBtn;
    private JButton prevBtn;
    private JButton nextBtn;
    private BoardPanel boardPanel;

    public QueensView() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(new JLabel("N:"));
        nField = new JTextField("8", 3);
        solveBtn = new JButton("Resolver");
        prevBtn = new JButton("< Anterior");
        nextBtn = new JButton("Siguiente >");
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        top.add(nField);
        top.add(solveBtn);
        top.add(prevBtn);
        top.add(nextBtn);
        add(top, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);
    }

    public int getN() {
        try {
            return Integer.parseInt(nField.getText());
        } catch (NumberFormatException e) {
            return 8;
        }
    }

    public void addSolveListener(ActionListener l) {
        solveBtn.addActionListener(l);
    }

    public void addPrevListener(ActionListener l) {
        prevBtn.addActionListener(l);
    }

    public void addNextListener(ActionListener l) {
        nextBtn.addActionListener(l);
    }

    public void showSolution(int[] solution, int index, int total) {
        boardPanel.setSolution(solution);
        prevBtn.setEnabled(index > 0);
        nextBtn.setEnabled(index < total - 1);
    }

    // Panel interno para dibujar el tablero y las reinas
    private static class BoardPanel extends JPanel {
        private int[] solution;
        private int n;

        public void setSolution(int[] solution) {
            this.solution = solution;
            this.n = solution.length;
            int size = Math.min(600, 50 * n);
            setPreferredSize(new Dimension(size, size));
            revalidate();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (solution == null) return;
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            int cell = Math.min(w, h) / n;
            // Dibujar celdas
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    boolean light = (r + c) % 2 == 0;
                    g2.setColor(light ? Color.WHITE : Color.LIGHT_GRAY);
                    g2.fillRect(c * cell, r * cell, cell, cell);
                }
            }
            // Dibujar reinas
            g2.setColor(Color.RED);
            for (int r = 0; r < n; r++) {
                int c = solution[r];
                int x = c * cell;
                int y = r * cell;
                g2.fillOval(x + cell/8, y + cell/8, cell * 3/4, cell * 3/4);
            }
        }
    }
}