/**
 * Edge class representing a weighted edge in an undirected graph
 * 
 * @author Almas
 */
public class Edge implements Comparable<Edge> {
    private final String from;
    private final String to;
    private final int weight;
    
    /**
     * Constructor for Edge
     * 
     * @param from Starting vertex
     * @param to Ending vertex
     * @param weight Weight/cost of the edge
     */
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    
    /**
     * Get the starting vertex
     * @return from vertex
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * Get the ending vertex
     * @return to vertex
     */
    public String getTo() {
        return to;
    }
    
    /**
     * Get the weight of the edge
     * @return weight
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * Compare edges by weight (for sorting)
     * @param other Edge to compare with
     * @return comparison result
     */
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
    
    /**
     * String representation of the edge
     * @return formatted edge string
     */
    @Override
    public String toString() {
        return String.format("%s --%d-- %s", from, weight, to);
    }
    
    /**
     * Check if two edges are equal
     * @param obj Object to compare
     * @return true if edges are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return weight == edge.weight &&
               ((from.equals(edge.from) && to.equals(edge.to)) ||
                (from.equals(edge.to) && to.equals(edge.from)));
    }
    
    /**
     * Generate hash code for the edge
     * @return hash code
     */
    @Override
    public int hashCode() {
        // For undirected edges, order shouldn't matter
        int hash1 = from.hashCode() + to.hashCode();
        int hash2 = weight;
        return 31 * hash1 + hash2;
    }
}

