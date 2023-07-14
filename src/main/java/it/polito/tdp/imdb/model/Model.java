package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	private Map<Integer, Director> directorIdMap;
	private Map<Integer, Actor> actorIdMap;
	


	
	
	public Model() {
		this.dao = new ImdbDAO();
		
		this.directorIdMap = new HashMap<Integer, Director>();
		this.actorIdMap = new HashMap<Integer, Actor>();
		
		
		//Popoliamo l'identity map, in caso ci servisse dopo
		List<Director> directors = this.dao.listAllDirectors();
		for (Director d : directors) {
			this.directorIdMap.put(d.getId(), d);
		}
		List<Actor> actors = this.dao.listAllActors();
		for (Actor a : actors) {
			this.actorIdMap.put(a.getId(), a);
		}
		
		
	}
	
	// svuota il grafo per crearne nel caso uno nuovo
	private void clearGraph() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);			
	}

	public void creaGrafo(int anno) {
		// TODO Auto-generated method stub
		
		clearGraph();
		//costruzione di un nuovo grafo
		this.grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		// per grafici orientati SimpleDirectedWeightedGraph

		//assegnazione dei vertici
		List<Director> vertici = this.dao.getVertici(anno, this.directorIdMap);
		Graphs.addAllVertices(this.grafo, vertici);
		
		//assegnazione degli archi
		// VERSIONE 2) aggreghiamo gli archi lato codice
		//Per ogni vertice del grafo, leggo tutti i suoi attori nell'anno
		List<DirectorActors> actorList = new ArrayList<DirectorActors>();
		for (Director d : vertici) {
			actorList.add(new DirectorActors(d, this.dao.getActorsYearDirectSpec(anno,d.getId())) );
		}

		// Doppio ciclo for, per verificare le coppie di retailers
		for (int i = 0; i<actorList.size(); i++) {
			for (int j = i+1; j < actorList.size(); j ++) {
				Director d1 = actorList.get(i).getDirector();
				Director d2 = actorList.get(j).getDirector();
				Set<Integer> actors1 = new HashSet<Integer>(actorList.get(i).getActors());
				Set<Integer> actors2 =  actorList.get(j).getActors();
				
				// rimuovo da actors1 tutti gli attori che non sono presenti in actors2
				actors1.retainAll(actors2);
				//se i due venditori hanno almeno Nmin prodotti in comune, 
				//aggiungo l'arco
				if (actors1.size()>= 1) {
					Graphs.addEdgeWithVertices(this.grafo, d1, d2, actors1.size());
				}
			}
		}
		
	}

	public int getNVertici() {
		// TODO Auto-generated method stub
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		// TODO Auto-generated method stub
		return this.grafo.edgeSet().size();
	}
	
	
	/**
	 * Metodo che restituisce una lista di vertici dell'arco
	 * @return
	 */
	public List<Director> getVertici(){
		return new ArrayList<Director>(this.grafo.vertexSet());
	}

	public List<Director> getAdiacenze(Director director) {
		// TODO Auto-generated method stub
		return Graphs.neighborListOf(this.grafo, director);
	}
	
	
	public int getWeightGivenTwoVertexes(Director d1, Director d2){
		
		return (int) this.grafo.getEdgeWeight(this.grafo.getEdge(d1, d2));
		
	}
	

}
