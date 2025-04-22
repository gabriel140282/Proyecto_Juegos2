package org.example.unificado.controller;

import org.example.unificado.view.MainView;
import org.example.caballo.model.KnightModel;
import org.example.caballo.view.KnightView;
import org.example.caballo.controller.KnightController;
import org.example.disco.controller.HanoiController;
import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import org.example.reinas.controller.QueensController;
import javax.swing.*;

public class MainController {
    private MainView vista;

    public MainController(MainView vista) {
        this.vista = vista;
        inicializar_juegos();
    }

    private void inicializar_juegos() {
        // Caballo MVC clásico con model+view
        KnightView kv = new KnightView();
        new KnightController(new KnightModel(8,0,0), kv);
        vista.addTab("Caballo", kv);

        // Hanoi MVC clásico con model+view
        JPanel hanoiTab = new JPanel();
        new HanoiController(hanoiTab);  // usa el ctor que hace reset, view y controles
        vista.addTab("Hanoi", hanoiTab);

        // Reinas MVC clásico con model+view
        QueensView qv = new QueensView();
        new QueensController(new QueensModel(8), qv);
        vista.addTab("Reinas", qv);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mv = new MainView();
            new MainController(mv);
            mv.setVisible(true);
        });
    }
}
