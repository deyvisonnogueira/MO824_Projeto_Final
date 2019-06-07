package problems.qbf.solvers;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import metaheuristics.tabusearch.AbstractTS;
import problems.Evaluator;
import problems.influence.Graph;
import problems.influence.Vertex;
import solutions.Solution;

public class TS_Influence extends AbstractTS<Vertex> {
	
	public Graph instance;
	public int k;

	public TS_Influence(Evaluator<Vertex> objFunction, Integer tenure, Integer iterations, String filename, int k) throws IOException {
		super(objFunction, tenure, iterations);
		this.instance = new Graph(filename);
		this.k = k;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<Vertex> makeCL() {
		ArrayList<Vertex> CL = new ArrayList<Vertex>();
		
		for(Vertex v: this.instance.V) {
			if(!this.bestSol.contains(v)) {
				CL.add(v);
			}
		}
		return CL;
	}

	@Override
	public ArrayList<Vertex> makeRCL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayDeque<Vertex> makeTL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCL() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Solution<Vertex> createEmptySol() {
		//Solution<Vertex> sol = new Solution<Vertex>();
		return  new Solution<Vertex>();
	}
	
	public Solution<Vertex> createRandomSol(){
		Solution<Vertex> sol = new Solution<Vertex>();
		int count =0;
		Random rng = new Random(0);
		while(count < this.k) {
			int sorteio = rng.nextInt(this.instance.V.size());
			if(!sol.contains(this.instance.V.get(sorteio))) {
				sol.add(this.instance.V.get(sorteio));
				count++;
			}
		}
		return sol;
	}

	@Override
	public Solution<Vertex> neighborhoodMove() {
		Double maxDeltaCost, deltaCost;
		Vertex bestCandIn = null, bestCandOut = null;
		Vertex fake = new Vertex(-1, -1);
		Solution<Vertex> aux = new Solution<Vertex>();
		

		maxDeltaCost = Double.NEGATIVE_INFINITY;
		updateCL();
		//Evaluate exchange
		for(Vertex in: CL) {
			for (Vertex out : this.incumbentSol) {
				deltaCost = this.instance.evaluateExchangeCost(in, out, aux);
				if(deltaCost > maxDeltaCost ) {
					maxDeltaCost = deltaCost;
					bestCandIn = in;
					bestCandOut = out;
				}
			}
			
		}
		
	
		// Implement the best non-tabu move
		TL.poll();
		if (bestCandOut != null) {
			incumbentSol.remove(bestCandOut);
			CL.add(bestCandOut);
			TL.add(bestCandOut);
		} else {
			TL.add(fake);
		}
		TL.poll();
		if (bestCandIn != null) {
			incumbentSol.add(bestCandIn);
			CL.remove(bestCandIn);
			TL.add(bestCandIn);
		} else {
			TL.add(fake);
		}
		ObjFunction.evaluate(incumbentSol);
		
		return null;
	}
	
	public static void main(String[] args) {
		//TS_Influence tb = new TS_Influence(objFunction, tenure, iterations, filename, k)
	}

}
