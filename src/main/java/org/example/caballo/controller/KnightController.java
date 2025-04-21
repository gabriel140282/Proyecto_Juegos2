package org.example.caballo.controller;

import org.example.caballo.model.KnightModel;
import org.example.caballo.view.KnightView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KnightController {
    private KnightModel model;
    private KnightView view;

    public KnightController(KnightModel model, KnightView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.addStartListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d = view.getDimension();
                model = new KnightModel(d, d/2, d/2);
                view.showTour(model.computeTour());
            }
        });
    }
}