import java.io.*;
import java.util.*;

/**
 * Class for writing MST results to JSON file
 * Writes in format: { "results": [ { "graph_id": 1, "prim": {...}, "kruskal": {...} } ] }
 * 
 * @author Almas
 */
public class ResultWriter {
    
    /**
     * Write results to JSON file
     * 
     * @param results List of test results
     * @param filename Output filename
     * @throws IOException if write error occurs
     */
    public static void writeResultsToJSON(List<TestResult> results, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("{");
            writer.println("  \"results\": [");
            
            for (int i = 0; i < results.size(); i++) {
                TestResult result = results.get(i);
                writeResult(writer, result, i == results.size() - 1);
            }
            
            writer.println("  ]");
            writer.println("}");
        }
    }
    
    /**
     * Write a single test result
     */
    private static void writeResult(PrintWriter writer, TestResult result, boolean isLast) {
        writer.println("    {");
        writer.printf("      \"graph_id\": %d,%n", result.graphId);
        
        // Input stats
        writer.println("      \"input_stats\": {");
        writer.printf("        \"vertices\": %d,%n", result.vertices);
        writer.printf("        \"edges\": %d%n", result.edges);
        writer.println("      },");
        
        // Prim results
        writer.println("      \"prim\": {");
        writeEdges(writer, result.primEdges);
        writer.printf("        \"total_cost\": %d,%n", result.primCost);
        writer.printf("        \"operations_count\": %d,%n", result.primOperations);
        writer.printf("        \"execution_time_ms\": %.2f%n", result.primTime / 1000.0);
        writer.println("      },");
        
        // Kruskal results
        writer.println("      \"kruskal\": {");
        writeEdges(writer, result.kruskalEdges);
        writer.printf("        \"total_cost\": %d,%n", result.kruskalCost);
        writer.printf("        \"operations_count\": %d,%n", result.kruskalOperations);
        writer.printf("        \"execution_time_ms\": %.2f%n", result.kruskalTime / 1000.0);
        writer.println("      }");
        
        writer.print("    }");
        if (!isLast) writer.println(",");
        else writer.println();
    }
    
    /**
     * Write MST edges array
     */
    private static void writeEdges(PrintWriter writer, List<Edge> edges) {
        writer.println("        \"mst_edges\": [");
        
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            writer.printf("          {\"from\": \"%s\", \"to\": \"%s\", \"weight\": %d}",
                         edge.getFrom(), edge.getTo(), edge.getWeight());
            
            if (i < edges.size() - 1) writer.println(",");
            else writer.println();
        }
        
        writer.println("        ],");
    }
    
    /**
     * Data class to hold test results
     */
    public static class TestResult {
        public int graphId;
        public int vertices;
        public int edges;
        
        public List<Edge> primEdges;
        public int primCost;
        public int primOperations;
        public long primTime; // in microseconds for precision
        
        public List<Edge> kruskalEdges;
        public int kruskalCost;
        public int kruskalOperations;
        public long kruskalTime; // in microseconds for precision
        
        public TestResult(int graphId, int vertices, int edges,
                         List<Edge> primEdges, int primCost, int primOps, long primTime,
                         List<Edge> kruskalEdges, int kruskalCost, int kruskalOps, long kruskalTime) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.primEdges = new ArrayList<>(primEdges);
            this.primCost = primCost;
            this.primOperations = primOps;
            this.primTime = primTime;
            this.kruskalEdges = new ArrayList<>(kruskalEdges);
            this.kruskalCost = kruskalCost;
            this.kruskalOperations = kruskalOps;
            this.kruskalTime = kruskalTime;
        }
    }
}






