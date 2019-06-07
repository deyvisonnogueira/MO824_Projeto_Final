package problems.influence;

import java.util.ArrayList;

import problems.Evaluator;
import solutions.Solution;

public class Vertex implements Evaluator<Vertex> {
	
	public int id;
	public int threshold;
	public ArrayList<Arc> arcs;
	
	public Vertex(int id, int threshold) {
		this.id = id;
		this.threshold = threshold;
		this.arcs = new ArrayList<Arc>();
		
	}
	
	public Vertex() {
		super();
	}
	
	public Vertex(Vertex v) {
		this.id = v.id;
		this.threshold = v.threshold;
		this.arcs = new ArrayList<Arc>();
		for(Arc a: v.arcs) {
			arcs.add(a);
		}
	}
	
	public void addArc(Arc arc) {
		if(searchArc(arc) == null ) {
			arcs.add(arc);
		}else {
			System.out.println("arco já inserido");
		}
		
	}
	
	@Override
	public String toString() {
		return "id=["+this.id+"], threshold = "+this.threshold + ", arcs {"+arcs.toString()+"}";
	}
	
	public Arc searchArc(Arc arc) {
		for(Arc a: this.arcs) {
			if(a.equals(arc)) {
				return a;
			}
		}
		
		return null;
	}


	@Override
	public Integer getDomainSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double evaluate(Solution<Vertex> sol) {
		return null;
	}
	
	public Double evaluate(Solution<Vertex> sol, Graph g) {		
		for(Vertex v: sol) {
			g.active.add(v);
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	public Graph influencia(Graph g) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(Vertex v: g.active) {
			
			ids.add(v.id);
			for(Arc a: v.arcs) { 
				g.decreaseThreashold(a.dest);
			}
		}
		for(Integer i: ids) {
			g.removeVertexById((int)i);
		}
		
		for(Vertex v: g.V) {
			if(v.threshold == 0) {
				g.active.add(v);
			}
		}
		return g;
	}

	@Override
	public Double evaluateInsertionCost(Vertex elem, Solution<Vertex> sol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double evaluateRemovalCost(Vertex elem, Solution<Vertex> sol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double evaluateExchangeCost(Vertex elemIn, Vertex elemOut, Solution<Vertex> sol) {
		Graph incuSol = new Graph();
		return null;
	}
	
}
