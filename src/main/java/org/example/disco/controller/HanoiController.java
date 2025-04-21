package org.example.disco.controller;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import org.example.disco.model.HanoiModel;
import org.example.disco.view.HanoiView;

/**
 * Controller MVC para el juego de Torres de Hanoi con niveles crecientes.
 */
public class HanoiController {
    private HanoiModel model;
    private HanoiView view;
    private int level;
    private JTextField levelField;
    private JButton resetBtn;

    /**
     * Monta el juego en el panel contenedor y gestiona niveles.
     */
    public HanoiController(JPanel container) {
        // Nivel inicial
        level = 1;
        model = new HanoiModel(level);
        view = new HanoiView(level);

        // Layout principal
        container.setLayout(new BorderLayout());
        container.add(view, BorderLayout.CENTER);

        // Panel de controles (nivel + reset)
        JPanel top = new JPanel();
        top.add(new JLabel("Nivel:"));
        levelField = new JTextField(String.valueOf(level), 3);
        levelField.setEditable(false);
        resetBtn = new JButton("Reset");
        top.add(levelField);
        top.add(resetBtn);
        container.add(top, BorderLayout.NORTH);

        // Listener de movimientos de discos
        view.setMoveListener((from, to) -> {
            if (model.moveDisk(from, to)) {
                view.setPegs(model.getPegs());
                // Si todos los discos están en la tercera torre, completar nivel
                if (model.getPegs().get(2).size() == level) {
                    JOptionPane.showMessageDialog(container,
                            "¡Nivel " + level + " completado! Avanzando al siguiente.");
                    level++;
                    levelField.setText(String.valueOf(level));
                    model.reset(level);
                    view.setPegs(model.getPegs());
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        });

        // Botón Reset manual
        resetBtn.addActionListener(e -> {
            level = Integer.parseInt(levelField.getText());
            model.reset(level);
            view.setPegs(model.getPegs());
        });

        // Mostrar estado inicial
        view.setPegs(model.getPegs());
    }
}