package org.example.unificado.controller;

import org.example.unificado.view.MainView;
import org.example.caballo.model.KnightModel;
import org.example.caballo.view.KnightView;
import org.example.caballo.controller.KnightController;
import org.example.disco.model.HanoiModel;          // ← paquete correcto
import org.example.disco.view.HanoiView;
import org.example.disco.controller.HanoiController;
import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import org.example.reinas.controller.QueensController;
import javax.swing.*;
import java.awt.*;

public class MainController {
    private MainView view;

    public MainController(MainView view) {
        this.view = view;
        init();
    }

    private void init() {
        // --- Caballo MVC clásico con model+view ---
        KnightView kv = new KnightView();
        new KnightController(new KnightModel(27, 13, 13), kv);
        view.addTab("Caballo", kv);

        // --- Hanoi MVC montado en un JPanel ---
        JPanel hanoiTab = new JPanel();
        new HanoiController(hanoiTab);  // usa el ctor que hace reset, view y controles
        view.addTab("Hanoi", hanoiTab);

        // --- Reinas MVC clásico con model+view ---
        QueensView qv = new QueensView();
        new QueensController(new QueensModel(8), qv);
        view.addTab("Reinas", qv);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mv = new MainView();
            new MainController(mv);
            mv.setVisible(true);
        });
    }
}
