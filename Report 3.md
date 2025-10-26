Assignment 3: City Transportation Network Optimization (MST)
Analytical Report

**Student:** Almas  
**Assignment:** Minimum Spanning Tree Algorithms Implementation and Analysis  

---

## 1. Executive Summary

This report presents a comprehensive analysis of two fundamental Minimum Spanning Tree (MST) algorithms: Prim's and Kruskal's algorithms. The project successfully implements both algorithms in Java, tests them on 30 graphs of varying sizes (from 7 to 1960 vertices), and provides detailed performance comparisons. The implementation demonstrates 100% correctness with identical MST costs across both algorithms, validating the theoretical foundations of MST algorithms.

## 2. Project Overview

### 2.1 Problem Statement
The city administration needs to construct roads connecting all districts such that:
- Each district is reachable from any other district
- The total construction cost is minimized

This optimization problem is modeled as finding the Minimum Spanning Tree of a weighted undirected graph where:
- Vertices represent city districts
- Edges represent potential roads
- Edge weights represent construction costs

### 2.2 Objectives
1. Implement Prim's algorithm using priority queue
2. Implement Kruskal's algorithm using Union-Find data structure
3. Compare performance and efficiency of both algorithms
4. Analyze results across different graph sizes and densities
5. Validate correctness through automated testing

## 3. Implementation Details

### 3.1 Data Structures

#### Graph Representation
- **Adjacency List**: Used for efficient edge traversal
- **Edge Class**: Represents weighted edges with proper comparison methods
- **Vertex Set**: Maintains all vertices for connectivity checking

#### Key Classes
1. **Graph.java**: Main graph data structure with adjacency list representation
2. **Edge.java**: Weighted edge with comparison capabilities
3. **PrimAlgorithm.java**: Prim's MST implementation using priority queue
4. **KruskalAlgorithm.java**: Kruskal's MST implementation using Union-Find
5. **UnionFind.java**: Disjoint set data structure with path compression

### 3.2 Algorithm Implementations

#### Prim's Algorithm
```java
// Pseudocode implementation
1. Initialize priority queue with edges from start vertex
2. While MST incomplete and queue not empty:
   a. Extract minimum weight edge
   b. If destination not in MST:
      - Add edge to MST
      - Add all adjacent edges to priority queue
3. Return MST
```

**Time Complexity**: O(E log V)  
**Space Complexity**: O(V + E)  
**Key Features**: 
- Priority queue for efficient edge selection
- Greedy approach starting from arbitrary vertex
- Optimal for dense graphs

#### Kruskal's Algorithm
```java
// Pseudocode implementation
1. Sort all edges by weight
2. Initialize Union-Find data structure
3. For each edge in sorted order:
   a. If edge doesn't create cycle (Union-Find check):
      - Add edge to MST
      - Union the two components
4. Return MST
```

**Time Complexity**: O(E log E)  
**Space Complexity**: O(V + E)  
**Key Features**:
- Edge-based approach
- Union-Find for cycle detection
- Optimal for sparse graphs

### 3.3 Union-Find Optimization
Implemented with two key optimizations:
- **Path Compression**: Flattens tree structure during find operations
- **Union by Rank**: Attaches smaller tree under larger tree root

## 4. Testing Methodology

### 4.1 Test Data Generation
Created 30 test graphs using Python script with following distribution:
- **Small Graphs**: 5 graphs (< 30 vertices)
- **Medium Graphs**: 10 graphs (< 300 vertices)  
- **Large Graphs**: 10 graphs (< 1000 vertices)
- **Extra Large Graphs**: 5 graphs (< 2000 vertices)

### 4.2 Automated Testing
Implemented comprehensive test suite covering:

#### Correctness Tests
- ✅ Total cost verification (Prim vs Kruskal)
- ✅ MST edge count validation (V-1 edges)
- ✅ Acyclicity verification (no duplicate edges)
- ✅ Connectivity verification (single component)
- ✅ Disconnected graph handling

#### Performance Tests
- ✅ Execution time measurement
- ✅ Operation counting (comparisons, unions)
- ✅ Result reproducibility

#### Edge Cases
- ✅ Single vertex graphs
- ✅ Two vertex graphs
- ✅ Disconnected graphs

**Test Results**: All 35 automated tests passed ✅

## 5. Results and Analysis

### 5.1 Correctness Verification
- **Cost Match Rate**: 100% (30/30 graphs)
- **MST Validation**: All MSTs contain exactly V-1 edges
- **Algorithm Consistency**: Both algorithms produce identical minimum costs

### 5.2 Performance Analysis

#### Execution Time Comparison
| Graph Size | Average Prim Time | Average Kruskal Time | Winner |
|------------|------------------|---------------------|---------|
| Small (< 30) | 0.0 ms | 0.0 ms | Equal |
| Medium (< 300) | 1.2 ms | 1.4 ms | Prim |
| Large (< 1000) | 3.8 ms | 3.5 ms | Kruskal |
| Extra Large (< 2000) | 8.0 ms | 7.4 ms | Kruskal |

#### Operation Count Analysis
| Algorithm | Average Operations | Complexity |
|-----------|------------------|------------|
| Prim | 60,818 | O(E log V) |
| Kruskal | 431,508 | O(E log E) |

**Key Finding**: Prim uses significantly fewer operations due to priority queue efficiency.

### 5.3 Algorithm Performance Summary
- **Prim Faster**: 15 times (50.0%)
- **Kruskal Faster**: 2 times (6.7%)
- **Equal Performance**: 13 times (43.3%)

### 5.4 Scalability Analysis

#### Small Graphs (5-29 vertices)
- Both algorithms perform equally well
- Execution time < 1ms
- Prim: 45-348 operations, Kruskal: 65-766 operations

#### Medium Graphs (30-299 vertices)
- Prim shows slight advantage
- Execution time 0-2ms
- Prim: 616-15,196 operations, Kruskal: 1,580-81,440 operations

#### Large Graphs (300-999 vertices)
- Kruskal becomes competitive
- Execution time 0-9ms
- Prim: 20,157-150,528 operations, Kruskal: 112,615-1,147,061 operations

#### Extra Large Graphs (1000-1999 vertices)
- Kruskal shows better performance
- Execution time 4-23ms
- Prim: 93,371-238,051 operations, Kruskal: 586,469-1,787,277 operations

## 6. Theoretical vs Practical Analysis

### 6.1 Time Complexity Analysis

#### Prim's Algorithm
- **Theoretical**: O(E log V)
- **Practical**: Consistent performance across graph sizes
- **Best Case**: Dense graphs with adjacency matrix representation
- **Worst Case**: Sparse graphs with many priority queue operations

#### Kruskal's Algorithm
- **Theoretical**: O(E log E)
- **Practical**: Better scaling for very large graphs
- **Best Case**: Sparse graphs with edge list representation
- **Worst Case**: Dense graphs requiring many Union-Find operations

### 6.2 Space Complexity Analysis
Both algorithms use O(V + E) space:
- **Prim**: Priority queue + adjacency list
- **Kruskal**: Union-Find + sorted edge list

### 6.3 Graph Density Impact
- **Dense Graphs**: Prim performs better (fewer priority queue operations)
- **Sparse Graphs**: Kruskal performs better (fewer Union-Find operations)
- **Very Large Graphs**: Kruskal scales better due to simpler edge processing

## 7. Conclusions and Recommendations

### 7.1 Key Findings

1. **Correctness**: Both algorithms produce identical MST costs, validating implementation correctness.

2. **Performance Trade-offs**:
   - Prim excels on dense graphs and medium-sized problems
   - Kruskal scales better for very large, sparse graphs
   - Operation count favors Prim due to priority queue efficiency

3. **Scalability**: 
   - Small to medium graphs: Prim recommended
   - Large to very large graphs: Kruskal recommended
   - Graph density is the key determining factor

### 7.2 Algorithm Selection Guidelines

#### Choose Prim's Algorithm When:
- Graph is dense (high edge-to-vertex ratio)
- Graph size is small to medium (< 1000 vertices)
- Adjacency matrix representation is available
- Memory efficiency is important

#### Choose Kruskal's Algorithm When:
- Graph is sparse (low edge-to-vertex ratio)
- Graph size is very large (> 1000 vertices)
- Edge list representation is natural
- Parallel processing is desired (sorting can be parallelized)

### 7.3 Implementation Quality
- **Code Architecture**: Clean OOP design with proper encapsulation
- **Error Handling**: Graceful handling of disconnected graphs
- **Testing**: Comprehensive automated test suite
- **Documentation**: Well-documented code with clear comments

### 7.4 Future Improvements
1. **Parallel Implementation**: Kruskal's sorting phase can be parallelized
2. **Memory Optimization**: Implement adjacency matrix for dense graphs
3. **Dynamic Graphs**: Support for edge weight updates
4. **Visualization**: Interactive graph visualization tools



### 8.1 File Structure
```
Almas_assignment3/
├── src/
│   ├── Edge.java              # Edge class
│   ├── Graph.java             # Graph data structure
│   ├── PrimAlgorithm.java     # Prim's implementation
│   ├── KruskalAlgorithm.java  # Kruskal's implementation
│   ├── UnionFind.java         # Union-Find data structure
│   ├── GraphReader.java       # JSON input parser
│   ├── ResultWriter.java      # JSON output writer
│   ├── Assignment3Runner.java # Main execution class
│   └── MSTTest.java           # Automated tests
├── generate_graphs.py         # Test data generator
├── visualize_graphs.py       # Graph visualization
├── ass_3_input.json          # 30 test graphs
├── ass_3_output.json         # Algorithm results
└── README.md                  # Project documentation
```

### 8.2 Performance Metrics
- **Total Graphs Tested**: 30
- **Graph Size Range**: 7-1960 vertices
- **Edge Count Range**: 3-110,909 edges
- **Success Rate**: 100%
- **Test Coverage**: 35 automated tests



**Report prepared by:** Almas   
**Assignment:** Minimum Spanning Tree Implementation and Analysis

