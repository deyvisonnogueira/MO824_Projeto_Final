package problems.influence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Random;

public class Graph {
	public ArrayList<Vertex> V;
	public ArrayList<Arc> A;
	public ArrayList<Vertex> active;
	
	public Graph() {
		this.active = new ArrayList<Vertex>();
		this.V = new ArrayList<Vertex>();
		this.A = new ArrayList<Arc>();
	}
	
	public Graph(String filename) throws IOException {
		this.V = new ArrayList<Vertex>();
		this.A = new ArrayList<Arc>();
		this.active = new ArrayList<Vertex>();
		Reader fileInst = new BufferedReader(new FileReader(filename));
		StreamTokenizer stok = new StreamTokenizer(fileInst);
		

		
		stok.nextToken();
		stok.nextToken();
		stok.nextToken();
		stok.nextToken();
		
		int n = (int) stok.nval;
		stok.nextToken();
		int m = (int) stok.nval;
		stok.nextToken();
		stok.nextToken();
		stok.nextToken();
		stok.nextToken();
		
		for (int i = 0; i < n; i++) {
			int id = (int) stok.nval;
			stok.nextToken();
			int threshold = (int) stok.nval;
			stok.nextToken();
			V.add(new Vertex(id,threshold));
		}
		
		stok.nextToken();
		stok.nextToken();
		stok.nextToken();
		
		for (int i = 0; i < m; i++) {
			int source = (int) stok.nval;
			stok.nextToken();
			int dest = (int) stok.nval;
			stok.nextToken();
			int influence = (int) stok.nval;
			stok.nextToken();
			this.A.add(new Arc(source, dest, influence));
		}
		
		for(Vertex v: this.V) {
			for(Arc a: this.A) {
				if(v.id == a.source) {
					v.addArc(a);
				}
			}
		}
	}
	
	public Graph propagation() {
		
		
		Graph g = new Graph();
		for(Vertex v: this.V) {
			g.V.add(new Vertex(v));
		}
		for(Arc a: this.A) {
			g.A.add((new Arc(a)));
		}
		for(Vertex v: this.active) {
			g.active.add(v);
		}
		
	
		while(!g.active.isEmpty()) { 
			Vertex v = this.active.get(new Random().nextInt(this.active.size()));
			g = v.influencia(g);
		}	
		return g;	
	}
	
	public void removeVertexById(int id) {
		for(Vertex v: this.V) {
			if(v.id == id) {
				this.V.remove(v);
				break;
			}
			
		}
		for(Vertex v: this.active) {
			if(v.id == id) {
				this.active.remove(v);
				break;
			}
		}
		for(int i=0;i<this.A.size();i++) {
			if(this.A.get(i).source == id || this.A.get(i).dest == id) {
				this.A.remove(i);
				
			}
		}
		
	}
	
	public void decreaseThreashold(int id){
		for(Vertex v: this.V) {
			if(v.id == id) {
				if(v.threshold > 0) {
					v.threshold--;
				}else {
					System.out.println("Threshold isnt positive: "+v.toString());
				}
				break;
			}
		}
		//System.out.println("Vertex "+ id+" not found");
	}
	
	
	
	public static void main(String[] args) throws IOException {
		Graph g = new Graph("instances/scalefree_n10.imp");
		g.active.add(g.V.get(0));
		Graph result =  g.propagation();
		for(Vertex v: result.V) {
			System.out.println(v.toString());
		}
		System.out.println("actives: "+result.V.size());
		
		
	}
}
