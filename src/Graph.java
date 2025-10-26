import java.util.*;

/**
 * Graph class representing a weighted undirected graph
 * Uses adjacency list representation
 * 
 * @author Almas
 */
public class Graph {
    private final Map<String, List<Edge>> adjacencyList;
    private final List<Edge> edges;
    private final Set<String> vertices;
    
    /**
     * Constructor for Graph
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.edges = new ArrayList<>();
        this.vertices = new HashSet<>();
    }
    
    /**
     * Add a vertex to the graph
     * @param vertex Vertex name
     */
    public void addVertex(String vertex) {
        vertices.add(vertex);
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }
    
    /**
     * Add an edge to the graph (undirected)
     * @param from Starting vertex
     * @param to Ending vertex
     * @param weight Edge weight
     */
    public void addEdge(String from, String to, int weight) {
        // Add vertices if they don't exist
        addVertex(from);
        addVertex(to);
        
        // Create edge
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        
        // Add to adjacency list (undirected, so add both directions)
        adjacencyList.get(from).add(edge);
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }
    
    /**
     * Get all edges in the graph
     * @return List of edges
     */
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    /**
     * Get all vertices in the graph
     * @return Set of vertices
     */
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }
    
    /**
     * Get adjacent edges for a vertex
     * @param vertex Vertex name
     * @return List of adjacent edges
     */
    public List<Edge> getAdjacentEdges(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }
    
    /**
     * Get the number of vertices
     * @return vertex count
     */
    public int getVertexCount() {
        return vertices.size();
    }
    
    /**
     * Get the number of edges
     * @return edge count
     */
    public int getEdgeCount() {
        return edges.size();
    }
    
    /**
     * Check if the graph is connected using DFS
     * @return true if connected
     */
    public boolean isConnected() {
        if (vertices.isEmpty()) return true;
        
        Set<String> visited = new HashSet<>();
        String startVertex = vertices.iterator().next();
        
        dfs(startVertex, visited);
        
        return visited.size() == vertices.size();
    }
    
    /**
     * Depth-first search helper method
     * @param vertex Current vertex
     * @param visited Set of visited vertices
     */
    private void dfs(String vertex, Set<String> visited) {
        visited.add(vertex);
        
        for (Edge edge : getAdjacentEdges(vertex)) {
            String neighbor = edge.getTo();
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }
    
    /**
     * String representation of the graph
     * @return formatted graph string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(getVertexCount())
          .append(" vertices and ").append(getEdgeCount()).append(" edges\n");
        
        for (String vertex : vertices) {
            sb.append(vertex).append(": ");
            for (Edge edge : getAdjacentEdges(vertex)) {
                sb.append(edge.getTo()).append("(").append(edge.getWeight()).append(") ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}

