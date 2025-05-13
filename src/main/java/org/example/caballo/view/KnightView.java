package org.example.caballo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KnightView extends JPanel {
    private JTextField dimField;
    private JButton startBtn;
    private JLabel statusLabel;
    private DrawPanel drawPanel;
    private JTextArea moveLogArea; // ✅ Área para registrar movimientos

    public KnightView() {
        setLayout(new BorderLayout());

        // Panel superior de controles
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Dimensión: "));
        dimField = new JTextField("27", 3);
        top.add(dimField);

        startBtn = new JButton("Iniciar Tour");
        top.add(startBtn);

        statusLabel = new JLabel(" ");
        top.add(statusLabel);

        add(top, BorderLayout.NORTH);

        // Panel central con tablero + registro lateral
        JPanel centerPanel = new JPanel(new BorderLayout());

        drawPanel = new DrawPanel();
        centerPanel.add(drawPanel, BorderLayout.CENTER);

        // ✅ Panel de registro a la derecha
        moveLogArea = new JTextArea(20, 20);
        moveLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLogArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Movimientos"));

        centerPanel.add(scrollPane, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void addStartListener(ActionListener l) {
        startBtn.addActionListener(l);
    }

    public int getDimension() {
        return Integer.parseInt(dimField.getText());
    }

    public void showTour(List<int[]> path) {
        drawPanel.setPath(path);
    }

    public void setStatus(String msg) {
        statusLabel.setText(msg);
    }

    // ✅ Método para añadir movimientos al registro
    private void logMove(int step, int row, int col) {
        moveLogArea.append("Paso " + step + ": (" + row + ", " + col + ")\n");
        moveLogArea.setCaretPosition(moveLogArea.getDocument().getLength());
    }

    private class DrawPanel extends JPanel {
        private List<int[]> path;
        private int step;
        private Timer timer;
        private Image horseImg;

        public DrawPanel() {
            try {
                horseImg = new ImageIcon(getClass().getResource("/images/caballo.png")).getImage();
            } catch (Exception e) {
                horseImg = null;
            }
        }

        public void setPath(List<int[]> p) {
            this.path = p;
            this.step = 0;
            moveLogArea.setText(""); // ✅ limpiar registro anterior
            revalidate();
            repaint();

            if (p == null || p.isEmpty()) {
                setStatus("No se encontró un tour.");
                return;
            }

            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(300, e -> {
                if (step < path.size()) {
                    int[] pos = path.get(step);
                    logMove(step + 1, pos[0], pos[1]); // ✅ log en movimiento
                }

                step++;
                if (step >= path.size()) {
                    timer.stop();
                    setStatus("Tour completo. Total de movimientos: " + path.size());
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (path == null) return;

            Graphics2D g2 = (Graphics2D) g;
            int d = (int) Math.sqrt(path.size());

            int cellWidth = getWidth() / d;
            int cellHeight = getHeight() / d;
            int cellSize = Math.min(cellWidth, cellHeight);

            int boardSize = cellSize * d;
            int offsetX = (getWidth() - boardSize) / 2;
            int offsetY = (getHeight() - boardSize) / 2;

            for (int r = 0; r < d; r++) {
                for (int c = 0; c < d; c++) {
                    g2.setColor((r + c) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
                    g2.fillRect(offsetX + c * cellSize, offsetY + r * cellSize, cellSize, cellSize);
                }
            }

            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            for (int i = 1; i <= step && i < path.size(); i++) {
                int[] a = path.get(i - 1);
                int[] b = path.get(i);
                g2.drawLine(
                        offsetX + a[1] * cellSize + cellSize / 2, offsetY + a[0] * cellSize + cellSize / 2,
                        offsetX + b[1] * cellSize + cellSize / 2, offsetY + b[0] * cellSize + cellSize / 2
                );
            }

            if (step < path.size()) {
                int[] p = path.get(step);
                int x = offsetX + p[1] * cellSize;
                int y = offsetY + p[0] * cellSize;
                if (horseImg != null) {
                    g2.drawImage(horseImg, x, y, cellSize, cellSize, this);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillOval(x + 4, y + 4, cellSize - 8, cellSize - 8);
                }
            }
        }
    }
}
