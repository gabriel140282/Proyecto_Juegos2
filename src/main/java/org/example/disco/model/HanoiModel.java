package org.example.disco.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HanoiModel {
    private int n;
    private Stack<Integer>[] palos;

    @SuppressWarnings("unchecked")
    public HanoiModel(int n) {
        reset(n);
    }

    @SuppressWarnings("unchecked")
    public void reset(int n) {
        this.n = n;
        palos = new Stack[3];
        for (int i = 0; i < 3; i++) {
            palos[i] = new Stack<>();
        }
        for (int d = n; d >= 1; d--) {
            palos[0].push(d);
        }
    }

    public boolean moveDisk(int from, int to) {
        if (from < 0 || from > 2 || to < 0 || to > 2) return false;
        Stack<Integer> src = palos[from];
        Stack<Integer> dst = palos[to];
        if (src.isEmpty()) return false;
        int disk = src.peek();
        if (!dst.isEmpty() && dst.peek() < disk) return false;
        dst.push(src.pop());
        return true;
    }

    public List<Stack<Integer>> getPegs() {
        List<Stack<Integer>> list = new ArrayList<>(3);
        for (Stack<Integer> peg : palos) {
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
