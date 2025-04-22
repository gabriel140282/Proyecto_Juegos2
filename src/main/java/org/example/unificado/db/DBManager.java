package org.example.unificado.db;

import java.sql.*;

public class DBManager {
    private static final String URL  =
            "jdbc:mysql://localhost:3306/juegos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection conn;

    public DBManager() throws SQLException {

        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException ignored) {}

        conn = DriverManager.getConnection(URL, USER, PASS);
    }

    public void resultado_hanoi_guardado(int nivel, int movimientos) throws SQLException {
        String sql = "INSERT INTO hanoi_results (nivel, movimientos) VALUES (?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, nivel);
            st.setInt(2, movimientos);
            st.executeUpdate();
        }
    }

    public void resultado_reinas_guardado(int n, int totalSolutions) throws SQLException {
        String sql = "INSERT INTO queens_results (n, total_sols) VALUES (?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, n);
            st.setInt(2, totalSolutions);
            st.executeUpdate();
        }
    }

    public void resultado_knight_guardado(int dim, int row, int col, int pasos, int reintentos) throws SQLException {
        String sql = "INSERT INTO knight_results (dimension, start_row, start_col, pasos, reintentos) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, dim);
            st.setInt(2, row);
            st.setInt(3, col);
            st.setInt(4, pasos);
            st.setInt(5, reintentos);
            st.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

