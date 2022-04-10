package frame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class TestFileOrganizer
{
	
	// This class is only needed to realize the tests!!!
	
	
	/**
	 * 
	 * @param fileName name of a .txt file in which a graph is specified in following format: <nodeNumber\n(from to weight\n)*> 
	 * @return graph object based on data in file
	 */
	public static Graph createGraphFromFile(String fileName) {
		Hashtable<Integer, Hashtable<Integer,Integer>> edges = new Hashtable<>();
		int nodeNumber = 1337;
		try {
		      File myObj = new File(fileName);
		      Scanner myReader = new Scanner(myObj);
		      nodeNumber = Integer.parseInt(myReader.nextLine());
		      for (int i = 0; i<nodeNumber; i++)
		    	  edges.put(i, new Hashtable<>());
		      
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        int from = Integer.parseInt(data.substring(0, data.indexOf(" ")));
		        data = data.substring(data.indexOf(" ")+1);
		        int to = Integer.parseInt(data.substring(0, data.indexOf(" ")));
		        data = data.substring(data.indexOf(" ")+1);
		        int weight = Integer.parseInt(data.substring(0));
		        edges.get(from).put(to, weight);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return new Graph(nodeNumber, edges);
	}
	
	/**
	 * this method was used to store test data
	 */
	public static String resultsToText (Hashtable<Integer, SSSPNode> resultData) {
		String text = "";
		
		for (int i = 0; i<resultData.size(); i++) {
			SSSPNode current = resultData.get(i);
			text = text + current.nodeNumber +  ": P" + current.predecessor + " D" + current.distance + "\n";
		}
		return text;
	}
	
	/**
	 * this method helps comparing the results of "solution" code with actual, in "lab" existing code
	 */
	public static Hashtable<Integer, Hashtable<Integer, SSSPNode>> textToResults (String fileName) {
		Hashtable<Integer, Hashtable<Integer, SSSPNode>> ssspData = new Hashtable<>();
		try {
		      File myObj = new File(fileName);
		      Scanner myReader = new Scanner(myObj);
		      int nodeNumber = Integer.parseInt(myReader.nextLine());
		      for (int i = 0; i<nodeNumber; i++) {
		        ssspData.put(Integer.parseInt(myReader.nextLine()), new Hashtable<>());
		        Hashtable<Integer, SSSPNode> currentSSSPdata = ssspData.get(i);
		        for (int j = 0; j<nodeNumber; j++) {
		        	String currentLine = myReader.nextLine();
		        	
		        	int nodeID = Integer.parseInt(currentLine.substring(0, currentLine.indexOf(":")));
		        	String predecessor = currentLine.substring(currentLine.indexOf("P")+1, currentLine.indexOf("D")-1);
		        	String distance = currentLine.substring(currentLine.indexOf("D")+1);
		        
		        	currentSSSPdata.put(j, new SSSPNode(predecessor.equals("null")? null : Integer.parseInt(predecessor), distance.equals("null")? null : Integer.parseInt(distance), nodeID));
		        	
		        }
		        myReader.nextLine();
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
		return ssspData;
	}
	
	/**
	 * this method was used to create test data
	 */
	public static void saveText(String text, String fileName) {
		BufferedWriter output = null;
        try {
            File file = new File(fileName);
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
          if ( output != null ) {
            try
			{
				output.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
          }
        }
	}
	

}
