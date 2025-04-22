package org.example.caballo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KnightView extends JPanel {
    private JTextField dimField;
    private JButton startBtn;
    private JLabel statusLabel;        // NUEVO: etiqueta para mostrar el estado
    private DrawPanel drawPanel;

    public KnightView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Dimensión: "));
        dimField = new JTextField("27", 3);
        top.add(dimField);

        startBtn = new JButton("Iniciar Tour");
        top.add(startBtn);

        statusLabel = new JLabel(" ");   // NUEVO: etiqueta de estado vacía inicialmente
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
        statusLabel.setText(msg);  // NUEVO: método para actualizar el mensaje
    }

    // Panel interno para dibujar el tour
    private class DrawPanel extends JPanel {
        private List<int[]> path;
        private int step;
        private Timer timer;
        private static final int CELL = 20;
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

            if (p == null || p.isEmpty()) {
                setStatus("No se encontró un tour.");
                repaint();
                return;
            }

            int size = p.get(0)[0] > p.get(0)[1] ? p.get(0)[0] : p.get(0)[1];
            size = Math.max(size, 10);
            setPreferredSize(new Dimension(CELL * size, CELL * size));
            revalidate();

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
            for (int r = 0; r < d; r++)
                for (int c = 0; c < d; c++) {
                    g2.setColor((r + c) % 2 == 0 ? Color.LIGHT_GRAY : Color.GRAY);
                    g2.fillRect(c * CELL, r * CELL, CELL, CELL);
                }

            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            for (int i = 1; i <= step && i < path.size(); i++) {
                int[] a = path.get(i - 1), b = path.get(i);
                g2.drawLine(a[1] * CELL + CELL / 2, a[0] * CELL + CELL / 2,
                        b[1] * CELL + CELL / 2, b[0] * CELL + CELL / 2);
            }

            if (step < path.size()) {
                int[] p = path.get(step);
                int x = p[1] * CELL;
                int y = p[0] * CELL;
                if (horseImg != null) {
                    g2.drawImage(horseImg, x, y, CELL, CELL, this);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillOval(x + 4, y + 4, CELL - 8, CELL - 8);
                }
            }
        }
    }
}
