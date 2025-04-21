package org.example.disco.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Stack;

/**
 * Vista interactiva de Torres de Hanoi.
 * Permite seleccionar torre origen y destino con el ratón.
 */
public class HanoiView extends JPanel implements MouseListener {
    private List<Stack<Integer>> pegs;
    private int selectedPeg = -1;
    private int numDisks;
    private static final Color[] COLORS = {
            new Color(0xF44336), new Color(0xE91E63), new Color(0x9C27B0),
            new Color(0x3F51B5), new Color(0x2196F3), new Color(0x03A9F4),
            new Color(0x009688), new Color(0x4CAF50), new Color(0xFFEB3B),
            new Color(0xFF9800)
    };

    public HanoiView(int n) {
        this.numDisks = n;
        addMouseListener(this);
    }

    public void setPegs(List<Stack<Integer>> pegs) {
        this.pegs = pegs;
        this.numDisks = pegs.get(0).size() + pegs.get(1).size() + pegs.get(2).size();
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
        int pegWidth = 10;
        int diskHeight = 20;

        // dibujar base
        g2.setColor(new Color(0x795548));
        g2.fillRect(0, baseY, w, 10);

        // dibujar palos y discos
        for (int i = 0; i < 3; i++) {
            int x = pegX[i];
            g2.setColor(new Color(0x5D4037));
            g2.fillRect(x-pegWidth/2, baseY - numDisks*diskHeight - 20, pegWidth, numDisks*diskHeight + 20);

            Stack<Integer> stack = pegs.get(i);
            for (int j = 0; j < stack.size(); j++) {
                int d = stack.get(j);
                int diskW = d * spacing / (numDisks + 1);
                int dx = x - diskW/2;
                int dy = baseY - diskHeight*(j+1);
                Color color = COLORS[(d-1) % COLORS.length];
                g2.setColor(color);
                g2.fillRoundRect(dx, dy, diskW, diskHeight, 8, 8);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(dx, dy, diskW, diskHeight, 8, 8);
            }

            // resaltar
            if (i == selectedPeg) {
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x-spacing/2, baseY - numDisks*diskHeight - 20, spacing, numDisks*diskHeight + 20);
                g2.setStroke(new BasicStroke(1));
            }
        }
    }

    private int pegAt(int x) {
        int spacing = getWidth()/4;
        for (int i = 0; i < 3; i++) {
            if (Math.abs(x - spacing*(i+1)) < spacing/2) return i;
        }
        return -1;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int peg = pegAt(e.getX());
        if (peg < 0 || pegs == null) return;
        if (selectedPeg < 0) {
            if (!pegs.get(peg).isEmpty()) selectedPeg = peg;
        } else {
            if (peg != selectedPeg) {
                fireMoveRequest(selectedPeg, peg);
            }
            selectedPeg = -1;
        }
        repaint();
    }

    // eventos no usados
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /**
     * Listener para eventos de solicitud de movimiento.
     */
    public interface MoveListener {
        void onMoveRequested(int from, int to);
    }

    private MoveListener moveListener;
    public void setMoveListener(MoveListener ml) {
        this.moveListener = ml;
    }
    private void fireMoveRequest(int from, int to) {
        if (moveListener != null) moveListener.onMoveRequested(from, to);
    }
}
