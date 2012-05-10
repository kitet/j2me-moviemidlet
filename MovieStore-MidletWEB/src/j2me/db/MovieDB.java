package j2me.db;

import j2me.model.MovieDTO;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class MovieDB {

	private RecordStore recordStore = null;
	
	public MovieDB(String dbName){
		try {
			recordStore = RecordStore.openRecordStore(dbName, true);
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}
	
	/*public synchronized void addMovie(MovieDTO movie){
		String pack = movie.pack();
		try {
			recordStore.addRecord(pack.getBytes(), 0, pack.getBytes().length);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreFullException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}*/
	
	public void close(){
		try {
			recordStore.closeRecordStore();
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}
	
	public MovieDTO getMovie(int recordId){
		
		MovieDTO movie = null;
		try {
			byte[] record = recordStore.getRecord(recordId);
			String movieRecord = new String(record);
			movie = MovieDTO.unpackMovie(movieRecord);
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		return movie;
	}
	
	public synchronized String[] getListOfMovies(){
		
		RecordEnumeration enumerateRecords;
		String[] movies = null;
		try {
			enumerateRecords = recordStore.enumerateRecords(null, null, false);
			movies = new String[enumerateRecords.numRecords()];
			while(enumerateRecords.hasNextElement()){
				int nextRecordId = enumerateRecords.nextRecordId();
				MovieDTO movie = getMovie(nextRecordId);
				movies[nextRecordId-1] = movie.getName();
			}
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		}
		return movies;
	}
}
