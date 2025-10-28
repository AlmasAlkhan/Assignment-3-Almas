#!/usr/bin/env python3
"""
CSV Results Generator for Assignment 3
Converts JSON output to CSV format for analysis
"""

import json
import csv

def generate_csv_results():
    """Generate CSV file from JSON results"""
    
    # Read JSON results
    with open('ass_3_output.json', 'r') as f:
        data = json.load(f)
    
    # Create CSV file
    with open('assignment3_results.csv', 'w', newline='') as csvfile:
        fieldnames = [
            'graph_id', 'vertices', 'edges', 
            'prim_cost', 'prim_time_ms', 'prim_operations',
            'kruskal_cost', 'kruskal_time_ms', 'kruskal_operations',
            'cost_match', 'time_difference_ms', 'operations_difference'
        ]
        
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        
        for result in data['results']:
            # Calculate differences
            cost_match = result['prim']['total_cost'] == result['kruskal']['total_cost']
            time_diff = result['prim']['execution_time_ms'] - result['kruskal']['execution_time_ms']
            ops_diff = result['prim']['operations_count'] - result['kruskal']['operations_count']
            
            writer.writerow({
                'graph_id': result['graph_id'],
                'vertices': result['input_stats']['vertices'],
                'edges': result['input_stats']['edges'],
                'prim_cost': result['prim']['total_cost'],
                'prim_time_ms': result['prim']['execution_time_ms'],
                'prim_operations': result['prim']['operations_count'],
                'kruskal_cost': result['kruskal']['total_cost'],
                'kruskal_time_ms': result['kruskal']['execution_time_ms'],
                'kruskal_operations': result['kruskal']['operations_count'],
                'cost_match': cost_match,
                'time_difference_ms': round(time_diff, 2),
                'operations_difference': ops_diff
            })
    
    print("✅ CSV file generated: assignment3_results.csv")

if __name__ == "__main__":
    generate_csv_results()
