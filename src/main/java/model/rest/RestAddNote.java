package main.java.model.rest;

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
