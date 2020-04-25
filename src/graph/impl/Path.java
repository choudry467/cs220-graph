package graph.impl;

import graph.INode;

public class Path implements Comparable<Path>{
	 
	  INode destination;
	  int cost;

	  public Path(INode destination, int cost){
	    this.destination = destination;
	    this.cost = cost;
	  }

	  public int compareTo(Path other){ // This thing is dope. Like it's simple yet so elegant
	    return this.cost - other.cost;
	  }
}


