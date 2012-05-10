package j2me.midlet;

import j2me.ui.UIMovieList;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class MovieMidlet extends MIDlet {
	
	private Display display;
	//private MovieDB movieDB;
	private UIMovieList uiMovieList;

	public MovieMidlet() {
		//movieDB = new MovieDB("movieStore-RMS");
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		notifyDestroyed();
		//movieDB.close();
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {

		display = Display.getDisplay(this);
		displayMovieList();
		
	}
	
	public void display(Displayable displayable){
		display.setCurrent(displayable);
	}

	public void exitApp(){
		try {
			destroyApp(false);
		} catch (MIDletStateChangeException e) {
			e.printStackTrace();
		}
	}
	
	public UIMovieList getUiMovieList() {
		return uiMovieList;
	}	
	
	public void displayMovieList(){
		uiMovieList = new UIMovieList(this);
		uiMovieList.findAllMovies();
		display(uiMovieList);
	}
}
