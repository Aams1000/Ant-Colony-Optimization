
        /****************************************
         *                                      *
         *               Ant class              *
         *             Gabby Grandin            *
         *                                      *
         ****************************************/

        /*

        Description: 	The ant class represents each individual ant. It contains a unique identifier, the ant's 
        				current city, a list of cities the ant has already visited, and the total distance the 
        				ant has traveled so far. Functions include updating the ant variables and basic getters 
        				and setters.
        				
        */

public class Ant {
	
	private int identifier;
	private int currentCity;
	private boolean[] visitedCities;
	private double totalDistanceTaveled;
	private Tour route = new Tour();
	
	//constructor takes ant identifier and starting city number
	public Ant(int number, int startingCity, int totalNumberOfCities)
	{
		identifier = number;
		currentCity = startingCity;
		visitedCities = new boolean[totalNumberOfCities];
		visitedCities[currentCity] = true;
		totalDistanceTaveled = 0;
		Tour route = new Tour();
	}
	
	//update ant variables after ant moves to new city
	public void updateAnt(int newCity, Path newPath)
	{
		currentCity = newCity;
		visitedCities[newCity] = true;
		route.addPath(newPath);
		totalDistanceTaveled = totalDistanceTaveled + newPath.getLength();
	}
	
	public int getIdentifier()
	{
		return identifier;
	}
	
	public int getCurrentCity()
	{
		return currentCity;
	}
	
	public boolean[] getVisitedCities()
	{
		return visitedCities;
	}
	
	public double getLength()
	{
		return totalDistanceTaveled;
	}
	
	public Tour getTour()
	{
		return route;
	}
	
}
