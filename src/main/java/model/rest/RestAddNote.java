package main.java.model.rest;

/**
 * This is Rest Model for Note Add Service with below attributes
 * 1. Note Title
 * 2. Note String
 * @Author shreyas patil
 */
public class RestAddNote {
	/**
	 * @Author shreyas patil
	 */
	private String noteTitle;
	private String noteString;
	
	public String getNoteTitle() {
		return noteTitle;
	}
	
	public void setNoteTitle(String title) {
		this.noteTitle = title;
	}
	
	public String getNoteString() {
		return noteString;
	}
	
	public void setNoteString(String note) {
		this.noteString = note;
	}
}
