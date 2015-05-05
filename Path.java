       
		/****************************************
         *                                      *
         *              Path class              *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description: 	The path class represents the path between two cities. It stores the two cities, as well as the 
                        distance between them, the pheromone levels on that path, and a unique identifier. Functions 
                        include basic get and set equations for the variables.

        */

import java.util.*;
import java.io.*;
import java.util.Random;

public class Path {
    
	//variables for the path
    private double pheromone;
    private City a;
    private City b;
    private int identifier;
    private double length;

    //constructor takes the two cities and the unique identifier. Initializes the pheromone level to 0.2 and calculates the path length
    public Path(City one, City two, int index, double pheromoneLevel)
    {
        a = one;
        b = two;
        identifier = index;
        pheromone = pheromoneLevel;
        length = Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    public double getPheromone()
    {
        return pheromone;
    }
    
    public void setPheromone(double updatedAmount)
    {
    	if (updatedAmount > 0)
    	{
    		pheromone = updatedAmount;
    	}
    	else
    	{
    		pheromone = 0;
    	}
    	
    }

    public void updatePheromone(double rowe, double update)
    {
        pheromone = (1 - rowe)*pheromone + update;
        if (pheromone < 0)
    	{
    		pheromone = 0;
    	}
    }

    public City getA()
    {
        return a;
    }

    public City getB()
    {
        return b;
    }

    public int getIdentifier()
    {
        return identifier;
    }

    public double getLength()
    {
        return length;
    }
    
}