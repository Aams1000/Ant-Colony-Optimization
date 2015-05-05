        /****************************************
         *                                      *
         *            Main ACO class            *
         *         		Adela Yang				*
         *  	 Edited by Andrew Miller-Smith  *
         *                                      *
         ****************************************/

        /****

        Description:    The final version of the project contains a script for running multiple days worth of tests. I have commented out the final
        				code and included an earlier version that allows the user to run the algorithms on the command line. The arguments for running
        				my ElitistAnt algorithm are as follows:

        				File name (I recommend eil51.tsp or eil101.tsp)
						Algorithm  (e for my ElitistAnt algorithm)
						Ants 	   (Number of ants)
						Iterations 	(Number of iterations)
						Alpha 		(The degree of influence of the pheromone component. Recommended = 1.0)
						Beta		(The degree of influence of the elitist component. Recommended = 2 - 5)
						Roh			(The pheromone evaporation factor. Recommended = 0.1)
						Elitism  	(The elitism factor. Recommended = number of ants)
						Optimum		(The known optimum for the problem. For eil51.tsp = 426, eil101.tsp = 629)

						An example of a command line call:

						java ACO eil51.tsp e 10 10 1 3.5 0.1 10 426

						Play around with the parameters and see what happens to the best tour length the program can find!

						- Andrew
						

        */

import java.io.*;
import java.util.*;

public class ACO{

	//an instance of the elitism system
	private static ElitistAnt elite;

	//file Variables
	private static BufferedReader reader = null;
	private static File file;

	//a vector that contains the info of all cities
	private static Vector<City> place = new Vector<City>();

	//shares variables
	private static String algorithm;
	private static int ants;
	private static int iterations;
	private static double alpha;
	private static double beta;
	private static double roh;
	private static double optimum;

	//elitist ant variables
	private static double elitism;

	//any colony variables
	private static double epsilon;
	private static double tao;
	private static double qProb;


	//Main method
	public static void main(String[] args) {
		
		file 		= new File(args[0]);
		algorithm 	= args[1];  					//either e or a
		ants 		= Integer.parseInt(args[2]);	//integer
		iterations 	= Integer.parseInt(args[3]);	//integer
		alpha 		= Double.parseDouble(args[4]);	//double
		beta		= Double.parseDouble(args[5]);	//double
		roh			= Double.parseDouble(args[6]);	//double

		if(algorithm.equals("e")){
			elitism = Double.parseDouble(args[7]);	//double
			optimum = Double.parseDouble(args[8]);

		}
		else if(algorithm.equals("a")){
			epsilon = Double.parseDouble(args[7]);	//double
			tao 	= Double.parseDouble(args[8]);	//double
			qProb 	= Double.parseDouble(args[9]);	//double
		}
		else{ 
			System.out.println("A valid algorithm name is needed.  e/a");
			System.exit(0);
		}

		readFile(file);

		elite.elitistAnt(place, ants, elitism, alpha, beta, roh, iterations, args[0], optimum);

		
				
	}


	//reads file and stores the cities coordinates and identifier
	public static void readFile(File f) 
	{	
		try 
		{
			reader = new BufferedReader(new FileReader(f));
			String line;
			boolean tempC = false; //true if the line read is coordinates

			while (!(line = reader.readLine()).equals("EOF")) 
			{
				if (line.equals("NODE_COORD_SECTION")) 
				{
					tempC = true;
					continue;
				}
				if(tempC)
				{
					String tempL = line.trim();
					String[] splitStr = tempL.split("\\s+");

					if(splitStr.length != 3)
					{
						System.out.println("There are more than 2 coordinates.");
						System.exit(0);
					}
					
					int identifier = Integer.parseInt(splitStr[0]); 
					double xCoord =	Double.parseDouble(splitStr[1]);
					double yCoord =	Double.parseDouble(splitStr[2]);

					City town = new City(xCoord, yCoord, identifier);
					place.add(town);
				}
			}

			reader.close();
		} 
		catch (Exception e) 
		{
			System.err.format("Exception occurred trying to read '%s'.", f);
			e.printStackTrace();	
		}
	}



}





/*

import java.io.*;
import java.util.*;

public class ACO {

	//an instance of the elitism system
	private static ElitistAnt elite;
	private static ACS antCS;

	//file Variables
	private static BufferedReader reader = null;
	private static File file;
	private static String fileName;

	//a vector that contains the info of all cities
	private static Vector<City> place = new Vector<City>();

	//shares variables
	private static String algorithm;
	private static int ants;
	private static int iterations;
	private static double alpha;
	private static double beta;
	private static double rho;
	private static double optimum;

	//elitist ant variables
	private static double elitism;

	//any colony variables
	private static double epsilon;
	private static double qProb;

	//contains the output of the algorithms
	private static double[] output = new double[2]; 
	//0: best length 
	//1: time in seconds

	//main method
	public static void main(String[] args) 
	{
		if(args.length != 0){
			runCommand(args);
		}
		else{
			runTestElitism();
			//runTestACS();
		}
	}

	public static void runTestACS(){
		PrintStream out = System.out;
        PrintStream std = System.out;
	    try
	    {
	        FileOutputStream fos = new FileOutputStream("resultsElitism.csv", true); 
	        out = new PrintStream(fos);
   		 	System.setOut(out);     
	    }
	    catch (FileNotFoundException ex)  
	    {
	    	System.out.println(ex.getMessage());
	    }

	    int runs = 10;

	    //shares variables
		ants = 20;
		iterations = 50;
		alpha = 1;
		beta = 3.5;
		rho = 0.1;

		for(int i=0; i<4; i++){
	    	switch (i){
	    		case 0: 
	    			fileName = "u2152.tsp";
	    			file = new File(fileName);
	    			optimum = 64253;
	    			break;
	    		case 1:
	    			fileName = "fl3795.tsp";
	    			file = new File(fileName);
	    			optimum = 28772;
	    			break;
	    		case 2:
	    			fileName = "fnl4461.tsp";
	    			file = new File(fileName);
	    			optimum = 182566;
	    			break;
	    		case 3:
	    			fileName = "rl5934.tsp";
	    			file = new File(fileName);
	    			optimum = 556045;
	    			break;
	    		default:
	    			System.out.printf("Error in file name.");
	    			break;
	    	}
	    	System.out.printf("\nFile Name: %s\n", fileName);
	    	readFile(file);

	    	for(int j=0; j<3; j++){
	    		switch (j){
	    			case 0:
	    				epsilon = 0.05;
	    				break;
	    			case 1:
	    				epsilon = 0.1;
	    				break;
	    			case 2:
	    				epsilon = 0.2;
	    				break;
	    			default:
	    				System.out.printf("Error in epsilon.");
	    				break;
	    		}
	    		System.out.printf("\nEpsilon: %f\n", epsilon);

	    		for(int k=0; k<2; k++){
	    			switch (k){
	    				case 0:
	    					qProb = 0.5;
	    					break;
	    				case 1:
	    					qProb = 0.9;
	    					break;
	    				default:
	    					System.out.println("Error in q probability.");
	    					break;
	    			}
	    			System.out.printf("\nQ Probability: %f\n", qProb);

	    			double avgLength = 0;
	    			double avgTime = 0;
	    			for(int l=0; l<runs; l++){
	    				antCS.acs(fileName, optimum, place, ants, iterations, alpha, beta, rho, epsilon, qProb);
	    				avgLength += antCS.getBestLength();
						avgTime += (double)antCS.getTime();
						System.setOut(std); 
						float pComplete = (((float)l+1)/(float)runs)*100;
						System.out.printf("%d%%\n", (int)pComplete);
						System.setOut(out);
	    			}
	    			avgLength = avgLength/(double)runs;
	    			avgTime = avgTime/(double)runs;
	    			System.out.printf("\nAvgLength, AvgTime\n%f,   %f\n", avgLength, avgTime);
	    		}
	    	}
	    	place.removeAllElements();
	    }

	}

	public static void runTestElitism(){
		PrintStream out = System.out;
        PrintStream std = System.out;
	    try
	    {
	        FileOutputStream fos = new FileOutputStream("resultsElitism.csv", true); 
	        out = new PrintStream(fos);
   		 	System.setOut(out);     
	    }
	    catch (FileNotFoundException ex)  
	    {
	    	System.out.println(ex.getMessage());
	    }

	    int runs = 10;

	    //fileName = "u2152.tsp";
	    //file = new File(fileName);

		//shares variables
		ants = 20;
		iterations = 50;
		alpha = 1;
		beta = 3.5;
		rho = 0.1;
		//optimum = 64253;

		//elitist ant variables
		//elitism = 10;

		 //first set of test to optimize the specialized parameters of elitist and acs
	    for(int i=0; i<4; i++){
	    	switch (i){
	    		case 0: 
	    			fileName = "u2152.tsp";
	    			file = new File(fileName);
	    			optimum = 64253;
	    			break;
	    		case 1:
	    			fileName = "fl3795.tsp";
	    			file = new File(fileName);
	    			optimum = 28772;
	    			break;
	    		case 2:
	    			fileName = "fnl4461.tsp";
	    			file = new File(fileName);
	    			optimum = 182566;
	    			break;
	    		case 3:
	    			fileName = "rl5934.tsp";
	    			file = new File(fileName);
	    			optimum = 556045;
	    			break;
	    		default:
	    			System.out.printf("Error in file name.");
	    			break;
	    	}
	    	System.out.printf("\nFile Name: %s\n", fileName);
	    	readFile(file);

	    	for(int j=0; j<3; j++){
	    		switch (j){
	    			case 0:
	    				elitism = 10;
	    				break;
	    			case 1:
	    				elitism = 20;
	    				break;
	    			case 2:
	    				elitism = 30;
	    				break;
	    			default: 
	    				System.out.printf("Error in elitism factor.");
	    				break;
	    		}
	    		System.out.printf("\nElitism Factor: %f\n", elitism);

	    		double avgLength = 0;
	    		double avgTime = 0;
	    		for(int k=0; k<runs; k++){
	    			elite.elitistAnt(place, ants, elitism, alpha, beta, rho, iterations, fileName, optimum);
	    			avgLength += elite.getBestLength();
					avgTime += (double)elite.getDuration();
					System.setOut(std); 
					float pComplete = (((float)k+1)/(float)runs)*100;
					System.out.printf("%d%%\n", (int)pComplete);
					System.setOut(out);
	    		}
	    		avgLength = avgLength/(double)runs;
	    		avgTime = avgTime/(double)runs;
	    		System.out.printf("\nAvgLength, AvgTime\n%f,   %f\n", avgLength, avgTime);
	    	}
	    	place.removeAllElements();
	    }   
	}

	public static void runCommand(String[] arg){
		file 		= new File(arg[0]);
		algorithm 	= arg[1];  					//either e or a
		ants 		= Integer.parseInt(arg[2]);	//integer
		iterations 	= Integer.parseInt(arg[3]);	//integer
		alpha 		= Double.parseDouble(arg[4]);	//double
		beta		= Double.parseDouble(arg[5]);	//double
		rho			= Double.parseDouble(arg[6]);	//double
		optimum 	= Double.parseDouble(arg[7]); 	//double

		readFile(file);
		
		if(algorithm.equals("e"))
		{
			elitism = Double.parseDouble(arg[8]);	//double
			
			//ANDREW: add optimum into your args
			elite.elitistAnt(place, ants, elitism, alpha, beta, rho, iterations, arg[0], optimum);
			output[0] = elite.getBestLength();
			output[1] = (double)elite.getDuration();
		}
		else if(algorithm.equals("a"))
		{
			epsilon = Double.parseDouble(arg[8]);	//double
			qProb 	= Double.parseDouble(arg[9]);	//double
			
			antCS.acs(arg[0], optimum, place, ants, iterations, alpha, beta, rho, epsilon, qProb);
			output[0] = antCS.getBestLength();
			output[1] = (double)antCS.getTime();
		}
		else
		{ 
			System.out.println("A valid algorithm name is needed.  e/a");
			System.exit(0);
		}

		System.out.printf("Best Length: %.0f, Time: %f",output[0], output[1]);
		System.out.println();
	}


	//reads file and stores the cities coordinates and identifier
	public static void readFile(File f) 
	{	
		try 
		{
			reader = new BufferedReader(new FileReader(f));
			String line;
			boolean tempC = false; //true if the line read is coordinates

			while (!(line = reader.readLine()).equals("EOF")) 
			{
				if (line.equals("NODE_COORD_SECTION")) 
				{
					tempC = true;
					continue;
				}
				if(tempC)
				{
					String tempL = line.trim();
					String[] splitStr = tempL.split("\\s+");

					if(splitStr.length != 3)
					{
						System.out.println("There are more than 2 coordinates.");
						System.exit(0);
					}
					
					int identifier = Integer.parseInt(splitStr[0]); 
					double xCoord =	Double.parseDouble(splitStr[1]);
					double yCoord =	Double.parseDouble(splitStr[2]);

					City town = new City(xCoord, yCoord, identifier);
					place.add(town);
				}
			}

			reader.close();
		} 
		catch (Exception e) 
		{
			System.err.format("Exception occurred trying to read '%s'.", f);
			e.printStackTrace();	
		}
	}
	
}

*/