package org.example.reinas.controller;

import org.example.reinas.model.QueensModel;
import org.example.reinas.view.QueensView;
import org.example.unificado.db.DBManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class QueensController {
    private QueensModel modelo;
    private QueensView vista;
    private List<int[]> soluciones;
    private int IndiceActual;
    private DBManager db;

    public QueensController(QueensModel modelo, QueensView vista) {
        this.modelo = modelo;
        this.vista = vista;

        try {
            db = new DBManager();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos.");
        }

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
                    vista.logSolucion("N = " + n + ": " + soluciones.size() + " soluciones encontradas.");

                    try {
                        if (db != null) {
                            db.resultado_reinas_guardado(n, soluciones.size());
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al guardar resultado de Reinas.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontraron soluciones.");
                    vista.logSolucion("N = " + n + ": 0 soluciones.");
                }
            }
        });

        vista.addPrevListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (IndiceActual > 0) {
                    IndiceActual--;
                    vista.showSolution(soluciones.get(IndiceActual), IndiceActual, soluciones.size());
                    vista.logSolucion("Mostrando solución #" + (IndiceActual + 1));
                }
            }
        });

        vista.addNextListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (IndiceActual < soluciones.size() - 1) {
                    IndiceActual++;
                    vista.showSolution(soluciones.get(IndiceActual), IndiceActual, soluciones.size());
                    vista.logSolucion("Mostrando solución #" + (IndiceActual + 1));
                }
            }
        });
    }
}
