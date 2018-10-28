package assignment1;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author N.C.M. van Nistelrooij
 */
public class HopcroftKarp {
    public static boolean BFS(Graph graph, int[] PairU, int[] PairV, int[] Dist) {
        Queue<Integer> queue = new LinkedList<>();

        for(int u = 1; u <= PairU.length; u++) {
            if(PairU[u - 1] == 0) {
                Dist[u] = 0;
                queue.add(u);
            } else {
                Dist[u] = Integer.MAX_VALUE;
            }
        }
        Dist[0] = Integer.MAX_VALUE;
        while(!queue.isEmpty()) {
            int u = queue.remove();
            if(Dist[u] < Dist[0]) {
                for(Edge edge : graph.getAdjacencyList(u)) {
                    if(edge != null) {
                        int v = edge.getTo() - PairV.length;
                        if(Dist[PairV[v - 1]] == Integer.MAX_VALUE) {
                            Dist[PairV[v - 1]] = Dist[u] + 1;
                            queue.add(PairV[v - 1]);
                        }
                    }
                }
            }
        }

        return Dist[0] != Integer.MAX_VALUE;
    }

    public static boolean DFS(int u, Graph graph, int[] PairU, int[] PairV, int[] Dist) {
        if(u != 0) {
            for(Edge edge : graph.getAdjacencyList(u)) {
                if(edge != null) {
                    int v = edge.getTo() - PairV.length;
                    if(Dist[PairV[v - 1]] == Dist[u] + 1) {
                        if(DFS(PairV[v - 1], graph, PairU, PairV, Dist)) {
                            PairV[v - 1] = u;
                            PairU[u - 1] = v;
                            return true;
                        }
                    }
                }
            }

            Dist[u] = Integer.MAX_VALUE;
            return false;
        }

        return true;
    }

    public static int flowValue(Graph graph, int nrOfBoxes) {
        int[] PairU = new int[nrOfBoxes];
        int[] PairV = new int[nrOfBoxes];
        int[] Dist = new int[nrOfBoxes * 2 + 1];
        int flowValue = 0;

        while(BFS(graph, PairU, PairV, Dist)) {
            for(int u = 1; u <= nrOfBoxes; u++) {
                if(PairU[u - 1] == 0 && DFS(u, graph, PairU, PairV, Dist)) {
                    flowValue++;
                }
            }
        }

        return flowValue;
    }
}
