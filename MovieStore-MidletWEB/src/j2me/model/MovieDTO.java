package j2me.model;

import gov.nist.core.StringTokenizer;

import java.util.Date;

public class MovieDTO {

	private int movieId;
	private String name;
	private int rating;
	private Date yearOfRelease;
	private String genre;
	
	public MovieDTO(){
		
	}
	
	public MovieDTO(String name, int rating, Date yearOfRelease, String genre) {
		this.name = name;
		this.rating = rating;
		this.yearOfRelease = yearOfRelease;
		this.genre = genre;
	}
	
	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRating() {
		return rating;
	}
	
	public void setRating(String ratingToken){
		ratingToken = ratingToken.substring(0, ratingToken.length()-1);
		int rating = Integer.parseInt(ratingToken);
		this.rating = rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Date getYearOfRelease() {
		return yearOfRelease;
	}
	public void setYearOfRelease(Date yearOfRelease) {
		this.yearOfRelease = yearOfRelease;
	}
	
	public void setYearOfRelease(String yearOfReleaseToken){
		yearOfReleaseToken = yearOfReleaseToken.substring(0, yearOfReleaseToken.length()-1);
		long parseLong = Long.parseLong(yearOfReleaseToken);
		Date d = new Date(parseLong);
		this.yearOfRelease = d;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public static MovieDTO unpackMovie(String record){
		StringTokenizer strToken = new StringTokenizer(record, ';');
		MovieDTO movie = new MovieDTO();
		String movieName = strToken.nextToken();
		movieName = movieName.substring(0, movieName.length()-1);
		movie.setName(movieName);
		movie.setRating(strToken.nextToken());
		movie.setYearOfRelease(strToken.nextToken());
		movie.setGenre(strToken.nextToken());
		return movie;
	}

	public String toString() {
		return "MovieDTO [movieId=" + movieId + ", name=" + name + ", rating="
				+ rating + ", yearOfRelease=" + yearOfRelease + ", genre="
				+ genre + "]";
	}

	public String toXML(MovieDTO movie) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<movie>");
		stringBuffer.append("<id>");
		stringBuffer.append(movie.getMovieId());
		stringBuffer.append("</id>");
		stringBuffer.append("<genre>");
		stringBuffer.append(movie.getGenre());
		stringBuffer.append("</genre>");
		stringBuffer.append("<rating>");
		stringBuffer.append(movie.getRating());
		stringBuffer.append("</rating>");
		stringBuffer.append("<name>");
		stringBuffer.append(movie.getName());
		stringBuffer.append("</name>");
		stringBuffer.append("<release>");
		stringBuffer.append(movie.getYearOfRelease());
		stringBuffer.append("</release>");
		stringBuffer.append("</movie>");
		return stringBuffer.toString();
	}
	
	
}
