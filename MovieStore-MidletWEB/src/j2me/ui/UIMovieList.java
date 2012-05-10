package j2me.ui;

import j2me.midlet.MovieMidlet;
import j2me.model.MovieDTO;
import j2me.web.HttpMovieService;

import java.util.Hashtable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;

public class UIMovieList extends List implements CommandListener{

	private MovieMidlet movieMidlet;
	private Command exitCmd, newMovieCmd;
	
	//Temporary data storage for retrieving key-value data
	private Hashtable movieTable = new Hashtable();

	public UIMovieList(MovieMidlet movieMidlet) {

		super("", List.IMPLICIT);

		this.movieMidlet = movieMidlet;

		init();

	}

	private void init() {
		
		setTicker(new Ticker("Select the movie you want to view"));
		setTitle("List of Movies");
		
		newMovieCmd = new Command("Add Movie", Command.ITEM, 3);
		exitCmd = new Command("Exit", Command.EXIT, 5);
		
		addCommand(newMovieCmd);
		addCommand(exitCmd);
		
		setCommandListener(this);
		
	}
	
	public void addMovieToList(MovieDTO movieDTO){
		append(movieDTO.getName(), null);
		movieTable.put(new String("" + (size())), movieDTO);
	}

	public void commandAction(Command cmd, Displayable arg1) {
		
		if(cmd.getCommandType() == Command.EXIT){
			
			movieMidlet.exitApp();
			
		}else if(cmd == List.SELECT_COMMAND){
			
			//Select a movie and show it in the view movie ui
			int sIdx = getSelectedIndex() + 1;
			MovieDTO movieDTO = (MovieDTO)movieTable.get(new String("" + sIdx));
			new HttpMovieService(movieMidlet, HttpMovieService.FIND_MOVIE_BY_ID, movieDTO);
			
		}else{	
			//Add new movie
			UIMovieAddEdit movieAddEdit = new UIMovieAddEdit(movieMidlet, null);
			movieMidlet.display(movieAddEdit);
		}
	}

	public void findAllMovies() {
		new HttpMovieService(movieMidlet, HttpMovieService.FIND_ALL_MOVIES, null);	
	}

}
