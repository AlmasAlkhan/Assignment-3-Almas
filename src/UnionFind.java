import java.util.*;

/**
 * Union-Find (Disjoint Set) data structure
 * Used for cycle detection in Kruskal's algorithm
 * Implements path compression and union by rank optimizations
 * 
 * @author Almas
 */
public class UnionFind {
    private final Map<String, String> parent;
    private final Map<String, Integer> rank;
    
    /**
     * Constructor - initializes Union-Find for given vertices
     * @param vertices Set of vertices
     */
    public UnionFind(Set<String> vertices) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        
        // Initialize each vertex as its own parent
        for (String vertex : vertices) {
            parent.put(vertex, vertex);
            rank.put(vertex, 0);
        }
    }
    
    /**
     * Find the root of the set containing vertex
     * Uses path compression for optimization
     * 
     * @param vertex Vertex to find
     * @return Root of the set
     */
    public String find(String vertex) {
        if (!parent.get(vertex).equals(vertex)) {
            // Path compression: make vertex point directly to root
            parent.put(vertex, find(parent.get(vertex)));
        }
        return parent.get(vertex);
    }
    
    /**
     * Union two sets containing vertices u and v
     * Uses union by rank for optimization
     * 
     * @param u First vertex
     * @param v Second vertex
     */
    public void union(String u, String v) {
        String rootU = find(u);
        String rootV = find(v);
        
        if (rootU.equals(rootV)) {
            return; // Already in same set
        }
        
        // Union by rank: attach smaller tree under larger tree
        int rankU = rank.get(rootU);
        int rankV = rank.get(rootV);
        
        if (rankU < rankV) {
            parent.put(rootU, rootV);
        } else if (rankU > rankV) {
            parent.put(rootV, rootU);
        } else {
            parent.put(rootV, rootU);
            rank.put(rootU, rankU + 1);
        }
    }
    
    /**
     * Check if two vertices are in the same set (connected)
     * 
     * @param u First vertex
     * @param v Second vertex
     * @return true if connected
     */
    public boolean connected(String u, String v) {
        return find(u).equals(find(v));
    }
}






