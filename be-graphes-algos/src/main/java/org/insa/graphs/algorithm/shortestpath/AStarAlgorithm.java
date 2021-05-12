package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm.Label;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected class LabelStar extends Label{
    	public LabelStar (Node courant, Arc pere, double cout) {
			super(courant,pere,cout);
		}
    	protected  double GetTotalCost () {
    		if (data.getMode() == org.insa.graphs.algorithm.AbstractInputData.Mode.TIME) {
        		return (this.cout+(this.courant.getPoint().distanceTo(getInputData().getDestination().getPoint()))/data.getGraph().getGraphInformation().getMaximumSpeed());
    		}
    		return (this.cout+this.courant.getPoint().distanceTo(getInputData().getDestination().getPoint()));
    	}
	}
    
	protected Label createlabel(Node fils,Arc successeur,double cout) {
		return new LabelStar (fils,successeur,cout);
	}

}
