package org.example.reinas.controller;

import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QueensController {
    private QueensModel modelo;
    private QueensView vista;
    private List<int[]> soluciones;
    private int IndiceActual;

    public QueensController(QueensModel modelo, QueensView vista) {
        this.modelo = modelo;
        this.vista = vista;
        inicializar_acciones();
    }

    private void inicializar_acciones() {
        vista.addSolveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = vista.getN();
                modelo = new QueensModel(n);
                soluciones = modelo.solve();
                IndiceActual = 0;
                if (!soluciones.isEmpty()) {
                    vista.showSolution(soluciones.get(0), 0, soluciones.size());
                }
            }
        });
        vista.addPrevListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (IndiceActual > 0) {
                    IndiceActual--;
                    vista.showSolution(soluciones.get(IndiceActual), IndiceActual, soluciones.size());
                }
            }
        });
        vista.addNextListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (IndiceActual < soluciones.size() - 1) {
                    IndiceActual++;
                    vista.showSolution(soluciones.get(IndiceActual), IndiceActual, soluciones.size());
                }
            }
        });
    }
}
