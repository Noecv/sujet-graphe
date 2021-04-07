package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	private class Label implements Comparable<Label> {
		private Node courant;
		private boolean marque=false;
		private double cout=Double.POSITIVE_INFINITY;
		private Node pere=null;
		
		public Label(Node courant, Node pere, double cout) {
			this.courant=courant;
			this.pere=pere;
			this.cout=cout;
		}
		@Override
		public int compareTo(Label label) {
			if (this.cout==label.getCost()) {
				return 0;
			}
			if (this.cout<label.getCost()) {
				return -1;
			}
			return 1;
			
		}
		
		public void mark() {
			this.marque=true;
		}
		public boolean getmark() {
			return this.marque;
		}
		public void setfather(Node pere) {
			this.pere=pere;
		}
		public Node getcurrent() {
			return this.courant;
		}

		public void setCost(double cout) {
			this.cout=cout;
		}
		public double getCost() {
			return this.cout;
		}
	}
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        // TODO:
        Graph graph = data.getGraph();
        int nbNodes = graph.size();

        // Initialize array of Label.
        Label[] labels = new Label[nbNodes];
        int i=0;
        labels[data.getOrigin().getId()]=new Label(data.getOrigin(),null,0);
        
        BinaryHeap<Label> tas = new BinaryHeap();
        tas.insert(labels[data.getOrigin().getId()]);
        
        boolean condition=true;  //condition qu'on passera à false quand la destionation sera traitée
        Label min;
        while (condition) {
        	min=tas.deleteMin();
        	for (Arc successeur: min.getcurrent().getSuccessors()) {
        		Node fils=successeur.getDestination();
        		if (labels[fils.getId()]==null) {
        			labels[fils.getId()] = new Label (fils,min.getcurrent(),min.getCost()+shortestpathfromnodes(successeur));
        		}
        		
        	}
        }
        
        return solution;
    }

}
