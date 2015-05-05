# Ant-Colony-Optimization
A project I led to design and test Ant Colony Optimization approaches on the Traveling Salesman Problem. I designed and coded all classes and objects (aside from the Ant class used in a separate method) and the ElitistAnt algorithm. Other team members implemented the file reading, command line reading, and Ant Colony System algorithms. The final version of the ACO class contains a script for running multiple days worth of tests. I have commented out this code and included an earlier version that allows the user to run the algorithms on the command line. The arguments for running my ElitistAnt algorithm are as follows: 

File name (I recommend eil51.tsp or eil101.tsp) 
Algorithm (e for my ElitistAnt algorithm)
Ants 	        (Number of ants) 
Iterations 	(Number of iterations) 
Alpha 	(The degree of influence of the pheromone component. Recommended = 1.0) 			Beta		(The degree of influence of the elitist component. Recommended = 2 - 5) 				Roh		(The pheromone evaporation factor. Recommended = 0.1) 						Elitism  	(The elitism factor. Recommended = number of ants) 						Optimum	(The known optimum for the problem. For eil51.tsp = 426, eil101.tsp = 629)  			

An example of a command line call: 

					java ACO eil51.tsp e 10 10 1 3.5 0.1 10 426  
					
Play around with the parameters and see what happens to the best tour length the program can find! This project was compiled on the command line in the Terminal.

A brief description of my ElitistAnt algorithm:

"This algorithm simulates Ant Colony Optimization on the traveling salesman problem. Paths are constructed between cities, and ants walk these paths to complete tours. After each tour is complete, paths receive pheromone updates. Updates are proportional to the frequency which which the ants use the path. Paths on the shortest tour receive extra boosts in order to steer the ants toward
the best current solution (this is known as the elitism factor). Pheromone evaporates over time."

						