package main.java.model.rest;

public class RestUpdateNote {

	/**
	 * @Author shreyas patil
	 */
	private Long id;
	private String noteTitle;
	private String noteString;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
