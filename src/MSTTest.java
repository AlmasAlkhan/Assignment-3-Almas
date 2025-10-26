/**
 * Automated tests for MST algorithms (Prim and Kruskal)
 * Tests correctness, connectivity, and consistency
 * 
 * @author Almas
 */
public class MSTTest {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("MST Algorithm Automated Tests");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Run all test categories
        testCorrectness();
        testConnectivity();
        testPerformance();
        testEdgeCases();
        
        // Print summary
        System.out.println();
        System.out.println("=".repeat(70));
        System.out.println("Test Summary:");
        System.out.println("-".repeat(70));
        System.out.printf("Tests run: %d%n", testsRun);
        System.out.printf("Tests passed: %d ✅%n", testsPassed);
        System.out.printf("Tests failed: %d ❌%n", testsFailed);
        System.out.println("=".repeat(70));
        
        if (testsFailed == 0) {
            System.out.println("✅ All tests passed!");
        } else {
            System.out.println("❌ Some tests failed!");
            System.exit(1);
        }
    }
    
    /**
     * Test correctness of MST algorithms
     */
    private static void testCorrectness() {
        System.out.println("📝 Correctness Tests:");
        System.out.println("-".repeat(70));
        
        // Test 1: Simple triangle graph
        Graph g1 = createTriangleGraph();
        testGraph(g1, "Triangle Graph", 3, 2);
        
        // Test 2: Square graph
        Graph g2 = createSquareGraph();
        testGraph(g2, "Square Graph", 4, 3);
        
        // Test 3: Pentagon graph
        Graph g3 = createPentagonGraph();
        testGraph(g3, "Pentagon Graph", 5, 4);
        
        System.out.println();
    }
    
    /**
     * Test connectivity handling
     */
    private static void testConnectivity() {
        System.out.println("🔗 Connectivity Tests:");
        System.out.println("-".repeat(70));
        
        // Test: Disconnected graph
        Graph disconnected = new Graph();
        disconnected.addEdge("A", "B", 1);
        disconnected.addEdge("C", "D", 1);
        
        PrimAlgorithm prim = new PrimAlgorithm(disconnected);
        KruskalAlgorithm kruskal = new KruskalAlgorithm(disconnected);
        
        boolean primResult = prim.findMST();
        boolean kruskalResult = kruskal.findMST();
        
        // Both should fail on disconnected graph
        testResult("Disconnected graph detection",
                  !primResult && !kruskalResult);
        
        System.out.println();
    }
    
    /**
     * Test performance and consistency
     */
    private static void testPerformance() {
        System.out.println("⚡ Performance Tests:");
        System.out.println("-".repeat(70));
        
        // Test: Execution time is measured
        Graph g = createLargeGraph(100);
        PrimAlgorithm prim = new PrimAlgorithm(g);
        KruskalAlgorithm kruskal = new KruskalAlgorithm(g);
        
        prim.findMST();
        kruskal.findMST();
        
        testResult("Prim execution time measured",
                  prim.getExecutionTime() >= 0);
        testResult("Kruskal execution time measured",
                  kruskal.getExecutionTime() >= 0);
        testResult("Prim operations counted",
                  prim.getOperationCount() > 0);
        testResult("Kruskal operations counted",
                  kruskal.getOperationCount() > 0);
        
        System.out.println();
    }
    
    /**
     * Test edge cases
     */
    private static void testEdgeCases() {
        System.out.println("🔍 Edge Case Tests:");
        System.out.println("-".repeat(70));
        
        // Test: Single vertex
        Graph single = new Graph();
        single.addVertex("A");
        testGraph(single, "Single Vertex", 1, 0);
        
        // Test: Two vertices
        Graph two = new Graph();
        two.addEdge("A", "B", 5);
        testGraph(two, "Two Vertices", 2, 1);
        
        System.out.println();
    }
    
    /**
     * Test a graph with both algorithms
     */
    private static void testGraph(Graph graph, String name, int expectedVertices, int expectedEdges) {
        PrimAlgorithm prim = new PrimAlgorithm(graph);
        KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
        
        boolean primSuccess = prim.findMST();
        boolean kruskalSuccess = kruskal.findMST();
        
        // Test 1: Both algorithms succeed
        testResult(name + ": Both algorithms succeed",
                  primSuccess && kruskalSuccess);
        
        // Test 2: Same total cost
        testResult(name + ": Same total cost",
                  prim.getTotalCost() == kruskal.getTotalCost());
        
        // Test 3: Correct number of edges (V-1)
        testResult(name + ": Prim edges = V-1",
                  prim.getMSTEdges().size() == expectedEdges);
        testResult(name + ": Kruskal edges = V-1",
                  kruskal.getMSTEdges().size() == expectedEdges);
        
        // Test 4: No duplicate edges
        testResult(name + ": Prim no duplicates",
                  hasNoDuplicates(prim.getMSTEdges()));
        testResult(name + ": Kruskal no duplicates",
                  hasNoDuplicates(kruskal.getMSTEdges()));
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
     * Check for duplicate edges
     */
    private static boolean hasNoDuplicates(java.util.List<Edge> edges) {
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Edge edge : edges) {
            String key = edge.getFrom() + "-" + edge.getTo();
            String reverseKey = edge.getTo() + "-" + edge.getFrom();
            if (seen.contains(key) || seen.contains(reverseKey)) {
                return false;
            }
            seen.add(key);
        }
        return true;
    }
    
    /**
     * Create test graphs
     */
    private static Graph createTriangleGraph() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        g.addEdge("B", "C", 2);
        g.addEdge("C", "A", 3);
        return g;
    }
    
    private static Graph createSquareGraph() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        g.addEdge("B", "C", 2);
        g.addEdge("C", "D", 3);
        g.addEdge("D", "A", 4);
        g.addEdge("A", "C", 5);
        return g;
    }
    
    private static Graph createPentagonGraph() {
        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        g.addEdge("B", "C", 2);
        g.addEdge("C", "D", 3);
        g.addEdge("D", "E", 4);
        g.addEdge("E", "A", 5);
        return g;
    }
    
    private static Graph createLargeGraph(int size) {
        Graph g = new Graph();
        for (int i = 0; i < size - 1; i++) {
            String from = "V" + i;
            String to = "V" + (i + 1);
            g.addEdge(from, to, i + 1);
        }
        return g;
    }
}




