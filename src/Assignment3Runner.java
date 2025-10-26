import java.io.*;
import java.util.*;

/**
 * Main class for Assignment 3 - MST Algorithm Comparison
 * Reads graphs from JSON, runs both algorithms, and writes JSON output
 * 
 * @author Almas
 */
public class Assignment3Runner {
    
    public static void main(String[] args) {
        String inputFile = "ass_3_input.json";
        String outputFile = "ass_3_output.json";
        
        if (args.length >= 1) {
            inputFile = args[0];
        }
        if (args.length >= 2) {
            outputFile = args[1];
        }
        
        try {
            System.err.println("Reading graphs from: " + inputFile);
            
            // Read all graphs from input file
            List<GraphReader.GraphData> graphDataList = GraphReader.readGraphsFromJSON(inputFile);
            
            System.err.println("Loaded " + graphDataList.size() + " graphs");
            System.err.println("Processing...");
            
            // Process each graph
            List<ResultWriter.TestResult> results = new ArrayList<>();
            
            for (int i = 0; i < graphDataList.size(); i++) {
                GraphReader.GraphData graphData = graphDataList.get(i);
                int graphId = graphData.id;
                Graph graph = graphData.graph;
                
                System.err.printf("Processing graph %d/%d (ID=%d, V=%d, E=%d)...\n", 
                                 i+1, graphDataList.size(), graphId, 
                                 graph.getVertexCount(), graph.getEdgeCount());
                
                // Check if graph is connected
                if (!graph.isConnected()) {
                    System.err.println("  Skipped (disconnected)");
                    continue;
                }
                
                // Run Prim's algorithm
                PrimAlgorithm prim = new PrimAlgorithm(graph);
                boolean primSuccess = prim.findMST();
                
                // Run Kruskal's algorithm
                KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
                boolean kruskalSuccess = kruskal.findMST();
                
                if (!primSuccess || !kruskalSuccess) {
                    System.err.println("  Failed");
                    continue;
                }
                
                System.err.printf("  Prim: cost=%d, time=%dms, ops=%d\n",
                                 prim.getTotalCost(), prim.getExecutionTime(), 
                                 prim.getOperationCount());
                System.err.printf("  Kruskal: cost=%d, time=%dms, ops=%d\n",
                                 kruskal.getTotalCost(), kruskal.getExecutionTime(),
                                 kruskal.getOperationCount());
                
                // Store result
                ResultWriter.TestResult result = new ResultWriter.TestResult(
                    graphId,
                    graph.getVertexCount(),
                    graph.getEdgeCount(),
                    prim.getMSTEdges(),
                    prim.getTotalCost(),
                    prim.getOperationCount(),
                    prim.getExecutionTime() * 1000,
                    kruskal.getMSTEdges(),
                    kruskal.getTotalCost(),
                    kruskal.getOperationCount(),
                    kruskal.getExecutionTime() * 1000
                );
                results.add(result);
            }
            
            System.err.println("\nWriting results to: " + outputFile);
            
            // Write JSON output to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                printJSONOutput(results, writer);
            }
            
            System.err.println("Done! Processed " + results.size() + " graphs");
            System.err.println("Output saved to: " + outputFile);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Print results in JSON format
     */
    private static void printJSONOutput(List<ResultWriter.TestResult> results, PrintWriter out) {
        out.println("{");
        out.println("  \"results\": [");
        
        for (int i = 0; i < results.size(); i++) {
            ResultWriter.TestResult result = results.get(i);
            
            out.println("    {");
            out.printf("      \"graph_id\": %d,%n", result.graphId);
            
            // Input stats
            out.println("      \"input_stats\": {");
            out.printf("        \"vertices\": %d,%n", result.vertices);
            out.printf("        \"edges\": %d%n", result.edges);
            out.println("      },");
            
            // Prim results
            out.println("      \"prim\": {");
            out.print("        \"mst_edges\": [");
            printEdges(result.primEdges, out);
            out.println("        ],");
            out.printf("        \"total_cost\": %d,%n", result.primCost);
            out.printf("        \"operations_count\": %d,%n", result.primOperations);
            out.printf("        \"execution_time_ms\": %.2f%n", result.primTime / 1000.0);
            out.println("      },");
            
            // Kruskal results
            out.println("      \"kruskal\": {");
            out.print("        \"mst_edges\": [");
            printEdges(result.kruskalEdges, out);
            out.println("        ],");
            out.printf("        \"total_cost\": %d,%n", result.kruskalCost);
            out.printf("        \"operations_count\": %d,%n", result.kruskalOperations);
            out.printf("        \"execution_time_ms\": %.2f%n", result.kruskalTime / 1000.0);
            out.println("      }");
            
            out.print("    }");
            if (i < results.size() - 1) {
                out.println(",");
            } else {
                out.println();
            }
        }
        
        out.println("  ]");
        out.println("}");
    }
    
    /**
     * Print edges array
     */
    private static void printEdges(List<Edge> edges, PrintWriter out) {
        if (edges.isEmpty()) {
            out.println();
            return;
        }
        
        out.println();
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);
            out.printf("          {\"from\": \"%s\", \"to\": \"%s\", \"weight\": %d}",
                      edge.getFrom(), edge.getTo(), edge.getWeight());
            
            if (i < edges.size() - 1) {
                out.println(",");
            } else {
                out.println();
            }
        }
    }
}
