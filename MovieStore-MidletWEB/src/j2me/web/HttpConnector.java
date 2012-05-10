package j2me.web;

import j2me.midlet.MovieMidlet;
import j2me.model.MovieDTO;
import j2me.ui.UIMovieAddEdit;
import j2me.util.Utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class HttpConnector {

	public void init(MovieMidlet movieMidlet, String url, int httpMode,
			String operationType, MovieDTO movieDTO) throws Exception {

		HttpConnection httpConnection = null;
		InputStream inputStream = null;
		try {
			httpConnection = (HttpConnection) Connector.open(url, httpMode, true);
			switch(httpMode){
				case Connector.READ:
									inputStream = httpConnection.openInputStream();
									KXmlParser xmlParser = new KXmlParser();
									xmlParser.setInput(new InputStreamReader(inputStream));
									
									if (operationType.equals(HttpMovieService.FIND_ALL_MOVIES)) {
										parseFindAllMovies(movieMidlet, xmlParser);
									} else if (operationType.equals(HttpMovieService.FIND_MOVIE_BY_ID)) {
										parseFindMovieById(movieMidlet, xmlParser);
									}
									break;
				case Connector.READ_WRITE:
									post(movieMidlet, httpConnection, movieDTO);
									break;
			}			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			if(inputStream!= null){
				inputStream.close();
			}
			httpConnection.close();
		}
	}

	private void parseFindMovieById(MovieMidlet movieMidlet,
			KXmlParser xmlParser) throws NumberFormatException,
			XmlPullParserException, IOException, Exception {

		MovieDTO movieDTO = new MovieDTO();
		xmlParser.nextTag();
		xmlParser.require(XmlPullParser.START_TAG, null, "movie");
		int xmlElementTag = 0;
		while (xmlParser.nextTag() != XmlPullParser.END_TAG) {
			switch (xmlElementTag) {
			case 0:
				movieDTO.setGenre(xmlParser.nextText());
				break;
			case 1:
				movieDTO.setMovieId(Integer.parseInt(xmlParser.nextText()));
				break;
			case 2:
				movieDTO.setName(xmlParser.nextText());
				break;
			case 3:
				movieDTO.setRating(Integer.parseInt(xmlParser.nextText()));
				break;
			case 4:
				String date = xmlParser.nextText();
				Date d = Utility.parseDate(date);
				movieDTO.setYearOfRelease(d);
				xmlElementTag = -1;
				UIMovieAddEdit movieAddEdit = new UIMovieAddEdit(movieMidlet, movieDTO);
				movieMidlet.display(movieAddEdit);
				break;
			}
			xmlElementTag++;

		}
	}

	private void parseFindAllMovies(MovieMidlet movieMidlet,
			KXmlParser xmlParser) throws XmlPullParserException, IOException,
			Exception {

		MovieDTO movieDTO = new MovieDTO();

		xmlParser.nextTag();
		xmlParser.require(XmlPullParser.START_TAG, null, "movieList");

		int xmlElementTag = 0;
		while (xmlParser.nextTag() == XmlPullParser.START_TAG) {

			switch (xmlElementTag) {
			case 0:
				xmlParser.require(XmlPullParser.START_TAG, null, "movie");
				movieDTO = new MovieDTO();
				break;
			case 1:
				movieDTO.setGenre(xmlParser.nextText());
				break;
			case 2:
				movieDTO.setMovieId(Integer.parseInt(xmlParser.nextText()));
				break;
			case 3:
				movieDTO.setName(xmlParser.nextText());
				break;
			case 4:
				movieDTO.setRating(Integer.parseInt(xmlParser.nextText()));
				break;
			case 5:
				String date = xmlParser.nextText();
				Date d = Utility.parseDate(date);
				movieDTO.setYearOfRelease(d);
				xmlElementTag = -1;
				xmlParser.nextTag();
				movieMidlet.getUiMovieList().addMovieToList(movieDTO);
				break;
			}
			xmlElementTag++;

		}
	}

	public void post(MovieMidlet movieMidlet, HttpConnection httpConnection,  MovieDTO movieDTO){
		
		try {
			String msg = movieDTO.toXML(movieDTO);
			//System.out.println("Content Length: " + msg.getBytes().length);
			/*httpConnection.setRequestMethod(HttpConnection.POST);
			httpConnection.setRequestProperty("Content-Type","application/xml");
			httpConnection.setRequestProperty("User-Agent","Profile/MIDP-2.0 Configuration/CLDC-1.1");
			//httpConnection.setRequestProperty("Accept-Charset","UTF-8;q=0.7,*;q=0.7");
			/*httpConnection.setRequestProperty("Content-Length", (Integer.toString(msg.getBytes().length)));     
			httpConnection.setRequestProperty("Transfer-encoding", "chunked");
			*/
			
			httpConnection.setRequestProperty("Content-Type","application/xml");
			httpConnection.setRequestProperty("Content-Length",String.valueOf(msg.getBytes("UTF-8").length));        
			httpConnection.setRequestProperty("User-Agent","Profile/MIDP-2.0 Configuration/CLDC-1.1");
			httpConnection.setRequestProperty("Accept-Charset","UTF-8;q=0.7,*;q=0.7");
			httpConnection.setRequestProperty("Accept-Encoding","gzip, deflate");
			httpConnection.setRequestProperty("Accept","text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");        
			httpConnection.setRequestMethod(HttpConnection.POST);

			
			//System.out.println("" + httpConnection.getRequestProperty("Content-Length"));
			// After this point, any call to http.setRequestProperty() will
			// cause an IOException
			//OutputStream out = httpConnection.openOutputStream();
			OutputStreamWriter dout = new OutputStreamWriter( httpConnection.openDataOutputStream(), "UTF-8" );
			
			/*dout.write( "Content-Disposition: application/octet-stream;".getBytes() );
			dout.write( "Content-Type:application/octet-stream".getBytes() );
			dout.write( ( "Content-Length:" + msg.getBytes().length  ).getBytes() );
			*/
			dout.write( msg );
			dout.close();
			
			int responseCode = httpConnection.getResponseCode();
			//System.out.println("RespCode : " + responseCode);
			//dout.flush();

/*			out.write()
			out.write(msg.getBytes());
			out.flush();*/
			
			StringBuffer sb = new StringBuffer();
		      InputStream is = httpConnection.openDataInputStream();
		      int chr;
		      while ((chr = is.read()) != -1)
		        sb.append((char) chr);

		      // Web Server just returns the birthday in mm/dd/yy format.
		      //System.out.println(sb.toString());
		     movieMidlet.displayMovieList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
