package it.polito.tdp.imdb.model;

import it.polito.tdp.imdb.db.ImdbDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;


public class Model {
	
	private ImdbDAO dao;
	private Graph<Director,DefaultWeightedEdge> grafo;
	private Map<Integer, Director> idMap;
	
	private List<Director> listaMigliore;
	
	public Model() {
		//super();
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		dao.listAllDirectors(idMap);
	}
	
	public String creaGrafo(int anno) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getDirectorsByYear(anno));
		
		// aggiungo gli archi
		for(Collegamento ci: dao.getEdgesByYear(anno, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, ci.getD1(), ci.getD2(),ci.getPeso());
		}
		
		
		return "Grafo creato con "+this.grafo.vertexSet().size()+" vertici"+ " e "+ this.grafo.edgeSet().size()+" archi";
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Director> getAllVertices(){
		return new ArrayList<Director>(this.grafo.vertexSet());
	}
	
	public List<Neighbour> getAllNeighbours(Director d){
		List<Director> direttori = new ArrayList<Director>(Graphs.neighborListOf(this.grafo, d));
		List <Neighbour> vicini = new ArrayList<>();
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(d.equals(this.grafo.getEdgeSource(e))) {
				for(Director di: direttori) {
					if(di.equals(this.grafo.getEdgeTarget(e))) {
						Neighbour n = new Neighbour(di,(int)(this.grafo.getEdgeWeight(e)));
						vicini.add(n);
					}
				}
				
			}
			else if (d.equals(this.grafo.getEdgeTarget(e))) {
				for(Director di: direttori) {
					if(di.equals(this.grafo.getEdgeSource(e))) {
						Neighbour n = new Neighbour(di,(int)(this.grafo.getEdgeWeight(e)));
						vicini.add(n);
					}
				}
				
			}
		
		}
		
		Collections.sort(vicini);
		
		return vicini;
		
	}
	
  public List<Director> startRicorsione(Director partenza, int max){
	  List<Director> parziale = new ArrayList<>();
	  listaMigliore = new ArrayList<>();
	  parziale.add(partenza);
	  cerca(parziale,1,max);
	  return listaMigliore;
  }

  private void cerca(List<Director> parziale, int i, int max) {
	// TODO Auto-generated method stub
	  
	  Director d = parziale.get(parziale.size()-1);
	  if(parziale.size() > listaMigliore.size()) {
			listaMigliore = new ArrayList<>(parziale); // SOVRASCRIVO LA LISTA MIGLIORE
	  }
			
		// passo base a cui aggiungiamo dei filtri per costruire soluzioni valide
		for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(d)) { // questo for prima o poi si esaurisce (prima condizione di terminazione implicita)
			// filtro : facciamo questa ricorsione solo se ci porta in giuste direzioni
		    Director prossimo = Graphs.getOppositeVertex(this.grafo, e, d);
			if(!parziale.contains(prossimo) &&
					(sommaPeso(parziale)+ (int)this.grafo.getEdgeWeight(e)) <= max) { // controllare che l'oggetto t abbia hashCode e equals implementati 
			parziale.add(prossimo);
			cerca(parziale,i+1,max);
			// parziale.remove(t); // funziona poichè so che questo oggetto è unico nella mia lista di valide
			parziale.remove(parziale.size()-1); // USARE QUESTO METODO PER RIMUOVERE QUANDO LAVORIAMO CON LE LISTE
			}
			
		}
}

public int sommaPeso(List<Director> parziale) {
	// TODO Auto-generated method stub
	int somma = 0;
	for(Director d: parziale) {
		for(Director di: parziale) {
			if(!d.equals(di)) {
				DefaultWeightedEdge e = this.grafo.getEdge(d, di);
				somma += (int) this.grafo.getEdgeWeight(e);
			}
			
		}
	}
	
	return somma;
}
  
  
	
	
	

}
