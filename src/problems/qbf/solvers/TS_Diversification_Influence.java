package problems.qbf.solvers;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import metaheuristics.tabusearch.AbstractTS;
import problems.influence.Graph;
import problems.influence.Vertex;
import solutions.Solution;

public class TS_Diversification_Influence extends AbstractTS<Vertex> {
	
	public Graph instance;
	public int k;
	public int[] count;
	

	public TS_Diversification_Influence(String filename, Integer tenure, Integer iterations, int k) throws IOException {
		super(new Graph(filename), tenure, iterations);
		this.instance = new Graph(filename);
		this.count = new int[this.instance.V.size()+1];
		this.k = k;
	}

	public void updateCount() {
		
		for (Vertex v: this.bestSol) {
			count[v.id]++;
		}
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
		
	}
	
	public void diversification() {
		ArrayList<Integer> menores = new ArrayList<Integer>();
		menores.add(1);
		for (int i = 1; i < count.length; i++) {
			if(count[i] < count[menores.get(0)]) {
				menores =  new ArrayList<Integer>();
				menores.add(i);
			}else if(count[i] == count[menores.get(0)] && i!= 1 ) {
				menores.add(i);
			}
		}
		
		
		Vertex elemIn = this.instance.V.get(menores.get(new Random().nextInt(menores.size())));
		Vertex elemOut = this.incumbentSol.get(new Random().nextInt(k));
		this.incumbentSol.remove(elemOut);
		this.incumbentSol.add(elemIn);
		this.incumbentSol.cost = this.incumbentCost = this.ObjFunction.evaluate(this.incumbentSol);
		this.TL.poll();
		TL.add(elemIn);
		
		for(Integer i: menores) {
			System.out.println("count ["+i + "] = "+count[i]);
		}
		
		for (int i = 0; i < count.length; i++) {
			count[i] = 0;
		}
	}


	@Override
	public Solution<Vertex> createEmptySol() {
		return  new Solution<Vertex>();
	}
	
	public Solution<Vertex> createRandomSol(){
		Solution<Vertex> sol = new Solution<Vertex>();
		int c = 0;
		Random rng = new Random();
		while(c < this.k) {
			int sorteio = rng.nextInt(this.instance.V.size());
			if(!sol.contains(this.instance.V.get(sorteio)) && this.instance.V.get(sorteio).arcs.size() >0 ) {
				sol.add(this.instance.V.get(sorteio));
				c++;
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
		long maxDurationInMilliseconds = 2 * 60 * 1000;
		boolean intime = true;
		int x = 0;
		for (int i = 0; i < iterations && intime ; i++) {
			neighborhoodMove();
			updateCount();
			if (bestSol.cost < incumbentSol.cost) {
				bestSol = new Solution<Vertex>();
				for(Vertex v: this.incumbentSol) {
					bestSol.add(new Vertex(v));
				}
				this.bestCost = this.bestSol.cost = this.incumbentSol.cost;
				if (verbose)
					System.out.println("(Iter. " + i + ") BestSol = " + bestSol);
			}
			
			if(System.currentTimeMillis() > startTime + maxDurationInMilliseconds) {
				intime = false;
			}
			
			x++;
			if(x >= 5) {
				diversification();
				x=0;
			}
		}

		return bestSol;
	}

	
	public static void main(String[] args) throws IOException {
		TS_Diversification_Influence tabusearch = new TS_Diversification_Influence("instances/scalefree_n100.imp", 10, 1000000, 10);
		Solution<Vertex> bestSol = tabusearch.solve();
		System.out.println("maxVal = " + bestSol);
		
	}

}
