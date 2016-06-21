package greedy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Vivek Mittal
 */
public class Main {
    public static void main(String[] args) {
        String routesString = "4 5 0.35\n" +
                "4 7 0.37\n" +
                "5 7 0.28\n" +
                "7 5 0.28\n" +
                "0 7 0.16\n" +
                "1 5 0.32\n" +
                "0 4 0.38\n" +
                "2 3 0.17\n" +
                "1 7 0.19\n" +
                "0 2 0.26\n" +
                "1 2 0.36\n" +
                "1 3 0.29\n" +
                "2 7 0.34\n" +
                "6 2 0.40\n" +
                "3 6 0.52\n" +
                "6 0 0.58\n" +
                "6 4 0.93";

        Graph graph = new Graph();
        final String[] split = routesString.split("\n");
        for (String route : split) {
            final String[] strings = route.split(" ");
            graph.addEdge(strings[0], strings[1], Float.valueOf(strings[2]));
        }

        Graph newGraph = null;

        newGraph = kruskals(graph);

        dfs(newGraph.getNode("0"), new HashMap<>(), System.out::println);

        System.out.println(totalWeight(newGraph));
    }

    private static Graph kruskals(Graph graph) {
        Graph newGraph = new Graph();
        for (Graph.Node node : graph.getNodes()) {
            newGraph.createOrGetNode(node.getName());
        }

        for (Graph.Edge edge : graph.getEdges()) {
            if (newGraph.getEdges().size() == graph.getNodes().size() - 1) {
                return newGraph;
            }

            final String nodeA = edge.getNodeA().getName();
            final String nodeB = edge.getNodeB().getName();
            if (!newGraph.getNode(nodeA).canReach(newGraph.getNode(nodeB))) {
                newGraph.addEdge(nodeA, nodeB, edge.getWeight());
            }
        }

        return newGraph;
    }

    private static float totalWeight(Graph graph) {
        float total = 0.0f;
        for (Graph.Edge edge : graph.getEdges()) {
            total += edge.getWeight();
        }
        return total;
    }

    private static void dfs(Graph.Node node, Map<Graph.Node, Boolean> nodeVisited, Consumer<Graph.Node> doWithEveryNode) {
        if (node == null) {
            return;
        }

        nodeVisited.put(node, true);
        doWithEveryNode.accept(node);

        for (Graph.Edge edge : node.getConnections()) {
            if (!nodeVisited.containsKey(edge.getAnotherNode(node))) {
                dfs(edge.getAnotherNode(node), nodeVisited, doWithEveryNode);
            }
        }
    }
}
