package problems.influence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import solutions.Solution;

public class RelaxGraph extends Graph {
	
	public RelaxGraph(String filename) throws IOException {
		super(filename);
	}
	public RelaxGraph() {
		super();
	}
	
	public RelaxGraph(Graph g) {
		this.V = g.V;
		this.A = g.A;
		this.active = g.active;
	}

	
	
	@Override
	public Graph propagation() {
		//System.out.println("Sorragate");
		Graph g = new Graph();
		for(Vertex v: this.V) {
			g.V.add(new Vertex(v));
		}
		for(Arc a: this.A) {
			g.A.add((new Arc(a)));
		}
		for(Vertex v: this.active) {
			g.active.add(new Vertex(v));
		}
		
	
		while(!g.active.isEmpty() && g.active.size() > (g.V.size()/2) ) { 
			Vertex v = g.active.get(new Random().nextInt(g.active.size()));
			g = v.influencia(g);
		}	
		
		
		return g;	
	}
	
	@Override
	public Double evaluateExchangeCost(Vertex elemIn, Vertex elemOut, Solution<Vertex> sol) {
		//System.out.println("relax eva");
		RelaxGraph incumSol = new RelaxGraph();
		for(Vertex v: this.V) {
			incumSol.V.add(new Vertex(v));
		}
		for(Arc a: this.A) {
			incumSol.A.add(new Arc(a));
		}
		incumSol.active = new ArrayList<Vertex>();
		for(Vertex v: sol) {
			incumSol.active.add(new Vertex(v));
		}
		//Graph aux = incumSol.propagation();
		incumSol.active.remove(elemOut);
		incumSol.active.add(elemIn);
		Graph aux = incumSol.propagation();
		Double posCost = Double.valueOf(aux.V.size());
		
		return  posCost;
	}
	
}
