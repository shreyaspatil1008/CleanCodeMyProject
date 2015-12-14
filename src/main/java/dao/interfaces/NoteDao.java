package main.java.dao.interfaces;

import java.util.List;

import main.java.model.Note;

public interface NoteDao {
	
	/**
	 * @Author shreyas patil
	 */
	Note saveNoteAndReturnSavedNote(Note note);
    Note getNoteById(Long notesId);
    List<Note> getAllNotesOfUser(Long userId);
    void updateNote(Note note);
    void deleteNote(Note note);

}
