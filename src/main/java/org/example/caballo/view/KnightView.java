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

    public KnightView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Dimensión: "));
        dimField = new JTextField("27", 3);
        top.add(dimField);

        startBtn = new JButton("Iniciar Tour");
        top.add(startBtn);

        statusLabel = new JLabel(" ");
        top.add(statusLabel);

        add(top, BorderLayout.NORTH);

        drawPanel = new DrawPanel();
        add(new JScrollPane(drawPanel), BorderLayout.CENTER);
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
            revalidate();
            repaint();

            if (p == null || p.isEmpty()) {
                setStatus("No se encontró un tour.");
                return;
            }

            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(300, e -> {
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

            // Dibujar tablero
            for (int r = 0; r < d; r++) {
                for (int c = 0; c < d; c++) {
                    g2.setColor((r + c) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
                    g2.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }

            // Dibujar el camino
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            for (int i = 1; i <= step && i < path.size(); i++) {
                int[] a = path.get(i - 1);
                int[] b = path.get(i);
                g2.drawLine(
                        a[1] * cellSize + cellSize / 2, a[0] * cellSize + cellSize / 2,
                        b[1] * cellSize + cellSize / 2, b[0] * cellSize + cellSize / 2
                );
            }

            // Dibujar el caballo
            if (step < path.size()) {
                int[] p = path.get(step);
                int x = p[1] * cellSize;
                int y = p[0] * cellSize;
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
