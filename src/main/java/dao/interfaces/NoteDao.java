package main.java.dao.interfaces;

import java.util.List;

import main.java.model.Note;

/**
 * A NoteDao interface class with below operations
 * 1. save note
 * 2. get note by id
 * 3. get all notes for user
 * 4. update note
 * 5. delete note
 * @Author shreyas patil
 */
public interface NoteDao {
	Note saveNoteAndReturnSavedNote(Note note);
    Note getNoteById(Long notesId);
    List<Note> getAllNotesOfUser(Long userId);
    void updateNote(Note note);
    void deleteNote(Note note);

}
