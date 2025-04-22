package org.example.caballo.controller;

import org.example.caballo.model.KnightModel;
import org.example.caballo.view.KnightView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KnightController {
    private KnightModel modelo;
    private KnightView vista;

    public KnightController(KnightModel modelo, KnightView vista) {
        this.modelo = modelo;
        this.vista = vista;
        initController();
    }

    private void initController() {
        vista.addStartListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d = vista.getDimension();
                modelo = new KnightModel(d, d/2, d/2);
                vista.showTour(modelo.computeTour());
            }
        });
    }
}