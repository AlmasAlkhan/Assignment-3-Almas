#!/usr/bin/env python3
"""
Graph Generator for Assignment 3
Generates test graphs of different sizes for MST algorithms testing
"""

import json
import random

def generate_graph(num_vertices, density=0.5, min_weight=1, max_weight=100):
    """
    Generate a random connected graph
    
    Args:
        num_vertices: Number of vertices in the graph
        density: Edge density (0.0 to 1.0)
        min_weight: Minimum edge weight
        max_weight: Maximum edge weight
    
    Returns:
        Dictionary with graph data
    """
    # Generate vertex names
    nodes = []
    for i in range(num_vertices):
        if i < 26:
            nodes.append(chr(65 + i))  # A-Z
        else:
            nodes.append(f"V{i}")
    
    edges = []
    edge_set = set()
    
    # First, create a spanning tree to ensure connectivity
    # Use randomized Prim's algorithm
    in_tree = [0]
    not_in_tree = list(range(1, num_vertices))
    
    while not_in_tree:
        # Pick random vertex from tree
        u_idx = random.choice(in_tree)
        # Pick random vertex not in tree
        v_idx = random.choice(not_in_tree)
        
        u = nodes[u_idx]
        v = nodes[v_idx]
        weight = random.randint(min_weight, max_weight)
        
        edges.append({"from": u, "to": v, "weight": weight})
        edge_set.add((min(u_idx, v_idx), max(u_idx, v_idx)))
        
        in_tree.append(v_idx)
        not_in_tree.remove(v_idx)
    
    # Add additional edges based on density
    max_edges = num_vertices * (num_vertices - 1) // 2
    current_edges = len(edges)
    target_edges = int(max_edges * density)
    
    attempts = 0
    max_attempts = max_edges * 2
    
    while len(edges) < target_edges and attempts < max_attempts:
        u_idx = random.randint(0, num_vertices - 1)
        v_idx = random.randint(0, num_vertices - 1)
        
        if u_idx == v_idx:
            attempts += 1
            continue
        
        edge_key = (min(u_idx, v_idx), max(u_idx, v_idx))
        
        if edge_key not in edge_set:
            u = nodes[u_idx]
            v = nodes[v_idx]
            weight = random.randint(min_weight, max_weight)
            
            edges.append({"from": u, "to": v, "weight": weight})
            edge_set.add(edge_key)
        
        attempts += 1
    
    return {
        "nodes": nodes,
        "edges": edges
    }

def create_input_file():
    """Create ass_3_input.json with multiple test graphs"""
    
    graphs = []
    
    # Small graphs (5 graphs, vertices < 30)
    print("Generating small graphs (5 graphs, vertices < 30)...")
    for i in range(5):
        num_vertices = random.randint(5, 29)
        density = random.uniform(0.3, 0.7)
        graph = generate_graph(num_vertices, density=density)
        graphs.append({
            "id": len(graphs) + 1,
            **graph
        })
        print(f"  Graph {len(graphs)}: {num_vertices} vertices, {len(graph['edges'])} edges")
    
    # Medium graphs (10 graphs, vertices < 300)
    print("\nGenerating medium graphs (10 graphs, vertices < 300)...")
    for i in range(10):
        num_vertices = random.randint(30, 299)
        density = random.uniform(0.15, 0.4)
        graph = generate_graph(num_vertices, density=density)
        graphs.append({
            "id": len(graphs) + 1,
            **graph
        })
        print(f"  Graph {len(graphs)}: {num_vertices} vertices, {len(graph['edges'])} edges")
    
    # Large graphs (10 graphs, vertices < 1000)
    print("\nGenerating large graphs (10 graphs, vertices < 1000)...")
    for i in range(10):
        num_vertices = random.randint(300, 999)
        density = random.uniform(0.05, 0.2)
        graph = generate_graph(num_vertices, density=density)
        graphs.append({
            "id": len(graphs) + 1,
            **graph
        })
        print(f"  Graph {len(graphs)}: {num_vertices} vertices, {len(graph['edges'])} edges")
    
    # Extra large graphs (5 graphs, vertices < 2000)
    print("\nGenerating extra large graphs (5 graphs, vertices < 2000)...")
    for i in range(5):
        num_vertices = random.randint(1000, 1999)
        density = random.uniform(0.02, 0.1)
        graph = generate_graph(num_vertices, density=density)
        graphs.append({
            "id": len(graphs) + 1,
            **graph
        })
        print(f"  Graph {len(graphs)}: {num_vertices} vertices, {len(graph['edges'])} edges")
    
    # Create output structure
    output = {
        "graphs": graphs
    }
    
    # Write to file
    with open('ass_3_input.json', 'w') as f:
        json.dump(output, f, indent=2)
    
    print(f"\n✅ Generated {len(graphs)} graphs")
    print(f"📁 Saved to: ass_3_input.json")
    
    # Print statistics summary
    print("\n📊 Summary by Category:")
    print("-" * 60)
    print(f"Small (< 30 vertices):      5 graphs")
    print(f"Medium (< 300 vertices):   10 graphs")
    print(f"Large (< 1000 vertices):   10 graphs")
    print(f"Extra Large (< 2000):       5 graphs")
    print(f"{'=' * 60}")
    print(f"TOTAL:                     30 graphs")

if __name__ == "__main__":
    random.seed(42)  # For reproducibility
    create_input_file()
