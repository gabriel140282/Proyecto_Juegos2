package org.example.reinas.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista para mostrar soluciones de N-Reinas.
 */
public class QueensView extends JPanel {
    private JTextField nField;
    private JButton solveBtn;
    private JTextArea area;

    public QueensView() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(new JLabel("N:"));
        nField = new JTextField("8",3);
        solveBtn = new JButton("Resolver");
        top.add(nField);
        top.add(solveBtn);
        add(top, BorderLayout.NORTH);

        area = new JTextArea(); area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public int getN() { return Integer.parseInt(nField.getText()); }
    public void addSolveListener(ActionListener l) { solveBtn.addActionListener(l); }

    public void display(List<int[]> sols) {
        area.setText("");
        int count=0;
        for (int[] sol:sols) {
            count++;
            area.append("Sol " + count + ": ");
            for (int col: sol) area.append(col+" ");
            area.append("\n");
        }
        area.append("Total soluciones: " + sols.size());
    }
}
