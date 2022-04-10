package lab;

import java.util.Hashtable;
import java.util.PriorityQueue;

import frame.Graph;
import frame.SSSPNode;

public class Algorithms {

    /**
     * @param graph     input graph
     * @param edgeRatio specifying until what ratio of actualEdge#/possibleEdge# the
     *                  Johnsons is to run, otherwise FloydWarshall will be run
     * @return a hashmap with the nodeId´s as entries for "their" SSSPNodes
     */
    public static Hashtable<Integer, Hashtable<Integer, SSSPNode>> HybridSSSP(Graph graph, double edgeRatio) {
        double actualEdges = graph.getEdgeNumber();
        double possibleEdges = (actualEdges * (actualEdges - 1)); // for directed graph, with no self-loops
        if ((actualEdges / possibleEdges) <= edgeRatio) {
            return Johnson(graph);
        } else
            return FloydWarshall(graph);
    }

    /**
     * @param graph on which to run the algorithm
     * @return a hashmap with the nodeId´s as entries for "their" SSSPNodes; null if
     * negative cycle exists
     */
    public static Hashtable<Integer, Hashtable<Integer, SSSPNode>> Johnson(Graph graph) {
        // 1.
        // Initialisiere neuen Graphen mit ZUsätzlichen Knoten dessen Distanz zu allen
        // Knoten Infty ist.
        Hashtable<Integer, Hashtable<Integer, Integer>> newEdges = new Hashtable<Integer, Hashtable<Integer, Integer>>();
        for (int i = 0; i < graph.getNodeNumber(); i++) {
            newEdges.put(i, new Hashtable<Integer, Integer>());
            final int from = i;
            graph.getNeighbours(from).forEach(to -> {
                newEdges.get(from).put(to, graph.getEdge(from, to));
            });
        }

        newEdges.put(graph.getNodeNumber(), new Hashtable<Integer, Integer>());
        for (int i = 0; i < graph.getNodeNumber(); i++)
            newEdges.get(graph.getNodeNumber()).put(i, 0);

        Graph g = new Graph(graph.getNodeNumber() + 1, newEdges);
        // 2.
        Hashtable<Integer, SSSPNode> BelF = BellmanFord(g, graph.getNodeNumber());

        if (BelF == null)
            return null;

        // 3.
        Hashtable<Integer, Hashtable<Integer, Integer>> correctEdges = new Hashtable<Integer, Hashtable<Integer, Integer>>();
        for (int i = 0; i < newEdges.size() - 1; i++) {
            correctEdges.put(i, new Hashtable<Integer, Integer>());
            for (int z = 0; z < graph.getNodeNumber(); z++) {
                try {
                    correctEdges.get(i).put(z, (BelF.get(i).distance + newEdges.get(i).get(z) - BelF.get(z).distance));
                } catch (NullPointerException exc) {
                }
            }
        }

        Graph gSecond = new Graph(graph.getNodeNumber(), correctEdges);
        // 4.
        Hashtable<Integer, Hashtable<Integer, SSSPNode>> result = new Hashtable<Integer, Hashtable<Integer, SSSPNode>>();
        for (int i = 0; i < graph.getNodeNumber(); i++) {
            result.put(i, Dijkstra(gSecond, i));
            // 5.
            for (int to = 0; to < graph.getNodeNumber(); to++)
                try {
                    result.get(i).get(to).distance = result.get(i).get(to).distance + BelF.get(to).distance
                            - BelF.get(i).distance;
                } catch (NullPointerException exc) {
                }
        }
        return result;

    }

    /**
     * @param graph  the input graph
     * @param source a valid Node ID of the graph
     * @return for each node in the graph an entry in the hashmap which leads to a
     * datatype storing the results of the algorithm (as on the slides);
     * returns null if negative cycle exists
     */
    public static Hashtable<Integer, SSSPNode> BellmanFord(Graph graph, int source) {
        Hashtable<Integer, SSSPNode> result = new Hashtable<Integer, SSSPNode>();

        // initSSSP(G,s,w); initialize the SSSP Nodes
        for (int i = 0; i < graph.getNodeNumber(); i++)
            result.put(i, new SSSPNode(null, null, i));
        result.get(source).distance = 0;

        // FOR i = 1 TO |V|-1 DO ; V is the number of nodes in the graph
        for (int m = 0; m < graph.getNodeNumber(); m++) {

            // FOREACH (u,v) in E Do:
            for (int i = 0; i < graph.getNodeNumber(); i++) {
                int nodeID = i;
                relax(graph, nodeID, result);
            }
        }

        // FOREACH (u,v) in E Do
        for (int i = 0; i < graph.getNodeNumber(); i++) {
            final int nodeID = i;
            if (result.size() == 0)
                return null;
            graph.getNeighbours(nodeID).forEach(x -> {
                if (result.get(nodeID).distance != null && result.get(x).distance != null
                        && result.get(x).distance > result.get(nodeID).distance + graph.getEdge(nodeID, x))
                    result.clear(); // false
            });
        }

        return result; // true
    }

    /**
     * @param graph  the input graph
     * @param source a valid Node ID of the graph
     * @return for each node in the graph an entry in the hashmap which leads to a
     * datatype storing the results of the algorithm (as on the slides)
     */
    public static Hashtable<Integer, SSSPNode> Dijkstra(Graph graph, int source) {
        Hashtable<Integer, SSSPNode> result = new Hashtable<Integer, SSSPNode>();
        PriorityQueue<SSSPNode> Q = new PriorityQueue<SSSPNode>(new SSSPNodeComparator());

        // initSSSP(G,s,w); initialize the SSSP Nodes
        for (int i = 0; i < graph.getNodeNumber(); i++) {
            result.put(i, new SSSPNode(null, null, i));
        }
        result.get(source).distance = 0; // s.dist = 0

        Q.add(result.get(source)); // node added to the queue

        SSSPNode u;
        while ((u = Q.poll()) != null) { // Extract-MIN (Q) and iteration until Q is empty
            final SSSPNode i = u;
            graph.getNeighbours(u.nodeNumber).forEach(x -> {
                int comID = i.nodeNumber;
                if (result.get(x).distance == null
                        || result.get(x).distance > result.get(comID).distance + graph.getEdge(comID, x)) {
                    result.get(x).distance = result.get(comID).distance + graph.getEdge(comID, x);
                    result.get(x).predecessor = comID;
                    Q.add(result.get(x));
                }
            });
        }
        return result;
    }

    /**
     * @param graph input graph
     * @return a hashmap with the nodeId´s as entries for "their" SSSPNodes; null if
     * negative cycle exists
     */
    public static Hashtable<Integer, Hashtable<Integer, SSSPNode>> FloydWarshall(Graph graph) {
        Hashtable<Integer, Hashtable<Integer, SSSPNode>> result = new Hashtable<Integer, Hashtable<Integer, SSSPNode>>();

        // Floyd Algorithmus -> Berechnung der kürzesten Distanz zwischen den Knoten
        // Initialisiere eine Tabelle mit SSSP-Node Einträgen mit allen möglichen
        // Distanzen und Einträgen.
        for (int i = 0; i < graph.getNodeNumber(); i++) {
            result.put(i, new Hashtable<Integer, SSSPNode>());
            for (int j = 0; j < graph.getNodeNumber(); j++) {
                if (i == j)
                    result.get(i).put(j, new SSSPNode(null, 0, j));
                else if (graph.getEdge(i, j) != null)
                    result.get(i).put(j, new SSSPNode(i, graph.getEdge(i, j), j));
                else
                    result.get(i).put(j, new SSSPNode(null, null, j));
            }
        }

        // Warshall Algorthmus, zur Berechnung der kürzesten Wege.
        for (int k = 0; k < graph.getNodeNumber(); k++)
            for (int i = 0; i < graph.getNodeNumber(); i++)
                for (int j = 0; j < graph.getNodeNumber(); j++)
                    if (result.get(i).get(k).distance != null && result.get(k).get(j).distance != null
                            && (result.get(i).get(j).distance == null || result.get(i).get(k).distance
                            + result.get(k).get(j).distance < result.get(i).get(j).distance)) {
                        result.get(i).get(j).distance = result.get(i).get(k).distance + result.get(k).get(j).distance;
                        result.get(i).get(j).predecessor = result.get(k).get(j).predecessor;
                    }

        // Exisiteren Negative Zyklen? Wenn ja, dann ist eine Distanz nach der
        // Berechnung negativ
        for (int i = 0; i < graph.getNodeNumber(); i++)
            if (result.get(i).get(i).distance < 0)
                return null;

        return result;
    }

    private static void relax(Graph graph, final int nodeID, Hashtable<Integer, SSSPNode> result) {
        graph.getNeighbours(nodeID).forEach(x -> {
            if (result.get(nodeID).distance != null && (result.get(x).distance == null
                    || result.get(x).distance > result.get(nodeID).distance + graph.getEdge(nodeID, x))) {
                result.get(x).distance = result.get(nodeID).distance + graph.getEdge(nodeID, x);
                result.get(x).predecessor = nodeID;
            }
        });
    }
}