package frame;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Graph
{
	private Hashtable<Integer,Hashtable<Integer, Integer>> edges;
	private Hashtable<Integer, ArrayList<Integer>> neighbours;
	private int nodeNumber;
	private int edgeNumber;
	
	/**
	 * 
	 * @param nodeNumber number of nodes of the graph; also specifying which node ID´s are valid (0<=ID<nodeNumber)
	 * @param edges specifies the edges of the graph with edge(a,b) = edges.get(a).get(b), second "get" call might lead to null iff edge does not exist
	 */
	public Graph(int nodeNumber,  Hashtable<Integer,Hashtable<Integer, Integer>> edges) {
		//you can pretty much ignore what happens in the constructor as long as you understand what the attributes stand for exactly and how to access them
		if (nodeNumber < 0)
			throw new RuntimeException();
		this.nodeNumber = nodeNumber;
		this.edges = new Hashtable<>();
		this.neighbours = new Hashtable<>();
		
		this.edgeNumber = 0;
			
		for (Enumeration<Integer> fromVertices = edges.keys(); fromVertices.hasMoreElements();) {
			int currentFrom = fromVertices.nextElement();
			if (currentFrom < 0 || currentFrom >= nodeNumber || this.edges.containsKey(currentFrom))
				throw new RuntimeException();
			
			this.edges.put(currentFrom, new Hashtable<>());
			this.neighbours.put(currentFrom, new ArrayList<>());
			
			for (Enumeration<Integer> toVertices = edges.get(currentFrom).keys(); toVertices.hasMoreElements();) {
				
				int currentTo = toVertices.nextElement();
				if (currentTo < 0 || currentTo >= nodeNumber || this.edges.get(currentFrom).containsKey(currentFrom))
					throw new RuntimeException();
				
				 
				this.edges.get(currentFrom).put(currentTo, edges.get(currentFrom).get(currentTo));
				this.neighbours.get(currentFrom).add(currentTo);
				//potentiell falsch, aber unwahrscheinlich
				edgeNumber++; 
			}
			
		}
		for (int i = 0; i<nodeNumber; i++) {
			if (!edges.containsKey(i)) {
				this.edges.put(i, new Hashtable<>());
				this.neighbours.put(i, new ArrayList<>());
			}
		}
	}
	
	
	
	/**
	 * 
	 * @return in constructor specified number of edges
	 */
	public int getEdgeNumber() {
		return edgeNumber;
	}
	
	/**
	 * 
	 * @return in constructor specified number of nodes
	 */
	public int getNodeNumber() {
		return nodeNumber;
	}
	
	/**
	 * 
	 * @param from ID of a node in the graph 
	 * @return all ID´s of nodes which can be reached from node with "from" ID
	 */
	public ArrayList<Integer> getNeighbours (int from) {
		return neighbours.get(from);
	}

	/**
	 * 
	 * @param from node ID
	 * @param to node ID
	 * @return edge(node with "from" ID, node with "to" ID), null if it does not exist
	 */
	public Integer getEdge(int from, int to) {
		return edges.get(from).get(to);
	}

}
