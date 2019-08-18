package es.german.djtools.rekordboxtotraktor.rekordbox;

import java.util.List;

public class Track {
	String location;
	String fileName;
	int rating;
	
	List<RBCue> listCues;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<RBCue> getListCues() {
		return listCues;
	}

	public void setListCues(List<RBCue> listCues) {
		this.listCues = listCues;
	}

}
