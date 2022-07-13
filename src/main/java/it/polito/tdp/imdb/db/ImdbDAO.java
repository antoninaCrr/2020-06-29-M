package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Collegamento;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer,Director> idMap){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
				
				while (res.next()) {
					if(!idMap.containsKey(res.getInt("id"))) {
					Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
					idMap.put(director.getId(),director);
				
					}

			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Director> getDirectorsByYear(int anno){
		String sql = " SELECT DISTINCT d.id,d.first_name,d.last_name "
				+ "FROM directors d, movies_directors md, movies m "
				+ "WHERE d.id = md.director_id AND md.movie_id = m.id AND m.year = ?";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<Collegamento> getEdgesByYear(int anno,Map<Integer,Director> idMap){
		String sql = " WITH actor_director " // nome tab temporanea
				+ "AS "
				+ "(SELECT r.actor_id, md.director_id " // creaiamo una tab temporanea di tutti gli attori e i direttori impegnati in film del anno
				+ "FROM movies_directors md, roles r,  movies m "
				+ "WHERE m.id = r.movie_id AND m.id = md.movie_id AND m.year = ?) " // fine istruzioni di creazione di tab temporanea
				+ "SELECT ad1.director_id AS d1_id, ad2.director_id AS d2_id,COUNT(*) AS peso " 
				+ "FROM actor_director ad1, actor_director ad2 "
				+ "WHERE ad1.actor_id = ad2.actor_id AND  ad1.director_id > ad2.director_id " // faccio un join di due tab temporanee basato su id dell'attore
				+ "GROUP BY ad1.director_id, ad2.director_id";
		List<Collegamento> result = new ArrayList<Collegamento>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("d1_id")) && idMap.containsKey(res.getInt("d2_id"))){
					Collegamento coll = new Collegamento (idMap.get(res.getInt("d1_id")),idMap.get(res.getInt("d2_id")), res.getInt("peso"));
				    result.add(coll);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
				
	}
	
	
	
	
	
	
}
