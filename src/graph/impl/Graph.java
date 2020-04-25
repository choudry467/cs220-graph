package graph.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra,
 * and Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph implements IGraph
{
	private Map<String, INode> nodes = new HashMap<>();
    
    /**
     * Return the {@link Node} with the given name.
     * 
     * If no {@link Node} with the given name exists, create
     * a new node with the given name and return it. Subsequent
     * calls to this method with the same name should
     * then return the node just created.
     * 
     * @param name
     * @return
     */
    public INode getOrCreateNode(String name) {
        if(nodes.containsKey(name))
        	return nodes.get(name);
        INode n = new Node(name);
        this.nodes.put(name,n);
        return n;
        
    }

    /**
     * Return true if the graph contains a node with the given name,
     * and false otherwise.
     * 
     * @param name
     * @return
     */
    public boolean containsNode(String name) {
    	if(nodes.containsKey(name))
        	return true;
    	return false;
    }

    /**
     * Return a collection of all of the nodes in the graph.
     * 
     * @return
     */
    public Collection<INode> getAllNodes() {
        return nodes.values();
    }
    
    /**
     * Perform a breadth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void breadthFirstSearch(String startNodeName, NodeVisitor v){
    	
        Set<INode> visited = new HashSet<>();
        Queue<INode> toVisit = new LinkedList<>();
        toVisit.offer(getOrCreateNode(startNodeName));
        while(!toVisit.isEmpty()) {
        	INode temp = toVisit.poll();
        	if (visited.contains(temp))
        		continue;
        	v.visit(temp);
        	visited.add(temp);
        	
        	for (INode temp2 : temp.getNeighbors()) {
        		if (!visited.contains(temp2))
        			toVisit.offer(temp2);
        	}
      
        }
    }

    /**
     * Perform a depth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void depthFirstSearch(String startNodeName, NodeVisitor v){ // Liked how the only difference between these 2 was of queue and stacks
    	
    	Set<INode> visited = new HashSet<>();
        Stack<INode> toVisit = new Stack<>();
        toVisit.push(getOrCreateNode(startNodeName));
        while(!toVisit.isEmpty()) {
        	INode temp = toVisit.pop();
        	if (visited.contains(temp))
        		continue;
        	v.visit(temp);
        	visited.add(temp);
        	
        	for (INode temp2 : temp.getNeighbors()) {
        		if (!visited.contains(temp2))
        			toVisit.push(temp2);
        	}
      
        }
    }

    /**
     * Perform Dijkstra's algorithm for computing the cost of the shortest path
     * to every node in the graph starting at the node with the given name.
     * Return a mapping from every node in the graph to the total minimum cost of reaching
     * that node from the given start node.
     * 
     * <b>Hint:</b> Creating a helper class called Path, which stores a destination
     * (String) and a cost (Integer), and making it implement Comparable, can be
     * helpful. Well, either than or repeated linear scans.
     * 
     * @param startName
     * @return
     */
    public Map<INode,Integer> dijkstra(String startName) {
        Map<INode,Integer> result = new HashMap<>();
        PriorityQueue<Path> toDo = new PriorityQueue<>();
        toDo.add(new Path(getOrCreateNode(startName),0));
        while ( result.size() < getAllNodes().size()) {
        	Path nextpath = toDo.poll();
        	INode Node1 = nextpath.destination;
        	if (result.containsKey(Node1))
        		continue;
        	int cost = nextpath.cost;
        	result.put(Node1, cost);
        	for(INode temp : Node1.getNeighbors()) {
        		toDo.add(new Path(temp, Node1.getWeight(temp)+cost));
        	}	
        }
        return result;
    }
    
    /**
     * Perform Prim-Jarnik's algorithm to compute a Minimum Spanning Tree (MST).
     * 
     * The MST is itself a graph containing the same nodes and a subset of the edges 
     * from the original graph.
     * 
     * @return
     */
    public IGraph primJarnik() { //This was by far my favorite implementation
        IGraph prim = new Graph();
        PriorityQueue<edge> toDo = new PriorityQueue<>(); //edge class with comparable
        INode start = (INode) this.getAllNodes().toArray()[0];//random first node
        for(INode temp : start.getNeighbors()) {
        	toDo.add(new edge(start,temp,start.getWeight(temp)));
        }
        while (prim.getAllNodes().size()< this.getAllNodes().size()) {
        	edge nextedge = toDo.poll();
        	INode Node1 = nextedge.node1;
        	INode Node2 = nextedge.node2;
        	if(prim.containsNode(Node1.getName()) && prim.containsNode(Node2.getName()))//so nodes that have a path are not included again
        		continue;
        	INode a = prim.getOrCreateNode(Node1.getName());
        	INode b = prim.getOrCreateNode(Node2.getName());
        	a.addUndirectedEdgeToNode(b,nextedge.cost);
        	for(INode temp : Node2.getNeighbors()) {
        		if (!temp.equals(Node1)) {
        		toDo.add(new edge(Node2,temp,Node2.getWeight(temp)));
        		}
        	}
        	
        }
        
        return prim;
        
    }
}