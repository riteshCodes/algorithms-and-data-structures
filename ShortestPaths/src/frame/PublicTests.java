package frame;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Hashtable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import lab.Algorithms;

class PublicTests
{

	static Graph g, g2, g3, g4, g5;
	
	protected static Duration timeout = Duration.ofSeconds(20);
	
	@BeforeAll
	public static void init() {
		g = TestFileOrganizer.createGraphFromFile("tests/public/testGraph1.txt");
		g2 = TestFileOrganizer.createGraphFromFile("tests/public/testGraph2.txt");
		g3 = TestFileOrganizer.createGraphFromFile("tests/public/testGraph3.txt");
		g4 = TestFileOrganizer.createGraphFromFile("tests/public/testGraph4.txt");
		g5 = TestFileOrganizer.createGraphFromFile("tests/public/negativeCycleTestGraph.txt");
	}
	
	@Test
	void testDijkstra()
	{
		System.out.println("\nStarting test: Dijkstra");
		assertTimeoutPreemptively(timeout, () -> {
			try {
				int nodeNumber = g.getNodeNumber();
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> expectedResults = TestFileOrganizer.textToResults("tests/public/dijkstraResultOfGraph1.txt");
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (Algorithms.Dijkstra(g, i), expectedResults.get(i));
				}
				
				nodeNumber = g2.getNodeNumber();
				expectedResults = TestFileOrganizer.textToResults("tests/public/dijkstraResultOfGraph2.txt");
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (Algorithms.Dijkstra(g2, i), expectedResults.get(i));
				}
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		});
		System.out.println("Finished test successfully: Djkstra");
	}
	
	
	@Test
	void testBellmanFord()
	{
		System.out.println("\nStarting test: Bellman-Ford");
		assertTimeoutPreemptively(timeout, () -> {
			try {
				int nodeNumber = g2.getNodeNumber();
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> expectedResults = TestFileOrganizer.textToResults("tests/public/bellmanFordResultOfGraph2.txt");
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (Algorithms.BellmanFord(g2, i), expectedResults.get(i));
				}
				
				nodeNumber = g4.getNodeNumber();
				expectedResults = TestFileOrganizer.textToResults("tests/public/bellmanFordResultOfGraph4.txt");
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (Algorithms.BellmanFord(g4, i), expectedResults.get(i));
				}
				
				assertEquals(null, Algorithms.BellmanFord(g5, 0));
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		});
		System.out.println("Finished test successfully: Bellman-Ford");
	}
	
	
	@Test
	void testJohnson()
	{
		System.out.println("\nStarting test: Johnson");
		assertTimeoutPreemptively(timeout, () -> {
			try {
				int nodeNumber = g2.getNodeNumber();
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> expectedResults = TestFileOrganizer.textToResults("tests/public/johnsohnResultOfGraph2.txt");
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> actualResults = Algorithms.Johnson(g2);
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (actualResults.get(i), expectedResults.get(i));
				}
				
				nodeNumber = g4.getNodeNumber();
				expectedResults = TestFileOrganizer.textToResults("tests/public/johnsohnResultOfGraph4.txt");
				actualResults = Algorithms.Johnson(g4);
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (actualResults.get(i), expectedResults.get(i));
				}
				
				
				assertEquals(null, Algorithms.Johnson(g5));
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		});
		System.out.println("Finished test successfully: Johnson");
	}
	
	
	@Test
	void testFloydWarshall()
	{
		System.out.println("\nStarting test: Floyd-Warshall");
		assertTimeoutPreemptively(timeout, () -> {
			try {
				int nodeNumber = g2.getNodeNumber();
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> expectedResults = TestFileOrganizer.textToResults("tests/public/floydWarshallResultOfGraph2.txt");
				Hashtable<Integer, Hashtable<Integer, SSSPNode>> actualResults = Algorithms.FloydWarshall(g2);
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (actualResults.get(i), expectedResults.get(i));
				}
				
				nodeNumber = g4.getNodeNumber();
				expectedResults = TestFileOrganizer.textToResults("tests/public/floydWarshallResultOfGraph4.txt");
				actualResults = Algorithms.FloydWarshall(g4);
				for (int i = 0; i<nodeNumber; i++) {
					compareSSSPdata (actualResults.get(i), expectedResults.get(i));
				}
				
				assertEquals(null, Algorithms.FloydWarshall(g5));
			} catch (Exception e) {
				System.out.println("Error: An exception was thrown: " + e.getMessage());
				throw e;
			}
		});
		System.out.println("Finished test successfully: Floyd-Warshall");
	}

	
	
	/**
	 * a helper function reducing code
	 */
	private static void compareSSSPdata(Hashtable<Integer, SSSPNode> actualData, Hashtable<Integer, SSSPNode> expectedData) {
		int nodeNumber = actualData.size();
		for (int j = 0; j<nodeNumber; j++) {
			SSSPNode actualNode = actualData.get(j);
			SSSPNode expectedNode = expectedData.get(j);
			assertEquals(actualNode.distance, expectedNode.distance);
			assertEquals(actualNode.predecessor, expectedNode.predecessor);
		
		}
	}
	
	

}

