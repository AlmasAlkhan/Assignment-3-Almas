import java.util.*;

/**
 * Kruskal's Algorithm for finding Minimum Spanning Tree
 * Uses Union-Find data structure for cycle detection
 * 
 * @author Almas
 */
public class KruskalAlgorithm {
    private final Graph graph;
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationCount;
    private long executionTime;
    
    /**
     * Constructor
     * @param graph Input graph
     */
    public KruskalAlgorithm(Graph graph) {
        this.graph = graph;
        this.mstEdges = new ArrayList<>();
        this.totalCost = 0;
        this.operationCount = 0;
        this.executionTime = 0;
    }
    
    /**
     * Find the Minimum Spanning Tree using Kruskal's algorithm
     * @return true if MST found, false if graph is disconnected
     */
    public boolean findMST() {
        long startTime = System.nanoTime();
        
        Set<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            executionTime = 0;
            return false;
        }
        
        // Reset
        mstEdges = new ArrayList<>();
        totalCost = 0;
        operationCount = 0;
        
        // Initialize Union-Find
        UnionFind uf = new UnionFind(vertices);
        operationCount++; // Initialize UF
        
        // Get all edges and sort by weight
        List<Edge> edges = new ArrayList<>(graph.getEdges());
        Collections.sort(edges);
        operationCount += edges.size() * (int)(Math.log(edges.size()) / Math.log(2)); // Sorting complexity
        
        // Process edges in order of weight
        for (Edge edge : edges) {
            String from = edge.getFrom();
            String to = edge.getTo();
            
            operationCount++; // Processing edge
            
            // Check if adding this edge creates a cycle
            if (!uf.connected(from, to)) {
                operationCount += 2; // Find operations
                
                // Add edge to MST
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                uf.union(from, to);
                operationCount += 2; // Union operation
                
                // If we have V-1 edges, MST is complete
                if (mstEdges.size() == vertices.size() - 1) {
                    break;
                }
            }
        }
        
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        
        // Check if MST is complete
        return mstEdges.size() == vertices.size() - 1;
    }
    
    /**
     * Get the MST edges
     * @return List of edges in MST
     */
    public List<Edge> getMSTEdges() {
        return new ArrayList<>(mstEdges);
    }
    
    /**
     * Get the total cost of MST
     * @return total cost
     */
    public int getTotalCost() {
        return totalCost;
    }
    
    /**
     * Get the number of operations performed
     * @return operation count
     */
    public int getOperationCount() {
        return operationCount;
    }
    
    /**
     * Get execution time in milliseconds
     * @return execution time
     */
    public long getExecutionTime() {
        return executionTime;
    }
    
    /**
     * Get formatted results string
     * @return results string
     */
    public String getResultsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Kruskal's Algorithm Results:\n");
        sb.append("Total Cost: ").append(totalCost).append("\n");
        sb.append("Number of Edges: ").append(mstEdges.size()).append("\n");
        sb.append("Operations: ").append(operationCount).append("\n");
        sb.append("Execution Time: ").append(executionTime).append(" ms\n");
        sb.append("MST Edges:\n");
        for (Edge edge : mstEdges) {
            sb.append("  ").append(edge).append("\n");
        }
        return sb.toString();
    }
}






