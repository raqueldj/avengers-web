package io.avengers.ws;

import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.avengers.domain.Hero;
import io.avengers.domain.Movie;
import io.avengers.service.HeroService;
import io.avengers.service.MovieService;

@Path("movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {
	@GET
	public Set<Movie> getAllMovies(){
		MovieService mService = new MovieService();
		return mService.findAll();
	}
	
	@GET
	@Path("{id}")
	public Movie getMovieById(@PathParam("id") int id){
		MovieService mService = new MovieService();
		return mService.findMovie(id);
	}
	@POST
	public Response createMovie(Movie movie){
		if(movie==null || movie.getMovie_title().isEmpty()){
			return Response.status(406).entity("\"Empty movie\"").build();
		}
		
		new MovieService().createMovie(movie);
		
		return Response.status(201).entity("\""+movie.getMovie_title()+"\"").build();
	}
	
	@POST
	@Path("{movieId}/{heroId}")
	public Response addHeroInMovie(@PathParam("movieId") int movieId, @PathParam("heroId") int heroId){
		Movie m = new MovieService().findMovie(movieId);
		Hero h = new HeroService().findHero(heroId);
		
		if(m==null || h==null){
			return Response.status(406).entity("\"Wrong movie or hero\"").build();
		}
		
		new MovieService().linkMovieToHero(m,h);
		
		return Response.status(201).entity("\""+m.getMovie_title()+" "+ h.getAlias()+"\"").build();
	}
	
	@DELETE
	@Path("{movieId}")
	public Response deleteMovie(@PathParam("movieId") int movieId){
		Movie movie = new MovieService().findMovie(movieId);
		
		if(movie==null){
			return Response.status(406).entity("\"Inexistant movie\"").build();
		}
		
		new MovieService().deleteMovie(movie);
		
		return Response.status(204).header("x-deleted", movie.getMovie_title()).build();
	}
}
