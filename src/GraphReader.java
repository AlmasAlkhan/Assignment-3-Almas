import java.io.*;
import java.util.*;

/**
 * Class for reading graphs from JSON files
 * Parses the input format: { "graphs": [ { "id": 1, "nodes": [...], "edges": [...] } ] }
 * 
 * @author Almas
 */
public class GraphReader {
    
    /**
     * Read all graphs from JSON file
     * 
     * @param filename Path to JSON file
     * @return List of GraphData objects
     * @throws IOException if file not found or read error
     */
    public static List<GraphData> readGraphsFromJSON(String filename) throws IOException {
        List<GraphData> graphDataList = new ArrayList<>();
        
        // Read entire file
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line.trim());
            }
        }
        
        String jsonString = json.toString();
        
        // Find graphs array
        int graphsStart = jsonString.indexOf("\"graphs\"");
        if (graphsStart == -1) {
            throw new IOException("Invalid JSON: 'graphs' field not found");
        }
        
        int arrayStart = jsonString.indexOf("[", graphsStart);
        int arrayEnd = findMatchingBracket(jsonString, arrayStart);
        
        if (arrayStart == -1 || arrayEnd == -1) {
            throw new IOException("Invalid JSON: graphs array not found");
        }
        
        String graphsString = jsonString.substring(arrayStart + 1, arrayEnd);
        
        // Parse each graph object
        List<String> graphObjects = splitGraphObjects(graphsString);
        
        for (String graphObj : graphObjects) {
            GraphData graphData = parseGraph(graphObj);
            graphDataList.add(graphData);
        }
        
        return graphDataList;
    }
    
    /**
     * Parse a single graph object
     */
    private static GraphData parseGraph(String graphObj) {
        int id = extractInt(graphObj, "id");
        List<String> nodes = extractArray(graphObj, "nodes");
        
        Graph graph = new Graph();
        
        // Add all nodes
        for (String node : nodes) {
            graph.addVertex(node);
        }
        
        // Parse and add edges
        int edgesStart = graphObj.indexOf("\"edges\"");
        int edgesArrayStart = graphObj.indexOf("[", edgesStart);
        int edgesArrayEnd = findMatchingBracket(graphObj, edgesArrayStart);
        String edgesString = graphObj.substring(edgesArrayStart + 1, edgesArrayEnd);
        
        String[] edgeObjects = edgesString.split("\\},\\{");
        
        for (String edgeObj : edgeObjects) {
            edgeObj = edgeObj.replace("{", "").replace("}", "").trim();
            if (edgeObj.isEmpty()) continue;
            
            String from = extractValue(edgeObj, "from");
            String to = extractValue(edgeObj, "to");
            int weight = Integer.parseInt(extractValue(edgeObj, "weight"));
            
            graph.addEdge(from, to, weight);
        }
        
        return new GraphData(id, graph);
    }
    
    /**
     * Split graphs array into individual graph objects
     */
    private static List<String> splitGraphObjects(String graphsString) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = 0;
        
        for (int i = 0; i < graphsString.length(); i++) {
            char c = graphsString.charAt(i);
            
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    objects.add(graphsString.substring(start, i + 1));
                }
            }
        }
        
        return objects;
    }
    
    /**
     * Find matching closing bracket
     */
    private static int findMatchingBracket(String str, int openPos) {
        if (openPos == -1 || openPos >= str.length()) return -1;
        
        char openBracket = str.charAt(openPos);
        char closeBracket = (openBracket == '[') ? ']' : '}';
        
        int depth = 1;
        for (int i = openPos + 1; i < str.length(); i++) {
            if (str.charAt(i) == openBracket) depth++;
            else if (str.charAt(i) == closeBracket) {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }
    
    /**
     * Extract array of strings from JSON
     */
    private static List<String> extractArray(String json, String field) {
        List<String> result = new ArrayList<>();
        String pattern = "\"" + field + "\"";
        int fieldStart = json.indexOf(pattern);
        if (fieldStart == -1) return result;
        
        int arrayStart = json.indexOf("[", fieldStart);
        int arrayEnd = findMatchingBracket(json, arrayStart);
        
        String arrayContent = json.substring(arrayStart + 1, arrayEnd);
        String[] items = arrayContent.split(",");
        
        for (String item : items) {
            item = item.trim().replace("\"", "");
            if (!item.isEmpty()) {
                result.add(item);
            }
        }
        
        return result;
    }
    
    /**
     * Extract integer value from JSON
     */
    private static int extractInt(String json, String field) {
        String value = extractValue(json, field);
        return Integer.parseInt(value);
    }
    
    /**
     * Extract string value from JSON
     */
    private static String extractValue(String json, String field) {
        String pattern = "\"" + field + "\"";
        int fieldStart = json.indexOf(pattern);
        if (fieldStart == -1) return "";
        
        int valueStart = json.indexOf(":", fieldStart) + 1;
        int valueEnd = json.indexOf(",", valueStart);
        if (valueEnd == -1 || valueEnd > json.length()) {
            valueEnd = json.indexOf("}", valueStart);
        }
        if (valueEnd == -1) {
            valueEnd = json.length();
        }
        
        String value = json.substring(valueStart, valueEnd).trim();
        value = value.replace("\"", "").trim();
        
        return value;
    }
    
    /**
     * Data class to hold graph with its ID
     */
    public static class GraphData {
        public final int id;
        public final Graph graph;
        
        public GraphData(int id, Graph graph) {
            this.id = id;
            this.graph = graph;
        }
    }
}






