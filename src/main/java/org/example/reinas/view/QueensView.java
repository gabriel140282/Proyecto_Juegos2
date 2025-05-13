package org.example.reinas.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class QueensView extends JPanel {
    private JTextField nField;
    private JButton boton_resolver;
    private JButton boton_anterior;
    private JButton boton_siguiente;
    private BoardPanel boardPanel;
    private JTextArea logArea; // ✅ Registro lateral

    public QueensView() {
        setLayout(new BorderLayout());

        // Panel superior
        JPanel top = new JPanel();
        top.add(new JLabel("N:"));
        nField = new JTextField("8", 3);
        boton_resolver = new JButton("Resolver");
        boton_anterior = new JButton("< Anterior");
        boton_siguiente = new JButton("Siguiente >");
        boton_anterior.setEnabled(false);
        boton_siguiente.setEnabled(false);
        top.add(nField);
        top.add(boton_resolver);
        top.add(boton_anterior);
        top.add(boton_siguiente);
        add(top, BorderLayout.NORTH);

        // Panel del tablero
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // ✅ Registro lateral derecho
        logArea = new JTextArea(20, 20);
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro de soluciones"));
        add(scroll, BorderLayout.EAST);
    }

    public int getN() {
        try {
            return Integer.parseInt(nField.getText());
        } catch (NumberFormatException e) {
            return 8;
        }
    }

    public void addSolveListener(ActionListener l) {
        boton_resolver.addActionListener(l);
    }

    public void addPrevListener(ActionListener l) {
        boton_anterior.addActionListener(l);
    }

    public void addNextListener(ActionListener l) {
        boton_siguiente.addActionListener(l);
    }

    public void showSolution(int[] solution, int index, int total) {
        boardPanel.setSolution(solution);
        boton_anterior.setEnabled(index > 0);
        boton_siguiente.setEnabled(index < total - 1);
    }

    // ✅ Método para añadir entradas al registro
    public void logSolucion(String texto) {
        logArea.append(texto + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private static class BoardPanel extends JPanel {
        private int[] solution;
        private int n;
        private Image queenImg;

        public BoardPanel() {
            try {
                queenImg = new ImageIcon(getClass().getResource("/images/reina.png")).getImage();
            } catch (Exception e) {
                queenImg = null;
            }
        }

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

            // Dibujar tablero
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    boolean light = (r + c) % 2 == 0;
                    g2.setColor(light ? Color.WHITE : Color.LIGHT_GRAY);
                    g2.fillRect(c * cell, r * cell, cell, cell);
                }
            }

            // Dibujar reinas
            for (int r = 0; r < n; r++) {
                int c = solution[r];
                int x = c * cell;
                int y = r * cell;
                if (queenImg != null) {
                    g2.drawImage(queenImg, x, y, cell, cell, this);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillOval(x + cell / 8, y + cell / 8, cell * 3 / 4, cell * 3 / 4);
                }
            }
        }
    }
}
