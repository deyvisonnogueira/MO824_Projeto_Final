package problems.qbf.solvers;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import metaheuristics.tabusearch.AbstractTS;
import problems.Evaluator;
import problems.influence.Graph;
import problems.influence.RelaxGraph;
import problems.influence.Vertex;
import solutions.Solution;

public class TS_Surrogate_Influence extends AbstractTS<Vertex> {
	
	public RelaxGraph instance;
	public int k;
	private boolean relax;
	protected Evaluator<Vertex> relaxObjFunction;

	public TS_Surrogate_Influence(String filename, Integer tenure, Integer iterations, int k) throws IOException {
		super(new Graph(filename), tenure, iterations);
		this.instance = new RelaxGraph(filename);
		this.relax = false;
		this.k = k;
	}
	
	
	

	public boolean isRelax() {
		return relax;
	}




	public void setRelax(boolean relax) {
		this.relax = relax;
	}




	@Override
	public ArrayList<Vertex> makeCL() {
		ArrayList<Vertex> CL = new ArrayList<Vertex>();
		
		for(Vertex v: this.instance.V) {
			if(!this.bestSol.contains(v) && v.arcs.size() > 0) {
				CL.add(v);
			}
		}
		return CL;
	}

	@Override
	public ArrayList<Vertex> makeRCL() {
		
		return CL;
	}

	@Override
	public ArrayDeque<Vertex> makeTL() {
	  ArrayDeque<Vertex> TL = new ArrayDeque<Vertex>(this.tenure*2);
        
        for(int i=0; i< this.tenure*2;i++) {
        	TL.add(new Vertex(-1,0));
        }
        return TL;
	}

	@Override
	public void updateCL() {
		for(Vertex v: this.instance.V) {
			if(!this.bestSol.contains(v) && v.arcs.size() > 0) {
				CL.add(v);
			}
		}
	}

	@Override
	public Solution<Vertex> createEmptySol() {
		return  new Solution<Vertex>();
	}
	
	public Solution<Vertex> createRandomSol(){
		Solution<Vertex> sol = new Solution<Vertex>();
		int count = 0;
		Random rng = new Random();
		while(count < this.k) {
			int sorteio = rng.nextInt(this.instance.V.size());
			if(!sol.contains(this.instance.V.get(sorteio)) && this.instance.V.get(sorteio).arcs.size() >0 ) {
				sol.add(this.instance.V.get(sorteio));
				count++;
			}
		}
		
	
		sol.cost = this.ObjFunction.evaluate(sol);
		return sol;
	}

	@Override
	public Solution<Vertex> neighborhoodMove() {
		Double minDeltaCost, deltaCost;
		Vertex bestCandIn = null, bestCandOut = null;
		Vertex fake = new Vertex(-1, -1);
		

		minDeltaCost = Double.POSITIVE_INFINITY;
		updateCL();
		//Evaluate exchange
		for(Vertex in: CL) {
			for (Vertex out : this.incumbentSol) {
				deltaCost = this.instance.evaluateExchangeCost(in, out, this.incumbentSol);
				if(deltaCost < minDeltaCost ) {
					minDeltaCost = deltaCost;
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
		incumbentSol.cost = this.incumbentCost =  ObjFunction.evaluate(incumbentSol);
		
		return null;
	}
	
	public Solution<Vertex> solve() {
		
		
		this.bestSol = createRandomSol();
		this.bestSol.cost = this.ObjFunction.evaluate(bestSol);
		this.incumbentSol = new Solution<Vertex>();
		for(Vertex v: this.bestSol) {
			incumbentSol.add(new Vertex(v));
		}
		
		this.bestCost = this.incumbentCost = this.incumbentSol.cost = this.bestSol.cost = this.ObjFunction.evaluate(bestSol);
		CL = makeCL();
		TL = makeTL();		
		long startTime = System.currentTimeMillis();
		long maxDurationInMilliseconds = 30 * 60 * 1000;
		boolean intime = true;
		
		for (int i = 0; i < iterations && intime ; i++) {
			neighborhoodMove();
			if (bestSol.cost < incumbentSol.cost) {
				bestSol = new Solution<Vertex>();
				for(Vertex v: this.incumbentSol) {
					bestSol.add(new Vertex(v));
				}
				this.bestCost = this.bestSol.cost = this.incumbentSol.cost;
				if (verbose)
					System.out.println("(Iter. " + i + ") BestSol = " + bestSol);
			}
			System.out.println("(Iter. " + i + ")");
			if(System.currentTimeMillis() > startTime + maxDurationInMilliseconds) {
				intime = false;
			}
		}

		return bestSol;
	}

	
	public static void main(String[] args) throws IOException {
		TS_Surrogate_Influence tabusearch = new TS_Surrogate_Influence("instances/scalefree_n"+args[0]+".imp", 10, 1000000, Integer.valueOf(args[1]));
		Solution<Vertex> bestSol = tabusearch.solve();
		System.out.println("maxVal = " + bestSol);
		
	}

}