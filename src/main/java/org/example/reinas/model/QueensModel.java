package org.example.reinas.model;

import java.util.ArrayList;
import java.util.List;

public class QueensModel {
    private int n;
    private List<int[]> soluciones;

    public QueensModel(int n) {
        this.n = n;
    }

    public List<int[]> solve() {
        soluciones = new ArrayList<>();
        backtrack(new int[n], 0);
        return soluciones;
    }

    private void backtrack(int[] pos, int row) {
        if (row == n) {
            soluciones.add(pos.clone());
            return;
        }
        for (int col = 0; col < n; col++) {
            if (isSafe(pos, row, col)) {
                pos[row] = col;
                backtrack(pos, row + 1);
            }
        }
    }

    private boolean isSafe(int[] pos, int row, int col) {
        for (int i = 0; i < row; i++) {
            if (pos[i] == col || Math.abs(pos[i] - col) == Math.abs(i - row)) {
                return false;
            }
        }
        return true;
    }
}