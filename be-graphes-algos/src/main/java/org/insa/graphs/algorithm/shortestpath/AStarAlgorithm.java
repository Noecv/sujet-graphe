package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm.Label;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected class LabelStar extends Label{
    	private Double distance_origine;
    	public LabelStar (Node courant, Arc pere, double distance_origine) {
			super(courant,pere,distance_origine);
			this.cout=distance_origine+courant.getPoint().distanceTo(getInputData().getDestination().getPoint());
			this.distance_origine=distance_origine;
		}

		public void setCost(double cout) {
			this.distance_origine=cout;
			this.cout=cout+this.courant.getPoint().distanceTo(getInputData().getDestination().getPoint());
			
		}
    	
	}
    
	protected Label createlabel(Node fils,Arc successeur,Label min) {
		return new Label (fils,successeur,min.getCost()+ successeur.getMinimumTravelTime()+fils.getPoint().distanceTo(getInputData().getDestination().getPoint()));
	}

}
