package org.example.caballo.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KnightView extends JPanel {
    private JTextField dimField;
    private JButton startBtn;
    private DrawPanel drawPanel;

    public KnightView() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(new JLabel("Dimensión: ")); dimField = new JTextField("27",3);
        startBtn = new JButton("Iniciar Tour");
        top.add(dimField);
        top.add(startBtn);
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

    // Panel interno para dibujar el tour
    private static class DrawPanel extends JPanel {
        private List<int[]> path;
        private int step;
        private Timer timer;
        private static final int CELL=20;

        public void setPath(List<int[]> p) {
            this.path = p;
            this.step = 0;
            int size = p.size()>0 ? p.get(0)[0] : 10;
            setPreferredSize(new Dimension(CELL*size, CELL*size));
            revalidate();
            if (timer!=null && timer.isRunning()) timer.stop();
            timer = new Timer(100, e -> {
                step++;
                if (step>=path.size()) timer.stop();
                repaint();
            });
            timer.start();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (path==null) return;
            Graphics2D g2=(Graphics2D)g;
            int d = (int)Math.sqrt(path.size());
            for (int r=0;r<d;r++) for (int c=0;c<d;c++) {
                g2.setColor((r+c)%2==0?Color.LIGHT_GRAY:Color.GRAY);
                g2.fillRect(c*CELL,r*CELL,CELL,CELL);
            }
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            for (int i=1;i<=step && i<path.size();i++) {
                int[] a=path.get(i-1), b=path.get(i);
                g2.drawLine(a[1]*CELL+CELL/2,a[0]*CELL+CELL/2,
                        b[1]*CELL+CELL/2,b[0]*CELL+CELL/2);
            }
            if (step<path.size()) {
                int[] p=path.get(step);
                g2.setColor(Color.RED);
                g2.fillOval(p[1]*CELL+4,p[0]*CELL+4,CELL-8,CELL-8);
            }
        }
    }
}