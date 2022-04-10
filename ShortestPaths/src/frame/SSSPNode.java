package frame;

public class SSSPNode
{
	//***this class helps implementing the lecture slides´ idea of realizing SSSP algorithms***
	
	
	//helps reconstructing the shortest paths, null if not set yet
	public Integer predecessor;
	//specifies paths length relative from a "source" to the node with "nodeNumber", null representing infinity (no path found yet)
	public Integer distance;
	//specifies nodeID of the node which is represented by this object
	public int nodeNumber;
	
	public SSSPNode(Integer predecessor, Integer weight, int nodeNumber) {
		this.predecessor = predecessor;
		this.distance = weight;
		this.nodeNumber = nodeNumber;
	}
}
