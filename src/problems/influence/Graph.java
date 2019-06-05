package problems.influence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;

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
	
	public void propagation() {
		
		ArrayList<Vertex> influenciadores = new ArrayList<Vertex>();
		Graph g = new Graph();
		for(Vertex v: this.V) {
			g.V.add(new Vertex(v));
		}
		for(Arc a: this.A) {
			g.A.add((new Arc(a)));
		}
		
		for(Vertex v: this.active) {
			influenciadores.add(v);
			g = v.influencia(g);
			
			//AQUI
		}
		
		
		
	}
	
	
	
	public static void main(String[] args) throws IOException {
		Graph g = new Graph("instances/scalefree_n10.imp");
		g.active.add(g.V.get(0));
		for(Vertex v: g.V) {
			System.out.println(v.toString());
		}
		g.propagation();
	}
}
