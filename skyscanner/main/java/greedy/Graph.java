package greedy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Vivek Mittal
 */
public class Graph {
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new TreeSet<>((o1, o2) -> Float.compare(o1.getWeight(), o2.getWeight()));
    private HashMap<String, Node> nodeMap = new HashMap<>();

    public void addEdge(String nodeA, String nodeB, Float cost) {
        final Node aNode = createOrGetNode(nodeA);
        final Node anotherNode = createOrGetNode(nodeB);
        final Edge edge = new Edge(aNode, anotherNode, cost);

        aNode.addConnection(edge);
        edges.add(edge);
    }

    public Node createOrGetNode(String node) {
        return Optional.ofNullable(getNode(node))
                .orElseGet(() -> {
                    final Node newNode = new Node(node);
                    nodes.add(newNode);
                    nodeMap.put(node, newNode);
                    return newNode;
                });
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public boolean isConnected(Node A, Node B) {
        return nodes.contains(A) && nodes.contains(B);
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public Node getNode(String node) {
        return nodeMap.get(node);
    }

    public class Node {
        private final String name;
        private final Set<Edge> connections = new HashSet<>();
        private final Set<Node> reachableNodes = new HashSet<>();

        public Node(String name) {
            Optional.ofNullable(name).orElseThrow(RuntimeException::new);
            this.name = name;
        }

        public void addConnection(Edge edge) {
            this.connections.add(edge);
            edge.getAnotherNode(this).connections.add(edge);

            final Node anotherNode = edge.getAnotherNode(this);

            this.reachableNodes.add(this);
            this.reachableNodes.add(anotherNode);
            this.reachableNodes.addAll(anotherNode.reachableNodes);

            for (Node node : reachableNodes) {
                node.reachableNodes.add(this);
                node.reachableNodes.addAll(reachableNodes);
                node.reachableNodes.add(anotherNode);
                node.reachableNodes.addAll(anotherNode.reachableNodes);
            }
        }

        public Set<Edge> getConnections() {
            return this.connections;
        }

        public boolean canReach(Node node) {
            return reachableNodes.contains(node);
        }

        public String getName() {
            return this.name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Node)) {
                return false;
            }

            Node node = (Node) o;

            return !(this.name != null ? !this.name.equals(node.name) : node.name != null);
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    public class Edge {
        private final Node nodeA;
        private final Node nodeB;
        private Float weight;

        public Edge(Node nodeA, Node nodeB, Float weight) {
            this.nodeA = Optional.ofNullable(nodeA).orElseThrow(RuntimeException::new);
            this.nodeB = Optional.ofNullable(nodeB).orElseThrow(RuntimeException::new);;
            this.weight = weight;
        }

        public Node getAnotherNode(Node node) {
            if (this.nodeA.equals(node)) {
                return getNodeB();
            }
            if (this.nodeB.equals(node)) {
                return this.nodeA;
            }

            throw new RuntimeException("Bad Node Requested");
        }

        public Float getWeight() {
            return weight;
        }

        public Node getNodeA() {
            return nodeA;
        }

        public Node getNodeB() {
            return nodeB;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Edge)) {
                return false;
            }

            Edge edge = (Edge) o;
            return (getNodeA().equals(edge.getNodeA()) && getNodeB().equals(edge.getNodeB()) || getNodeA().equals(edge.getNodeB())) && getNodeB().equals(edge.getNodeA());
        }

        @Override
        public int hashCode() {
            int result = getNodeA() != null ? getNodeA().hashCode() : 0;
            result = 31 * (result + (getNodeB() != null ? getNodeB().hashCode() : 0));
            result = result + (weight != null ? weight.hashCode() : 0);
            return result;
        }

    }
}