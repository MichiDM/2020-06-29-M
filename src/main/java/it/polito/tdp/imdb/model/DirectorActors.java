package it.polito.tdp.imdb.model;


import java.util.Objects;
import java.util.Set;

public class DirectorActors {
	
	private Director director;
	private Set<Integer> actors;
	
	
	public DirectorActors(Director director, Set<Integer> actors) {
		super();
		this.director = director;
		this.actors = actors;
	}


	public Director getDirector() {
		return director;
	}


	public void setDirector(Director director) {
		this.director = director;
	}


	public Set<Integer> getActors() {
		return actors;
	}


	public void setActors(Set<Integer> actors) {
		this.actors = actors;
	}


	@Override
	public int hashCode() {
		return Objects.hash(actors, director);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectorActors other = (DirectorActors) obj;
		return Objects.equals(actors, other.actors) && Objects.equals(director, other.director);
	}
	
	

}
