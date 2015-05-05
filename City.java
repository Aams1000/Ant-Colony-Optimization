
        /****************************************
         *                                      *
         *              City class              *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description: 	The city class represents a city (vertex) on our graph. It stores the x and y coordinates and 
                     	a unique identifier. Functions include basic set and get functions for the variables.

        */

import java.util.*;
import java.io.*;
import java.util.Random;

public class City {

    private double x;
    private double y;
    private int identifier;

    //constructor takes city coordinates as well as identifier
    public City(double xCoord, double yCoord, int index)
    {
        x = xCoord;
        y = yCoord;
        identifier = index;
    }

    public double getX()
    {
         return x;
    }
    public double getY()
    {
        return y;
    }

    public int getIdentifier()
    {
        return identifier;
    }

}
