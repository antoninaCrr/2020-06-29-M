package it.polito.tdp.imdb.model;

public class Neighbour implements Comparable<Neighbour>{
	
	private Director d;
	private int peso;
	
	public Neighbour(Director d, int peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	
	public Director getD() {
		return d;
	}



	public void setD(Director d) {
		this.d = d;
	}



	public int getPeso() {
		return peso;
	}



	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Neighbour o) {
		// TODO Auto-generated method stub
		return o.getPeso()-this.peso;
	}

	@Override
	public String toString() {
		return d.getId()+"-"+d.getFirstName()+" "+d.getLastName()+"-"+" #ATTORI CONDIVISI "+peso;
	}
	
	
}
