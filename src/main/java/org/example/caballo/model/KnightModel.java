package org.example.caballo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para el Tour del Caballo: Warnsdorff + Backtracking
 */
public class KnightModel {
    private int d;
    private int startX, startY;
    private boolean[][] visitado;
    private List<int[]> path;
    private final int[] dx = {2,2,1,1,-1,-1,-2,-2};
    private final int[] dy = {1,-1,2,-2,2,-2,1,-1};

    public KnightModel(int dimension, int startRow, int startCol) {
        this.d = dimension;
        this.startX = startRow;
        this.startY = startCol;
    }

    public List<int[]> computeTour() {
        visitado = new boolean[d][d];
        path = new ArrayList<>();
        dfs(startX, startY, 0);
        return path;
    }

    private boolean dfs(int x, int y, int paso) {
        visitado[x][y] = true;
        path.add(new int[]{x, y});
        if (paso == d*d - 1) return true;
        List<int[]> moves = new ArrayList<>();
        for (int i=0; i<8; i++) {
            int nx = x+dx[i], ny = y+dy[i];
            if (nx>=0 && ny>=0 && nx<d && ny<d && !visitado[nx][ny]) {
                moves.add(new int[]{nx, ny});
            }
        }
        moves.sort((a,b) -> degree(a[0],a[1]) - degree(b[0],b[1]));
        for (int[] mv : moves) {
            if (dfs(mv[0], mv[1], paso+1)) return true;
        }
        visitado[x][y] = false;
        path.remove(path.size()-1);
        return false;
    }

    private int degree(int x, int y) {
        int cnt=0;
        for (int i=0; i<8; i++) {
            int nx=x+dx[i], ny=y+dy[i];
            if (nx>=0 && ny>=0 && nx<d && ny<d && !visitado[nx][ny]) cnt++;
        }
        return cnt;
    }
}
