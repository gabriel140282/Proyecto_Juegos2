package org.example.disco.controller;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.example.disco.model.HanoiModel;
import org.example.disco.view.HanoiView;

public class HanoiController {
    private HanoiModel model;
    private HanoiView view;
    private JTextField nField;
    private JButton resetBtn;

    /**
     * Monta todo el MVC de Hanoi (vista + controles) en el panel que le pases.
     */
    public HanoiController(JPanel container) {
        // 1) inicializa modelo y vista con n=5 por defecto
        model = new HanoiModel(5);
        view = new HanoiView(5);

        // 2) Layout del panel contenedor
        container.setLayout(new BorderLayout());
        container.add(view, BorderLayout.CENTER);

        // 3) Panel de controles (reset)
        JPanel top = new JPanel();
        top.add(new JLabel("Discos:"));
        nField = new JTextField("5", 3);
        resetBtn = new JButton("Reset");
        top.add(nField);
        top.add(resetBtn);
        container.add(top, BorderLayout.NORTH);

        // 4) Conectar el listener de movimientos
        view.setMoveListener((from, to) -> {
            if (model.moveDisk(from, to)) {
                view.setPegs(model.getPegs());
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        });

        // 5) Conectar el botón Reset
        resetBtn.addActionListener(e -> {
            int n = Integer.parseInt(nField.getText());
            model.reset(n);
            view.setPegs(model.getPegs());
        });

        // 6) Inicializar estado de la vista
        resetBtn.doClick();
    }
}
