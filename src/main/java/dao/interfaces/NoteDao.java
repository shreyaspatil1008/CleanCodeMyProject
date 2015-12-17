package main.java.dao.interfaces;

import java.util.List;

import org.springframework.dao.DataAccessException;

import main.java.model.Note;
import main.java.model.exception.DataPersistanceException;

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
	Note saveNoteAndReturnSavedNote(Note note) throws DataPersistanceException ;
    Note getNoteById(Long notesId) throws DataAccessException ;
    List<Note> getAllNotesOfUser(Long userId) throws DataAccessException ;
    void updateNote(Note note) throws DataPersistanceException ;
    void deleteNote(Note note) throws DataPersistanceException ;

}
