        /****************************************
         *                                      *
         *        Elitist Ant Algorithm         *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description:    This algorithm simulates Ant Colony Optimization on the traveling salesman problem. Paths are constructed between cities, and ants
                        walk these paths to complete tours. After each tour is complete, paths receive pheromone updates. Updates are proportional to the
                        frequency which which the ants use the path. Paths on the shortest tour receive extra boosts in order to steer the ants toward
                        the best current solution (this is known as the elitism factor). Pheromone evaporates over time.

                        The Enrichment Center promises to always provide a safe testing environment. In dangerous testing environments, 
                        the Enrichment Center promises to always provide useful advice. For instance, the floor here will kill you. Try to avoid it.
        */

import java.util.*;
import java.io.*;
import java.util.Random;

public class ElitistAnt {
	
    //Vectors and arrays for storing tours the ants make and paths between cities
    private static Vector<Tour> solutions = new Vector<Tour>();
    private static Vector<Path> pathsVector = new Vector<Path>();
    private static Path[][] paths;
    private static Vector<City> cities;
    private static int numPaths;

    //variables for storing best solution so far
    private static Tour bestSolution;
    private static double bestLength = Double.POSITIVE_INFINITY;

    //time limit to stop algorithm. set at five minutes
    private static final long TIME_LIMIT = 300000;

    //random number generator
    private static Random rand = new Random();

    //named constants and other variables assigned upon receiving parameters
    private static int NUM_ANTS;
    private static String FILE_NAME;
    private static double ELITISM_FACTOR;
    private static double TARGET_OPTIMUM = 300;
    private static int NUM_CITIES;
    private static int NUM_ITERATIONS;
    private static double ALPHA;
    private static double BETA;
    private static double ROH;
    private static double PERCENT_ABOVE_OPTIMUM = 0.0;
    private static int totalIterations;
    private static long duration;

    public static void elitistAnt(Vector<City> citiesVector, int numAnts, double elitismFactor, 
    		double alpha, double beta, double roh, int iterations, String fileName, double optimum)
    {
        //set up variables
        cities = citiesVector;
        NUM_ANTS = numAnts;
        ELITISM_FACTOR = elitismFactor;
        NUM_CITIES = cities.size();
        ALPHA = alpha;
        BETA = beta;
        ROH = roh;
        NUM_ITERATIONS = iterations;
        FILE_NAME = fileName;
        TARGET_OPTIMUM = (double)optimum * (1 + PERCENT_ABOVE_OPTIMUM);

        //start timer
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        duration = 0;

        //CREATE PATHS VECTOR AND 2D ARRAY. I LIKE CAPS
        paths = new Path[cities.size()][cities.size()];
        int counter = 0;
        for (int i = 0; i < cities.size(); i++)
        {
            for (int j = i + 1; j < cities.size(); j++)
            {
                Path newPath = new Path(cities.get(i), cities.get(j), counter, 0.05);
                pathsVector.add(newPath);
                paths[i][j] = newPath;
                paths[j][i] = newPath;
                counter++;
                
            }
        }

        //store number of paths
        numPaths = counter;

        //continue sending out ants (finding solutions and updating) until find optimum or reach time limit
        totalIterations = 0;
        while (totalIterations < NUM_ITERATIONS && duration < TIME_LIMIT && bestLength > TARGET_OPTIMUM)
        {
            //System.out.println("Entered while loop.");

            //tour for storing current best
            double currBestLength = Double.POSITIVE_INFINITY;
            Tour currBest = new Tour();

            //construct a tour for each ant
            for (int i = 0; i < NUM_ANTS; i++)
            {
                //choose random starting city
                int startingCity = rand.nextInt(cities.size() - 1);
                int currentCity = startingCity;
                

                //create array to track which cities we have visited, counter to track total number
                int numVisited = 1;
                boolean[] visitedCities = new boolean[NUM_CITIES];
                visitedCities[startingCity] = true;

                //tour to store the ant's journey
                Tour route = new Tour();

                //visit new cities until we have been to all of them
                while (numVisited < NUM_CITIES)
                {
                    //variables to store numerators and denominator for probabilities
                    double denominator = 0;
                    double[] numerators = new double[cities.size()];

                    //loop through cities once to calculate sum of pheromone times length
                    for (int j = 0; j < cities.size(); j++)
                    {
                        if (visitedCities[j] == false)
                        {
                           Path possiblePath = paths[currentCity][j];
                           double currNumerator = (Math.pow(possiblePath.getPheromone(), ALPHA) * Math.pow(1 / possiblePath.getLength(), BETA));
                           denominator += currNumerator;
                           numerators[j] = currNumerator;
                        }
                    }

                    //calculate and store probabilities of each path
                    double[] probabilities = new double[cities.size()];
                    double testCounter = 0;
                    for (int j = 0; j < cities.size(); j++)
                    {
                        if (visitedCities[j] == false)
                        {
                            probabilities[j] = numerators[j] / denominator;
                            testCounter += probabilities[j];
                        }
                    }
                    
                    //use probabilities to chose next city, mark it as visited
                    double randomDouble = rand.nextDouble();
                    double probabilitySum = 0;
                    for (int j = 0; j < cities.size(); j++)
                    {
                        
                        if (visitedCities[j] == false)
                        {
                            //if chosen, add path to tour and update tracking variables. MWAHAHAHA IT WORKS! 
                            if (randomDouble < probabilitySum + probabilities[j]){

                                route.addPath(paths[currentCity][j]);
                                currentCity = j;
                                visitedCities[j] = true;
                                numVisited++;
                                break;
                            }
                            else
                            {
                                probabilitySum += probabilities[j];
                            }
                        }
                    }                    
                }

                //add new route to solutions vector, check if it's best so far
                solutions.add(route);
                if (route.getLength() < currBestLength)
                {    
                    //update old best to new
                    currBest.notBest();
                    currBest = route;
                    currBestLength = route.getLength();
                    currBest.markBest();
                }
            }

            //update pheromone levels according to elitism equations
            double updates[] = new double[numPaths];
            for (int i = 0; i < solutions.size(); i++)
            {
                for (int j = 0; j < solutions.get(i).getSize(); j++)
                {
                    Tour curr = solutions.get(i);
                    //update if tour is best
                    if (curr.isBest() == true)
                    {
                        updates[curr.getPath(j).getIdentifier()] += (ELITISM_FACTOR + 1)*(1 / curr.getLength());
                    }
                    else
                    {
                        updates[curr.getPath(j).getIdentifier()] += 1 / curr.getLength();
                    }
                }
            }

            //now apply updates to all paths
            for (int i = 0; i < pathsVector.size(); i++)
            {
                pathsVector.get(i).updatePheromone(ROH, updates[pathsVector.get(i).getIdentifier()]);
 
            }

            //check if current best is best so far
            if (currBestLength < bestLength)
            {

                bestLength = currBestLength;
                bestSolution = currBest;
            }

            totalIterations++;
            solutions.clear();

            //update time
            endTime = System.currentTimeMillis();
            duration = (endTime - startTime);

            //break;
        }

        //print out our findings
        System.out.println("\n******************* Elitist Ant Algorithm Results *******************");
        System.out.println("Please note that we have added a consequence for failure. Any contact with the chamber floor will result in an 'unsatisfactory' mark on your official testing record, followed by death. Good luck!");
        //we'll need to print out the name of the file we are reading. This should come from the user-input parsing part...maybe make it a global variable?
        System.out.println("Name of file: " + FILE_NAME);

        //same for NUM_VARIABLES and number of clauses
        System.out.println("Number of cities: " + NUM_CITIES);
        System.out.println("Number of ants: " + NUM_ANTS);
        
        if (bestLength < TARGET_OPTIMUM)
        {
            System.out.println("Target optimum reached early.");   
        }    
        
        if (duration > TIME_LIMIT)
        {
            System.out.println("Time limit reached.");
        }
       
        System.out.println("Number of iterations: " + totalIterations);
        
        //we know how many clauses we have satisfied
        System.out.println("Length of shortest path: " + bestLength);
        
        System.out.println("\nThis method took: " + duration + " milliseconds.");
    }

    public static int getTotalIterations(){
        return totalIterations;
    }

    public static double getBestLength(){
        return bestLength;
    }

    public static long getDuration(){
        return duration;
    }

    //method to fill temp cities vector. DON'T GET RID OF ME! I AM A TIMELSS RELIC OF THE PAST
    public static void generateCities()
    {
        for (int i = 0; i < NUM_CITIES; i++)
        {
            City newCity = new City(rand.nextInt(100), rand.nextInt(100), i);
            cities.add(newCity);
        }
    }

}