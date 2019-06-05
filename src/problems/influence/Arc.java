package problems.influence;

public class Arc {
	public int source;
	public int dest;
	public int influence;
	
	
	public Arc(int source, int dest, int influence) {
		super();
		this.source = source;
		this.dest = dest;
		this.influence = influence;
	}
	
	public Arc(Arc arc) {
		this.source = arc.source;
		this.dest = arc.dest;
		this.influence = arc.influence;
	}
	
	@Override
	public String toString() {
		return "[("+this.source+" -> "+this.dest+"), influence ="+this.influence+"]";
	}
	
	
	

}
