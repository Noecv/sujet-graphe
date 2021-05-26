package org.insa.graphs.algorithm.utils;


import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AStarAlgorithmTest {

    // Small graph use for tests
    private static Graph graph;
    
    // Some data
    private static ShortestPathData data;
    

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    
    private static AStarAlgorithm Astar;
    

    private static DijkstraAlgorithm Dijkstra;
    
    private static ShortestPathSolution solnull, soltest;
    

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

    }
    
	@Test
	public void testOneNodeSolution()throws Exception {
		data = new ShortestPathData(graph, nodes[0], nodes[0], ArcInspectorFactory.getAllFilters().get(0));
		Astar = new AStarAlgorithm(data);
		solnull = Astar.run();
		Dijkstra = new DijkstraAlgorithm(data);
		soltest = Dijkstra.run();
		assertEquals(solnull.toString(), soltest.toString());  // Ici BFA renvoie une absence de chemin alors que Dijkstra renvoie un chemin de longueur nulle, je ne règle pas cette failure car je préfère l'interprétation faite par Dijkstra
	}
	





	@Test
	public void testGuadeloupe() throws Exception {
	    // Visit these directory to see the list of available files on Commetud.
	    String mapName = "E:\\Noe\\insa\\POO\\maps\\guadeloupe.mapgr";
	    // Create a graph reader.
	    GraphReader reader = new BinaryGraphReader(
	            new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

	    Graph graphcarre = reader.read();
	    
	    
	    
	    for(int i=0; i<15; i++) {
	    	Random random = new Random();
	    	int a = random.nextInt(graphcarre.size());
	    	int b =  random.nextInt(graphcarre.size());
	    	while (a==b) {
		    	b =  random.nextInt(graphcarre.size());
	    	}
	    	Node origin = graphcarre.get(a);
	    	Node goal = graphcarre.get(b);
	    	data = new ShortestPathData(graphcarre, origin, goal, ArcInspectorFactory.getAllFilters().get(0));
	    	Astar = new AStarAlgorithm(data);
	    	Dijkstra = new DijkstraAlgorithm(data);
	    	
	    	if (Astar.run().isFeasible()) {
	    		List<Arc> expected = Astar.run().getPath().getArcs();
	    		Path path = Dijkstra.run().getPath();
	    		for (int j = 0; j < expected.size() ; j++) {
	    			assertEquals(expected.get(j), path.getArcs().get(j));
	    		}
	        }
	    	else {
	    		assertEquals(Astar.run().isFeasible(), Dijkstra.run().isFeasible());
	    	}
	    }  
		}
	
}
