package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	public static double EPSILON = 0.0000000001;
	protected class Label implements Comparable<Label> {
		protected Node courant;
		protected boolean marque=false;
		protected double cout=Double.POSITIVE_INFINITY;
		protected Arc pere=null;
		
		public Label(Node courant, Arc pere, double cout) {
			this.courant=courant;
			this.pere=pere;
			this.cout=cout;
		}
		@Override
		public int compareTo(Label label) {
			if (Math.abs(this.GetTotalCost()-label.GetTotalCost())<EPSILON) {
				return 0;
			}
			if (this.GetTotalCost()<label.GetTotalCost()) {
				return -1;
			}
			return 1;
			
		}
		protected  double GetTotalCost () {
			return this.cout;
		}
		
		public void mark() {
			this.marque=true;
		}
		public boolean getmark() {
			return this.marque;
		}
		public void setfather(Arc pere) {
			this.pere=pere;
		}
		public Arc getfather() {
			return this.pere;
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
	protected Label createlabel(Node fils,Arc successeur,double cout) {
		return new Label (fils,successeur,cout);
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
        
     	if (data.getOrigin()==data.getDestination()) {
     		Path path = new Path(graph, data.getOrigin());
     		solution = new ShortestPathSolution(data, Status.OPTIMAL, path);
     		return solution;
     		
     	}
        int nbNodes = graph.size();

        // Initialize array of Label.
        Label[] labels = new Label[nbNodes];
        labels[data.getOrigin().getId()]=createlabel(data.getOrigin(),null,0);
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        tas.insert(labels[data.getOrigin().getId()]);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        boolean condition=true;  //condition qu'on passera à false quand la destination sera traitée
        Label min;
        while (condition) {  //construction du tableau de dijkstra
        	min=tas.deleteMin();  //On récupère l'élément de moindre cout dans le tableau en temps constant grâce au tas binaire
        	labels[min.getcurrent().getId()].mark();   //On le marque puisque on a trouvé le chemin de taille minimal reliant l'origine à ce noeud
        	if (min.getcurrent() == data.getDestination()) {
        		condition = false;    //si ce noeud est la destination, on a fini
        		continue;
        	}
        	
        	for (Arc successeur: min.getcurrent().getSuccessors()) {  //On parcours les arcs issus du noeud min
        		Node fils=successeur.getDestination();
        		
        		if (!data.isAllowed(successeur)) {
        			continue;
        		}
        		
        		if (labels[fils.getId()] == null) {   //cas où le noeud destination de l'arc n'a jamais été traité
                    notifyNodeReached(successeur.getDestination());
        			labels[fils.getId()] = createlabel(fils, successeur, min.getCost()+ data.getCost(successeur));   //On lui attribue comme père le noeud min et son cout est cout(min)+cout(arc)
        	        tas.insert(labels[fils.getId()]);  //on insère ce label dans le tas
        		}
        		else if (labels[fils.getId()].getmark()) {  //si on a déjà trouvé le chemin de taille minimum jusqu'à la destination de l'arc, on enchaine
        			continue;
        		}
        		else if (labels[fils.getId()].getCost()>min.getCost()+ data.getCost(successeur)){  //On regarde si le nouveau chemin pour lier la destination de l'arc est plus efficace
        			labels[fils.getId()].setfather(successeur);
        			labels[fils.getId()].setCost(min.getCost()+ data.getCost(successeur));
        			tas.remove(labels[fils.getId()]);
        			tas.insert(labels[fils.getId()]);   // On retri le tas
        		}
        		
        	}
        	if (tas.isEmpty()) {
        		condition = false;
        	}
        }
        
        java.util.List<Arc> pluscourtchemin = new ArrayList<Arc>();
        Label courant = labels[data.getDestination().getId()];
        if (courant == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            return solution;
        }
        condition = true;
        while (courant.getfather()!=null) {
        	pluscourtchemin.add(0,courant.getfather());
        	courant=labels[courant.getfather().getOrigin().getId()];
        }
        Path path = new Path(graph , pluscourtchemin);
        // The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());
        solution = new ShortestPathSolution(data, Status.OPTIMAL, path);
        
        return solution;
    }

}
