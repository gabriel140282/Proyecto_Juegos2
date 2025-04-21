package org.example.reinas.controller;

import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueensController {
    private QueensModel model;
    private QueensView view;

    public QueensController(QueensModel model, QueensView view) {
        this.model = model;
        this.view = view;
        init();
    }

    private void init() {
        view.addSolveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = view.getN();
                model = new QueensModel(n);
                view.display(model.solve());
            }
        });
    }
}
