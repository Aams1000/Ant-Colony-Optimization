        /****************************************
         *                                      *
         *              Tour class              *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description: 	The tour class represents the route an ant uses to traverse the cities. It contains a 
        				vector of paths to store the legs of the journey. Functions allow other classes to 
        				perform basic vector operations on the route and indicate whether the tour is the best yet.
        */

import java.util.*;
import java.io.*;
import java.util.Random;

public class Tour {

    //vector to store paths
    private Vector<Path> route;
    double length;
    private boolean best;

    //constructor takes no parameters, initializes route vector, sets best to false, and sets length to zero
    public Tour()
    {
        route = new Vector<Path>();
        length = 0;
        best = false;
    }

    public void addPath(Path p)
    {    	
        route.add(p);
        length = length + p.getLength();
    }

    public Path getPath(int i)
    {
        return route.get(i);
    }
    
    public int getSize()
    {
        return route.size();
    }

    public double getLength()
    {
        return length;
    }
    
    //////////////////////////////////

    public void clear()
    {
        route.clear();
    }

    public boolean isEmpty()
    {
        return route.isEmpty();
    }

    public boolean isBest()
    {
        return best;
    }
    
    public void markBest()
    {
        best = true;
    }

    public void notBest()
    {
    	best = false;
    }

}
