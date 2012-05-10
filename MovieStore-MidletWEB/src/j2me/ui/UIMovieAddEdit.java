package j2me.ui;

import j2me.midlet.MovieMidlet;
import j2me.model.MovieDTO;
import j2me.web.HttpMovieService;

import java.util.Date;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.TextField;

public class UIMovieAddEdit extends Form implements CommandListener{

	private MovieMidlet movieMidlet;
	private MovieDTO movie;
	
	private TextField nameField;
	private Gauge ratingGauge;
	private ChoiceGroup genreChoiceGroup;
	private DateField releaseYear;
	
	private Command backCmd, saveCmd, exitCmd;
	
	public UIMovieAddEdit(MovieMidlet movieMidlet, MovieDTO movie) {
		super((movie == null ? "Add New Movie" : movie.getName()));
		this.movieMidlet = movieMidlet;
		this.movie = movie;
		
		init();
	}
	
	public void init(){
		
		nameField = new TextField("Name", (movie == null ? "" : movie.getName()), 50, (movie == null ? TextField.ANY: TextField.UNEDITABLE));
		ratingGauge = new Gauge("Rating", true, 10, (movie == null ? 0 : movie.getRating()));
		String[] genreTypes = new String[]{"ACTION", "COMEDY", "HORROR", "ROMANCE"};
		genreChoiceGroup = new ChoiceGroup("Genre", ChoiceGroup.POPUP, genreTypes, null);
		if( movie != null){
			for( int i = 0 ; i < genreTypes.length; i++){
				if( genreTypes[i].equals(movie.getGenre())){
					genreChoiceGroup.setSelectedIndex(i, true);
				}
			}
		}
		releaseYear = new DateField("Release Year", DateField.DATE);
		releaseYear.setDate((movie == null ? new Date(): movie.getYearOfRelease()));
		
		backCmd = new Command("Back", Command.BACK, 5);
		saveCmd = new Command("Save", Command.SCREEN, 3);
		exitCmd = new Command("Exit", Command.EXIT, 5);
		
		append(nameField);
		append(ratingGauge);
		append(releaseYear);
		append(genreChoiceGroup);
		
		addCommand(backCmd);
		addCommand(saveCmd);
		addCommand(exitCmd);
		
		setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable displayable) {
		
		if(cmd.getCommandType() == Command.BACK){
			
			movieMidlet.displayMovieList();
			
		}else if(cmd.getCommandType() == Command.EXIT){
			
			movieMidlet.exitApp();
			
		}else{
			
			MovieDTO newMovie = getMovie();
			HttpMovieService movieService = new HttpMovieService(movieMidlet, HttpMovieService.ADD_NEW_MOVIE, newMovie);
/*			//TODO: http post movie add/update
			//movieMidlet.getMovieDB().addMovie(newMovie);
			movieMidlet.displayMovieList();*/
		}
	}
	
	public MovieDTO getMovie(){
		String movieName = nameField.getString();
		String movieGenre = genreChoiceGroup.getString(genreChoiceGroup.getSelectedIndex());
		int movieRating = ratingGauge.getValue();
		Date movieYearOfRelease = releaseYear.getDate();
		
		MovieDTO movie = new MovieDTO(movieName, movieRating, movieYearOfRelease, movieGenre);
		return movie;
	}

}
