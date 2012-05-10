package j2me.web;

import j2me.midlet.MovieMidlet;
import j2me.model.MovieDTO;

import javax.microedition.io.Connector;

public class HttpMovieService extends Thread{

	public static final String FIND_ALL_MOVIES = "findAllMovies";
	public static final String FIND_MOVIE_BY_ID = "findMovieById";
	public static final String ADD_NEW_MOVIE = "addMovie";
	
	private MovieMidlet movieMidlet;
	private MovieDTO movieDTO;
	
	public HttpMovieService(MovieMidlet movieMidlet, String threadName, MovieDTO movieDTO) {
		super(threadName);
		this.movieMidlet = movieMidlet;
		this.movieDTO = movieDTO;
		start();
	}

	public void run() {
	
		Thread currentThread = Thread.currentThread();
		if(currentThread.getName().equals( FIND_ALL_MOVIES)){
			String url = movieMidlet.getAppProperty(FIND_ALL_MOVIES);
			invokeRestService(url, FIND_ALL_MOVIES, null, Connector.READ);
		}else if(currentThread.getName().equals(FIND_MOVIE_BY_ID)){
			String url = movieMidlet.getAppProperty(FIND_MOVIE_BY_ID);
			url += movieDTO.getMovieId();
			invokeRestService(url, FIND_MOVIE_BY_ID, movieDTO, Connector.READ);
		}else if(currentThread.getName().equals(ADD_NEW_MOVIE)){
			String url = movieMidlet.getAppProperty(ADD_NEW_MOVIE);
			invokeRestService(url, ADD_NEW_MOVIE, movieDTO, Connector.READ_WRITE);
		}
	}

	private void invokeRestService(String url, String restServiceName, MovieDTO movieDTO, int httpMode) {
		HttpConnector connector = new HttpConnector();
		
		try {
			connector.init(movieMidlet, url, httpMode, restServiceName, movieDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}
