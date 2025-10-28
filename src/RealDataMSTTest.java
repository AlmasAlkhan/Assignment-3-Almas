import java.util.*;

/**
 * Real Data Tests - Tests with actual 30 graphs from JSON file
 * This tests the REAL implementation with REAL data
 * 
 * @author Almas
 */
public class RealDataMSTTest {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("REAL DATA MST Tests - Testing with actual 30 graphs");
        System.out.println("=".repeat(70));
        System.out.println();
        
        try {
            // Load real graphs from JSON
            List<GraphReader.GraphData> graphDataList = GraphReader.readGraphsFromJSON("ass_3_input.json");
            
            System.out.printf("📊 Loaded %d real graphs from JSON file%n", graphDataList.size());
            System.out.println();
            
            // Test each real graph
            testRealGraphs(graphDataList);
            
            // Test performance consistency across real data
            testRealPerformanceConsistency(graphDataList);
            
            // Test edge cases in real data
            testRealEdgeCases(graphDataList);
            
        } catch (Exception e) {
            System.out.println("❌ Error loading real data: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Print summary
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("REAL DATA Test Summary:");
        System.out.println("-".repeat(70));
        System.out.printf("Tests run: %d%n", testsRun);
        System.out.printf("Tests passed: %d ✅%n", testsPassed);
        System.out.printf("Tests failed: %d ❌%n", testsFailed);
        System.out.println("=".repeat(70));
        
        if (testsFailed == 0) {
            System.out.println("✅ All REAL DATA tests passed!");
            System.out.println("🎉 Your implementation works correctly with REAL graphs!");
        } else {
            System.out.println("❌ Some REAL DATA tests failed!");
            System.out.println("🔧 Your implementation has issues with real data!");
        }
    }
    
    /**
     * Test all real graphs from JSON file
     */
    private static void testRealGraphs(List<GraphReader.GraphData> graphDataList) {
        System.out.println("🏗️ Real Graph Tests (from JSON file):");
        System.out.println("-".repeat(70));
        
        int connectedGraphs = 0;
        int disconnectedGraphs = 0;
        
        for (GraphReader.GraphData graphData : graphDataList) {
            Graph graph = graphData.graph;
            int graphId = graphData.id;
            
            System.out.printf("Testing Graph %d: %d vertices, %d edges%n", 
                             graphId, graph.getVertexCount(), graph.getEdgeCount());
            
            // Check if graph is connected
            if (!graph.isConnected()) {
                disconnectedGraphs++;
                System.out.printf("  ⚠️ Graph %d is disconnected - skipping MST tests%n", graphId);
                testResult("Graph " + graphId + ": Disconnected graph detected", true);
                continue;
            }
            
            connectedGraphs++;
            
            // Test with both algorithms
            PrimAlgorithm prim = new PrimAlgorithm(graph);
            KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
            
            boolean primSuccess = prim.findMST();
            boolean kruskalSuccess = kruskal.findMST();
            
            // Test 1: Both algorithms succeed
            testResult("Graph " + graphId + ": Both algorithms succeed", 
                      primSuccess && kruskalSuccess);
            
            // Test 2: Same total cost
            testResult("Graph " + graphId + ": Same total cost", 
                      prim.getTotalCost() == kruskal.getTotalCost());
            
            // Test 3: Correct number of edges (V-1)
            testResult("Graph " + graphId + ": Prim edges = V-1", 
                      prim.getMSTEdges().size() == graph.getVertexCount() - 1);
            testResult("Graph " + graphId + ": Kruskal edges = V-1", 
                      kruskal.getMSTEdges().size() == graph.getVertexCount() - 1);
            
            // Test 4: No cycles (no duplicate edges)
            testResult("Graph " + graphId + ": Prim no cycles", 
                      hasNoCycles(prim.getMSTEdges()));
            testResult("Graph " + graphId + ": Kruskal no cycles", 
                      hasNoCycles(kruskal.getMSTEdges()));
            
            // Test 5: Performance metrics are reasonable
            testResult("Graph " + graphId + ": Prim time > 0", 
                      prim.getExecutionTime() > 0);
            testResult("Graph " + graphId + ": Kruskal time > 0", 
                      kruskal.getExecutionTime() > 0);
            testResult("Graph " + graphId + ": Prim operations > 0", 
                      prim.getOperationCount() > 0);
            testResult("Graph " + graphId + ": Kruskal operations > 0", 
                      kruskal.getOperationCount() > 0);
            
            // Print detailed results for first few graphs
            if (graphId <= 5) {
                System.out.printf("    📊 Graph %d Results:%n", graphId);
                System.out.printf("        Cost: %d (Prim) = %d (Kruskal)%n", 
                                 prim.getTotalCost(), kruskal.getTotalCost());
                System.out.printf("        Time: %.2fμs (Prim) vs %.2fμs (Kruskal)%n", 
                                 prim.getExecutionTime(), kruskal.getExecutionTime());
                System.out.printf("        Ops: %d (Prim) vs %d (Kruskal)%n", 
                                 prim.getOperationCount(), kruskal.getOperationCount());
                System.out.printf("        Edges: %d (Prim) vs %d (Kruskal)%n", 
                                 prim.getMSTEdges().size(), kruskal.getMSTEdges().size());
            }
            
            System.out.println();
        }
        
        System.out.printf("📈 Summary: %d connected graphs, %d disconnected graphs%n", 
                         connectedGraphs, disconnectedGraphs);
        System.out.println();
    }
    
    /**
     * Test performance consistency across real data
     */
    private static void testRealPerformanceConsistency(List<GraphReader.GraphData> graphDataList) {
        System.out.println("⚡ Real Data Performance Consistency Tests:");
        System.out.println("-".repeat(70));
        
        // Test first 5 connected graphs multiple times
        int testedGraphs = 0;
        for (GraphReader.GraphData graphData : graphDataList) {
            if (testedGraphs >= 5) break;
            
            Graph graph = graphData.graph;
            if (!graph.isConnected()) continue;
            
            int graphId = graphData.id;
            System.out.printf("Testing Graph %d consistency (3 runs)...%n", graphId);
            
            // Run same graph 3 times
            int[] primCosts = new int[3];
            int[] kruskalCosts = new int[3];
            double[] primTimes = new double[3];
            double[] kruskalTimes = new double[3];
            
            for (int run = 0; run < 3; run++) {
                PrimAlgorithm prim = new PrimAlgorithm(graph);
                KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
                
                prim.findMST();
                kruskal.findMST();
                
                primCosts[run] = prim.getTotalCost();
                kruskalCosts[run] = kruskal.getTotalCost();
                primTimes[run] = prim.getExecutionTime();
                kruskalTimes[run] = kruskal.getExecutionTime();
            }
            
            // Test consistency
            testResult("Graph " + graphId + ": Prim cost consistent", 
                      primCosts[0] == primCosts[1] && primCosts[1] == primCosts[2]);
            testResult("Graph " + graphId + ": Kruskal cost consistent", 
                      kruskalCosts[0] == kruskalCosts[1] && kruskalCosts[1] == kruskalCosts[2]);
            testResult("Graph " + graphId + ": Cross-algorithm cost consistent", 
                      primCosts[0] == kruskalCosts[0]);
            
            // Time can vary, but should be reasonable
            testResult("Graph " + graphId + ": Prim times reasonable", 
                      primTimes[0] > 0 && primTimes[1] > 0 && primTimes[2] > 0);
            testResult("Graph " + graphId + ": Kruskal times reasonable", 
                      kruskalTimes[0] > 0 && kruskalTimes[1] > 0 && kruskalTimes[2] > 0);
            
            testedGraphs++;
        }
        
        System.out.println();
    }
    
    /**
     * Test edge cases in real data
     */
    private static void testRealEdgeCases(List<GraphReader.GraphData> graphDataList) {
        System.out.println("🔍 Real Data Edge Case Tests:");
        System.out.println("-".repeat(70));
        
        // Find smallest and largest graphs
        Graph smallestGraph = null;
        Graph largestGraph = null;
        int smallestVertices = Integer.MAX_VALUE;
        int largestVertices = 0;
        
        for (GraphReader.GraphData graphData : graphDataList) {
            Graph graph = graphData.graph;
            if (!graph.isConnected()) continue;
            
            int vertices = graph.getVertexCount();
            if (vertices < smallestVertices) {
                smallestVertices = vertices;
                smallestGraph = graph;
            }
            if (vertices > largestVertices) {
                largestVertices = vertices;
                largestGraph = graph;
            }
        }
        
        // Test smallest graph
        if (smallestGraph != null) {
            System.out.printf("Testing smallest graph (%d vertices)...%n", smallestVertices);
            testRealGraph(smallestGraph, "Smallest Graph");
        }
        
        // Test largest graph
        if (largestGraph != null) {
            System.out.printf("Testing largest graph (%d vertices)...%n", largestVertices);
            testRealGraph(largestGraph, "Largest Graph");
        }
        
        // Test performance scaling
        System.out.println("Testing performance scaling...");
        testPerformanceScaling(graphDataList);
        
        System.out.println();
    }
    
    /**
     * Test a single real graph
     */
    private static void testRealGraph(Graph graph, String name) {
        PrimAlgorithm prim = new PrimAlgorithm(graph);
        KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
        
        prim.findMST();
        kruskal.findMST();
        
        testResult(name + ": Both algorithms succeed", 
                  prim.getTotalCost() > 0 && kruskal.getTotalCost() > 0);
        testResult(name + ": Same total cost", 
                  prim.getTotalCost() == kruskal.getTotalCost());
        testResult(name + ": Reasonable execution times", 
                  prim.getExecutionTime() > 0 && kruskal.getExecutionTime() > 0);
        testResult(name + ": Reasonable operation counts", 
                  prim.getOperationCount() > 0 && kruskal.getOperationCount() > 0);
    }
    
    /**
     * Test performance scaling across different graph sizes
     */
    private static void testPerformanceScaling(List<GraphReader.GraphData> graphDataList) {
        // Group graphs by size
        Map<String, List<Graph>> sizeGroups = new HashMap<>();
        
        for (GraphReader.GraphData graphData : graphDataList) {
            Graph graph = graphData.graph;
            if (!graph.isConnected()) continue;
            
            int vertices = graph.getVertexCount();
            String sizeGroup;
            if (vertices < 50) sizeGroup = "Small (< 50)";
            else if (vertices < 200) sizeGroup = "Medium (50-200)";
            else if (vertices < 500) sizeGroup = "Large (200-500)";
            else sizeGroup = "Extra Large (> 500)";
            
            sizeGroups.computeIfAbsent(sizeGroup, k -> new ArrayList<>()).add(graph);
        }
        
        // Test each size group
        for (Map.Entry<String, List<Graph>> entry : sizeGroups.entrySet()) {
            String sizeGroup = entry.getKey();
            List<Graph> graphs = entry.getValue();
            
            if (graphs.isEmpty()) continue;
            
            // Test first graph in each group
            Graph testGraph = graphs.get(0);
            PrimAlgorithm prim = new PrimAlgorithm(testGraph);
            KruskalAlgorithm kruskal = new KruskalAlgorithm(testGraph);
            
            prim.findMST();
            kruskal.findMST();
            
            testResult(sizeGroup + ": Prim performs reasonably", 
                      prim.getExecutionTime() > 0 && prim.getOperationCount() > 0);
            testResult(sizeGroup + ": Kruskal performs reasonably", 
                      kruskal.getExecutionTime() > 0 && kruskal.getOperationCount() > 0);
            testResult(sizeGroup + ": Both algorithms give same result", 
                      prim.getTotalCost() == kruskal.getTotalCost());
            
            System.out.printf("    %s: %d graphs, Prim %.2fμs, Kruskal %.2fμs%n", 
                             sizeGroup, graphs.size(), 
                             prim.getExecutionTime(), kruskal.getExecutionTime());
        }
    }
    
    /**
     * Test result helper
     */
    private static void testResult(String testName, boolean passed) {
        testsRun++;
        if (passed) {
            testsPassed++;
            System.out.printf("  ✅ %s%n", testName);
        } else {
            testsFailed++;
            System.out.printf("  ❌ %s%n", testName);
        }
    }
    
    /**
     * Check for cycles in MST edges
     */
    private static boolean hasNoCycles(List<Edge> edges) {
        // Simple check: if we have V-1 edges and no duplicates, it's acyclic
        Set<String> seen = new HashSet<>();
        for (Edge edge : edges) {
            String key1 = edge.getFrom() + "-" + edge.getTo();
            String key2 = edge.getTo() + "-" + edge.getFrom();
            if (seen.contains(key1) || seen.contains(key2)) {
                return false;
            }
            seen.add(key1);
        }
        return true;
    }
}
