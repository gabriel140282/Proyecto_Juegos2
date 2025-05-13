package org.example.caballo.controller;

import org.example.caballo.model.KnightModel;
import org.example.caballo.view.KnightView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.unificado.db.DBManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class KnightController {
    private KnightModel modelo;
    private KnightView vista;
    private DBManager db;

    public KnightController(KnightModel modelo, KnightView vista) {
        this.modelo = modelo;
        this.vista = vista;
        try {
            this.db = new DBManager();  // conexión a BD
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
        }
        initController();
    }


    private void initController() {
        vista.addStartListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d = vista.getDimension();
                modelo = new KnightModel(d, d / 2, d / 2);
                java.util.List<int[]> tour = modelo.computeTour();
                vista.showTour(tour);

                // Guardar en base de datos si hay tour
                if (tour != null && !tour.isEmpty()) {
                    try {
                        db.resultado_knight_guardado(d, d / 2, d / 2, tour.size(), 0);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al guardar resultado del caballo.");
                    }
                }
            }
        });
    }
}