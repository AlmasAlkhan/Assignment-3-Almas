import java.util.*;

/**
 * Prim's Algorithm for finding Minimum Spanning Tree
 * Uses priority queue for efficient edge selection
 * 
 * @author Almas
 */
public class PrimAlgorithm {
    private final Graph graph;
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationCount;
    private long executionTime;
    
    /**
     * Constructor
     * @param graph Input graph
     */
    public PrimAlgorithm(Graph graph) {
        this.graph = graph;
        this.mstEdges = new ArrayList<>();
        this.totalCost = 0;
        this.operationCount = 0;
        this.executionTime = 0;
    }
    
    /**
     * Find the Minimum Spanning Tree using Prim's algorithm
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
        
        // Priority queue to store edges by weight
        PriorityQueue<EdgeWithVertex> pq = new PriorityQueue<>();
        Set<String> inMST = new HashSet<>();
        
        // Start from first vertex
        String startVertex = vertices.iterator().next();
        inMST.add(startVertex);
        operationCount++; // Adding vertex to MST
        
        // Add all edges from start vertex to priority queue
        for (Edge edge : graph.getAdjacentEdges(startVertex)) {
            pq.offer(new EdgeWithVertex(edge, edge.getTo()));
            operationCount++; // Adding edge to PQ
        }
        
        // Process edges until MST is complete or PQ is empty
        while (!pq.isEmpty() && inMST.size() < vertices.size()) {
            EdgeWithVertex current = pq.poll();
            operationCount++; // Polling from PQ
            
            Edge edge = current.edge;
            String vertex = current.vertex;
            
            // Skip if vertex already in MST
            if (inMST.contains(vertex)) {
                operationCount++; // Comparison
                continue;
            }
            
            // Add edge to MST
            mstEdges.add(edge);
            totalCost += edge.getWeight();
            inMST.add(vertex);
            operationCount += 3; // Add edge, add cost, add vertex
            
            // Add all adjacent edges of the new vertex
            for (Edge adjacentEdge : graph.getAdjacentEdges(vertex)) {
                String nextVertex = adjacentEdge.getTo();
                if (!inMST.contains(nextVertex)) {
                    pq.offer(new EdgeWithVertex(adjacentEdge, nextVertex));
                    operationCount += 2; // Check + Add to PQ
                }
            }
        }
        
        long endTime = System.nanoTime();
        executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        
        // Check if MST is complete
        return inMST.size() == vertices.size();
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
        sb.append("Prim's Algorithm Results:\n");
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
    
    /**
     * Helper class to store edge with its destination vertex
     */
    private static class EdgeWithVertex implements Comparable<EdgeWithVertex> {
        Edge edge;
        String vertex;
        
        EdgeWithVertex(Edge edge, String vertex) {
            this.edge = edge;
            this.vertex = vertex;
        }
        
        @Override
        public int compareTo(EdgeWithVertex other) {
            return edge.compareTo(other.edge);
        }
    }
}




