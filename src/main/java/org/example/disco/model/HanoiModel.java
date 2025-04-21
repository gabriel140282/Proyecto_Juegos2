package org.example.disco.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Modelo de Torres de Hanoi para MVC interactivo.
 */
public class HanoiModel {
    private int n;
    private Stack<Integer>[] pegs;

    @SuppressWarnings("unchecked")
    public HanoiModel(int n) {
        reset(n);
    }

    /**
     * Inicializa las pilas con n discos en la primera torre.
     */
    @SuppressWarnings("unchecked")
    public void reset(int n) {
        this.n = n;
        pegs = new Stack[3];
        for (int i = 0; i < 3; i++) {
            pegs[i] = new Stack<>();
        }
        for (int d = n; d >= 1; d--) {
            pegs[0].push(d);
        }
    }

    /**
     * Intenta mover el disco superior de from a to.
     * @return true si el movimiento fue válido, false si ilegal.
     */
    public boolean moveDisk(int from, int to) {
        if (from < 0 || from > 2 || to < 0 || to > 2) return false;
        Stack<Integer> src = pegs[from];
        Stack<Integer> dst = pegs[to];
        if (src.isEmpty()) return false;
        int disk = src.peek();
        if (!dst.isEmpty() && dst.peek() < disk) return false;
        dst.push(src.pop());
        return true;
    }

    /**
     * Devuelve una copia del estado de las pilas.
     */
    public List<Stack<Integer>> getPegs() {
        List<Stack<Integer>> list = new ArrayList<>(3);
        for (Stack<Integer> peg : pegs) {
            // clonamos la pila para que la vista no la modifique
            Stack<Integer> copy = (Stack<Integer>) peg.clone();
            list.add(copy);
        }
        return list;
    }

    public int getN() {
        return n;
    }
}
