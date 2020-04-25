package graph.impl;

import graph.INode;

public class edge implements Comparable<edge>{
	
	INode node1;
	INode node2;
	int cost;
	
	public edge(INode node1, INode node2, int cost) {
		this.node1=node1;
		this.node2=node2;
		this.cost=cost;
	}
	
	public int compareTo(edge o) {
		return this.cost - o.cost;
	}

}
