/**
 * Created by Cristian-PC on 15/05/2016.
 */

package ad1.ss16.pa;

import java.util.*;

public class Network {

    private ArrayList<HashSet<Integer>> adj;

    private boolean[] visited;
    private static int totalOfNodes;
    private Integer parent;

    private int p[];
    private int low[];
    private int counter;
    private int tata[];
    private boolean isAP[];

    public Network(int n) {

        adj = new ArrayList<HashSet<Integer>>();
        totalOfNodes = n;

        for (int i = 0; i < n; i++)
            adj.add(new HashSet<Integer>());

    }

    public int numberOfNodes() {
        return totalOfNodes;
    }

    public int numberOfConnections() {
        //Queue<Integer> q = new LinkedList<>();

        int sum = 0;

        for (HashSet<Integer> neighNode : adj)
            sum += neighNode.size();

        return sum / 2;

    }

    public void addConnection(int v, int w) {
        if (!adj.get(v).contains(w) && v != w) {
            adj.get(w).add(v);
            adj.get(v).add(w);
        }

    }

    public void addAllConnections(int v) {
        //HashSet<Integer> set = adj.get(v);

        for (int i = 0; i < adj.size(); i++) {
            if (v != i)
                addConnection(v, i);
        }

    }

    public void deleteConnection(int v, int w) {
        if (adj.get(v).contains(w)) {
            adj.get(v).remove(w);
            adj.get(w).remove(v);
        }

    }

    public void deleteAllConnections(int v) {
        for (int i = 0; i < adj.size(); i++) {
            if (v != i)
                deleteConnection(v, i);
        }
    }

    /*

     */
    public void DFS(int k) {

        visited[k] = true;

        for (int i : adj.get(k))
            if (!visited[i]) {
                DFS(i);
            }

    }

    public int numberOfComponents() {
        int anzahl = 0;
        visited = new boolean[adj.size()];

        for (int i = 0; i < adj.size(); i++) {
            if (visited[i] == false) {
                anzahl += 1;
                DFS(i);
            }

        }
        return anzahl;
    }

    public boolean hasCycle() {

        //it has no cycle
        boolean cycle = false;
        int i;

        visited = new boolean[totalOfNodes];
        tata = new int[totalOfNodes];

        for (i = 0; i < totalOfNodes; i++) {
            visited[i] = false;
            tata[i] = -1;
        }

        for(i = 0; (i < totalOfNodes) && !cycle; i++) {
            if(!visited[i]) {
                cycle = dfsForCycle(i);
            }
        }

        return cycle;
    }

    public boolean dfsForCycle(int i) {
        boolean hasCycle = false;
        visited[i] = true;

        for(int w : adj.get(i)) {
            if (!visited[w]) {
                tata[w] = i;
                hasCycle = dfsForCycle(w);
            }
            else
            if (w != tata[i])
                hasCycle = true;

            if (hasCycle)
                break;
        }

        return hasCycle;
    }



    public int minimalNumberOfConnections(int start, int end) {
        /*
        HashSet<Integer> neighboursOfStart = adj.get(start);
        HashSet<Integer> neighboursOfEnd = adj.get(end);

        if (!neighboursOfStart.contains(end))
            return -1;

        if (!neighboursOfEnd.contains(start))
            return -1;
        */

        if (start == end)
            return 0;

        visited = new boolean[totalOfNodes];
        Queue<Integer> q = new LinkedList<Integer>();
        int[] depth = new int[totalOfNodes];
        depth[start] = 0;

        visited[start] = true;
        //System.out.print(start + " # ");
        q.add(new Integer(start));

        while(!q.isEmpty()){
            Integer obj = q.remove();

            for(int k : adj.get(obj))
                if(!visited[k]){

                    visited[k] = true;
                    depth[k] = depth[obj] + 1;

                    //System.out.print(k + " # ");

                    if( end == k )
                        return depth[k];

                    q.add(k);
                }
        }
        return -1;
    }


    /*
    public List<Integer> criticalNodes() {
        List<Integer> critical = new LinkedList<Integer>();
        return critical;
    }
    */

    private void DFSTarjan(int k) {
        int children = 0;

        visited[k] = true;
        counter++;
        p[k] = counter;
        low[k] = counter;
        for(int u : adj.get(k)) {
            if (!visited[u]) {
                tata[u] = k;
                children++;
                DFSTarjan(u);

                //update the low time
                low[k] = Math.min(low[k], low[u]);

                //visited time of a vertex <= lowtime of any adjacent vertex(back edge)
                if (tata[k] != -1 && p[k] <= low[u]) {
                    isAP[k] = true;
                }
            } else
            if (tata[k] != u) {
                low[k] = Math.min(low[k], p[u]);
            }
        }

        //root vertex with 2 independent children
        if (tata[k] == -1 && children > 1)
            isAP[k] = true;
    }

    public List<Integer> criticalNodes() {
        int n = adj.size();
        int i;

        visited = new boolean[n];

        //storing visited time of vertex
        p = new int[n];

        //min of al low time of all the adj vertices
        low = new int[n];

        tata = new int[n];
        isAP = new boolean[n];

        for (i = 0; i < n; i++) {
            visited[i] = false;
            tata[i] = -1;
            isAP[i] = false;
        }

        //current visited time
        counter = 0;

        for (i = 0; i < n; i++) {
            if (visited[i] == false) {
                DFSTarjan(i);
            }
        }

        List<Integer> critical = new LinkedList<Integer>();
        for (i = 0; i < n; i++)
            if (isAP[i])
                critical.add(new Integer(i));

        return critical;
    }




    public static void main(String[] args) {

        Network graph = new Network(13);

        graph.addConnection(0,1);
        graph.addConnection(0,2);
        graph.addConnection(0,6);
        graph.addConnection(1,3);
        graph.addConnection(1,4);
        graph.addConnection(2,4);
        graph.addConnection(2,6);
        graph.addConnection(2,7);
        graph.addConnection(3,4);
        graph.addConnection(4,5);
        graph.addConnection(6,7);

        graph.addConnection(8,9);
        graph.addConnection(8,10);
        graph.addConnection(9, 10);
        graph.addConnection(10,11);
        graph.addConnection(11, 12);

        System.out.println(graph.numberOfNodes());

        System.out.println(graph.numberOfConnections());

        System.out.println("Number of Components " + graph.numberOfComponents());

        System.out.println("Critical nodes: " + graph.criticalNodes());

        System.out.println("Has cycle: " + graph.hasCycle());

        /*
        Network graph = new Network(13);

        graph.addConnection(0,1);
        graph.addConnection(0,2);
        graph.addConnection(0,6);
        graph.addConnection(1,0);
        graph.addConnection(1,3);
        graph.addConnection(1,4);
        graph.addConnection(2,0);
        graph.addConnection(2,4);
        graph.addConnection(2,6);
        graph.addConnection(2,7);

        graph.addConnection(8,9);
        graph.addConnection(8,10);

        System.out.println(graph.numberOfNodes());

        System.out.println(graph.numberOfConnections());

        System.out.println("Number of Compnents " + graph.numberOfComponents());
        System.out.println("Number of Compnents " + graph.numberOfComponents());

        System.out.println("Critical nodes: " + graph.criticalNodes());

        */

        /*
        Network graph = new Network(6);

        graph.addConnection(0, 1);
        graph.addConnection(0, 2);
        graph.addConnection(1, 0);
        graph.addConnection(2, 1);
        graph.addConnection(2, 3);
        graph.addConnection(4, 5);

        //graph.addAllConnections(0);


        System.out.println("Number of nodes: " + graph.numberOfNodes());
        System.out.println("Number of edges: " + graph.numberOfConnections());
        System.out.println("Number of Components " + graph.numberOfComponents());

        //
        System.out.println("The min path: " + graph.minimalNumberOfConnections(0, 3));

        //
        System.out.println("The graph has cycle: " + graph.hasCycle());

        graph.deleteConnection(1, 2);
        System.out.println("The graph has cycle: " + graph.hasCycle());

        System.out.println("Critical nodes: " + graph.criticalNodes());
        */
    }

}



