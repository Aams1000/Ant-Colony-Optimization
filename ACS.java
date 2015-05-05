       
		/****************************************
         *                                      *
         *               ACS class              *
         *             Gabby Grandin            *
         *                                      *
         ****************************************/

        /*
        
        Description:    This algorithm simulates Ant Colony Optimization on the traveling salesman problem. Paths are constructed between cities, and ants
                        walk these paths to complete tours. Each time an ant walks on a path, a small amount of the pheromone is worn away. After each tour 
                        is complete, paths receive pheromone updates. Paths on the shortest tour receive extra boosts in order to steer the ants toward the 
                        best current solution, and all pheromone levels evaporate over time.
                        
        */

import java.util.*;
import java.lang.Math;

public class ACS {

	//vectors for storing cities and paths between cities
	private static Vector<City> cities;
    private static Vector<Path> pathsVector = new Vector<Path>();
    private static Path[][] paths;
    
    //variables for storing best solution
    private static Tour bestSolution = new Tour();
    private static double bestLength = Double.POSITIVE_INFINITY;

    //time limit to stop algorithm (set at five minutes)
    private static final long TIME_LIMIT = 300000;

    //random number generator
    private static Random rand = new Random();

    //named constants assigned after receiving parameters
    private static String FILE_NAME;
    private static double OPTIMAL_LENGTH;
    private static int NUM_CITIES;
    private static int NUM_ANTS;
    private static int NUM_ITERATIONS;
    private static double ALPHA; //the degree of influence of the pheromone component
    private static double BETA; //the degree of influence of the heuristic component
    private static double RHO; //the pheromone evaporation factor
    private static double TAU; //initial pheromone concentration on the leg connecting 2 cities
    private static double EPSILON; //factor controlling "wearing away" of pheromones (typically e = 0.1)
    private static double Q_PROB; //probability that an ant will choose the best leg for the next leg of it's tour
    private static int totalIterations;
    private static long time;

    public static void acs(String fileName, double optimalLength, Vector<City> citiesVector, int numAnts, 
    		int iterations, double alpha, double beta, double rho, double epsilon, double qProb)
    {
        //set up variables
        FILE_NAME = fileName;
        OPTIMAL_LENGTH = optimalLength;
        cities = citiesVector;
        NUM_CITIES = cities.size();
        NUM_ANTS = numAnts;
        NUM_ITERATIONS = iterations;
        ALPHA = alpha;
        BETA = beta;
        RHO = rho;
        EPSILON = epsilon;
        Q_PROB = qProb;

        //start timer
        long startTime = System.currentTimeMillis();
        long endTime = 0;

        //create paths vector and 2D array  
        int counter = 0;
        paths = new Path[cities.size()][cities.size()];
        for (int i=0; i<cities.size(); i++)
        {
            for (int j=i+1; j<cities.size(); j++)
            {
                Path newPath = new Path(cities.get(i), cities.get(j), counter, 0);
                pathsVector.add(newPath);
                paths[i][j] = newPath;
                paths[j][i] = newPath;
                counter++;
            }
        }
        
        calculateTAU();
        
        //initalize pheromone level
        for (int i=0; i<pathsVector.size(); i++)
        {
        	Path path = pathsVector.get(i); 
        	path.setPheromone(TAU);
        }

        //START TOURS
        
        //send ants out on tours (find solutions and update) until either the optimum is found or time limit is reached
        totalIterations = 0;
        while ((totalIterations < NUM_ITERATIONS) && (System.currentTimeMillis()-startTime < TIME_LIMIT) && (bestLength > OPTIMAL_LENGTH))
        {
            //current best tour
        	Tour currentBest = new Tour();
            double currentBestLength = Double.POSITIVE_INFINITY;
            
            //CREATE ANTS
            
            //initialize all ants
            Ant[] arrayOfAnts = new Ant[NUM_ANTS];
            for (int i=0; i<NUM_ANTS; i++)
            {
            	int antID = i;
            	int startingCity = rand.nextInt(cities.size() - 1); //choose random starting city
            	
            	Ant ant = new Ant(antID, startingCity, NUM_CITIES);
            	arrayOfAnts[antID] = ant;
            }
            
            //MOVE ANTS
            
            //travel to new cities until all cities have been visited
            int numberOfCitiesVisited = 1;
            while (numberOfCitiesVisited < NUM_CITIES)
            {
            	//FOR EACH INDIVIDUAL ANT
            	
            	//move each ant to a new city
            	for (int i=0; i<NUM_ANTS; i++)
            	{
            		int newCity = -1;
            		int currentCity = arrayOfAnts[i].getCurrentCity();
            		boolean[] vistiedCityAlready = new boolean[NUM_CITIES];            		
        			vistiedCityAlready = arrayOfAnts[i].getVisitedCities();
            		
            		//use q probability to choose ant's next city destination
            		if (rand.nextDouble() < Q_PROB)
            		{
            			//move to city that maximizes equation t*(n^beta) 
            			int maxCity = -1;
            	    	double maxCityValue = 0;
            		
            			//loop through all cities the ant hasn't visited yet to find the max result
            			for (int j=0; j<cities.size(); j++)
            			{
            				if (vistiedCityAlready[j] == false)
            				{            					
            					Path possiblePath = paths[currentCity][j];
            					double nextCityValue = possiblePath.getPheromone()*Math.pow(1/possiblePath.getLength(), BETA);
            					if (nextCityValue > maxCityValue)
            					{
            						maxCityValue = nextCityValue;
            						maxCity = j;
            					}
            				}
            			}
            			//city that maximizes the equation
            			newCity = maxCity;
            		}
            		//use the action choice rule to choose ant's next city destination
            		else 
            		{
            			while (newCity == -1) //must find new city
            			{
            				int possibleCity = rand.nextInt(cities.size() - 1); //randomly choose a city

            				if (vistiedCityAlready[possibleCity] == false) //haven't visited city yet
            				{
            					//calculate proability of moving ant to this city
            					double denominator = 0;
            					for (int j=0; j<cities.size(); j++)
            					{
            						if (vistiedCityAlready[j] == false)
            						{
            							Path allPossiblePaths = paths[currentCity][j];
            							denominator = denominator + (Math.pow(allPossiblePaths.getPheromone(), ALPHA)*Math.pow(1/allPossiblePaths.getLength(), BETA));
            						}
            					}

            					Path possiblePath = paths[currentCity][possibleCity];
            					double numerator = (Math.pow(possiblePath.getPheromone(), ALPHA)*Math.pow(1/possiblePath.getLength(), BETA));

            					double probabilityOfChoosing = numerator/denominator;

            					//is the probability high enough for the ant to actually move?
            					if (rand.nextDouble() < probabilityOfChoosing)
            					{
            						newCity = possibleCity;
            					}
            				}
            			}
            		}
            		//move ant to new city
            		arrayOfAnts[i].updateAnt(newCity, paths[currentCity][newCity]);
            		
            		//update path, pheromone "worn away" when ant moves along path 
    				Path traversedPath = paths[currentCity][newCity];
    				double newPheromone = (((1-EPSILON)*traversedPath.getPheromone())+(EPSILON*TAU));
    				traversedPath.setPheromone(newPheromone);
            	}
            	
            	//REPEAT UNTIL ALL CITIES VISITED
            	
            	//all ants have visited a new city
            	numberOfCitiesVisited++;		
            }
            
            //TOUR COMPLETE
            
            //check if a shorter tour has been found
            for (int i=0; i<NUM_ANTS; i++)
            {
            	Tour test = arrayOfAnts[i].getTour();
            	if (test.getLength() < bestLength)
            	{
            		bestLength = test.getLength();
            		bestSolution = test;
            	}
            }
            
            //UPDATE PHEROMONE LEVELS 
            
            //pheromone increases only on legs of best-so-far tour
            double increaseAmount= 1/bestSolution.getLength();
            for (int i=0; i<bestSolution.getSize(); i++)
            {
            	Path step = bestSolution.getPath(i);
            	step.setPheromone(step.getPheromone()+increaseAmount);
            }
    			
            //pheromone evaporates on every leg after a tour 
            for (int i=0; i<pathsVector.size(); i++)
            {
            	Path step = pathsVector.get(i);
            	step.setPheromone(step.getPheromone()*(1-RHO));
            }
            
            //REPEAT UNTIL OPTIMAL FOUND
            totalIterations++;
            
            //System.out.println("iteration " +totalIterations+ ", current best length: " +bestLength);
        }
        
        //DONE
        
        //print out our findings
        // System.out.println();
        // System.out.println("********** Ant Colony System Algorithm Results **********");
        // System.out.println();
        // System.out.println("Name of file: " + FILE_NAME);
        // System.out.println("Number of cities: " + NUM_CITIES);
        // System.out.println("Number of ants: " + NUM_ANTS);
        // System.out.println("Number of iterations: " + NUM_ITERATIONS);
        // System.out.println();
        // System.out.println("Length of shortest tour: " + bestLength);
        
        endTime = System.currentTimeMillis();
        time = (endTime-startTime)/1000;
        // System.out.println("This method took: " + (time) + " seconds.");
        // System.out.println();
    }

    public static int getTotalIterations(){
        return totalIterations;
    }

    public static double getBestLength(){
        return bestLength;
    }
    
    public static long getTime(){
        return time;
    }

    public static void calculateTAU()
    {    			
    	int antID = 1;
    	int startingCity = rand.nextInt(cities.size() - 1); //choose random starting city
    	Ant ant = new Ant(antID, startingCity, NUM_CITIES);
    	
    	int numberOfCitiesVisited = 1;
    	boolean[] vistiedCityAlready = new boolean[NUM_CITIES]; 
    	while (numberOfCitiesVisited < NUM_CITIES)
        {   		
    		vistiedCityAlready = ant.getVisitedCities();
    		
    		//move to closest city 
			int closestCity = -1;
	    	double closestCityValue = Double.POSITIVE_INFINITY;
		
			//loop through all cities the ant hasn't visited yet to find the closest one
			for (int i=0; i<cities.size(); i++)
			{
				if (vistiedCityAlready[i] == false)
				{            					
					Path possiblePath = paths[ant.getCurrentCity()][i];
					if (possiblePath.getLength() < closestCityValue)
					{
						closestCityValue = possiblePath.getLength();
						closestCity = i;
					}
				}
			}
			//move ant to new city
			ant.updateAnt(closestCity, paths[ant.getCurrentCity()][closestCity]);
			
			numberOfCitiesVisited++;	
        }
    	//get tour length
    	Tour nearestNeighborTour = ant.getTour();
    	double length = nearestNeighborTour.getLength();
    	
    	TAU = 1/(NUM_ANTS*length);
    }

}
