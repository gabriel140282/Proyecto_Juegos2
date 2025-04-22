package org.example.disco.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Stack;

public class HanoiView extends JPanel implements MouseListener, MouseMotionListener {
    private List<Stack<Integer>> pegs;
    private int numero_discos;
    private MoveListener moveListener;

    // Estado de arrastre
    private boolean dragging;
    private int dragDiskSize;
    private int dragOrigPeg;
    private int dragX, dragY;

    private static final Color[] COLORS = {
            new Color(0xF44336), new Color(0xE91E63), new Color(0x9C27B0),
            new Color(0x3F51B5), new Color(0x2196F3), new Color(0x03A9F4),
            new Color(0x009688), new Color(0x4CAF50), new Color(0xFFEB3B),
            new Color(0xFF9800)
    };

    public HanoiView(int n) {
        this.numero_discos = n;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setPegs(List<Stack<Integer>> pegs) {
        this.pegs = pegs;
        // recalcula numDisks para el tamaño del panel
        this.numero_discos = pegs.get(0).size() + pegs.get(1).size() + pegs.get(2).size();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (pegs == null) return;
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth(), h = getHeight();
        int baseY = h - 40;
        int spacing = w / 4;
        int pegX[] = {spacing, 2*spacing, 3*spacing};
        int diskH = 20;

        // dibujar base
        g2.setColor(new Color(0x795548));
        g2.fillRect(0, baseY, w, 10);

        // palos y discos
        for (int i = 0; i < 3; i++) {
            int x = pegX[i];
            g2.setColor(new Color(0x5D4037));
            g2.fillRect(x-5, baseY - numero_discos*diskH - 20, 10, numero_discos*diskH + 20);

            Stack<Integer> stack = pegs.get(i);
            for (int j = 0; j < stack.size(); j++) {
                int d = stack.get(j);
                int wDisk = d * spacing / (numero_discos + 1);
                int dx = x - wDisk/2;
                int dy = baseY - diskH*(j+1);
                Color c = COLORS[(d-1) % COLORS.length];
                g2.setColor(c);
                g2.fillRoundRect(dx, dy, wDisk, diskH, 8, 8);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(dx, dy, wDisk, diskH, 8, 8);
            }
        }

        // disco arrastrado si aplica
        if (dragging) {
            Graphics2D g3 = (Graphics2D) g;
            int spacing2 = w / 4;
            int wDisk = dragDiskSize * spacing2 / (numero_discos + 1);
            int dy = 20;
            int dx = dragX - wDisk/2;
            int dyPos = dragY - dy/2;
            Color c = COLORS[(dragDiskSize-1) % COLORS.length];
            g3.setColor(c);
            g3.fillRoundRect(dx, dyPos, wDisk, dy, 8, 8);
            g3.setColor(Color.BLACK);
            g3.drawRoundRect(dx, dyPos, wDisk, dy, 8, 8);
        }
    }

    private int pegAt(int x) {
        int spacing = getWidth()/4;
        for (int i = 0; i < 3; i++) {
            if (Math.abs(x - spacing*(i+1)) < spacing/2) return i;
        }
        return -1;
    }

    // MouseListener
    @Override public void mousePressed(MouseEvent e) {
        int peg = pegAt(e.getX());
        if (peg < 0 || pegs == null) return;
        Stack<Integer> stack = pegs.get(peg);
        if (!stack.isEmpty()) {
            dragging = true;
            dragOrigPeg = peg;
            dragDiskSize = stack.pop();
            dragX = e.getX();
            dragY = e.getY();
            repaint();
        }
    }
    @Override public void mouseReleased(MouseEvent e) {
        if (!dragging) return;
        dragging = false;
        int dest = pegAt(e.getX());
        if (moveListener != null && dest >= 0) {
            moveListener.onMoveRequested(dragOrigPeg, dest);
        }
    }
    // no usados
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // MouseMotionListener
    @Override public void mouseDragged(MouseEvent e) {
        if (!dragging) return;
        dragX = e.getX();
        dragY = e.getY();
        repaint();
    }
    @Override public void mouseMoved(MouseEvent e) {}

    // MoveListener
    public interface MoveListener {
        void onMoveRequested(int from, int to);
    }
    public void setMoveListener(MoveListener ml) {
        this.moveListener = ml;
    }
}
