package main.java.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.UnexpectedRollbackException;

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.util.HibernateUtil;

/**
 * A NoteDao implementation class with below operations
 * 1. save note
 * 2. get note by id
 * 3. get all notes for user
 * 4. update note
 * 5. delete note
 * @Author shreyas patil
 */
public class NoteDaoImpl implements NoteDao {
	private Session session;
	private Transaction transaction;

	public Note saveNoteAndReturnSavedNote(Note note) throws TransactionException {
		HibernateUtil.initializeSessionAndTransaction(session,transaction);
		saveNote(note);
		return note;
	}

	private void saveNote(Note note) throws TransactionException {
		try {
			saveAndCommitTransaction(note);
		} catch (TransactionException exception) {
			HibernateUtil.rollbackTransactionAndThrowException(exception,transaction);
		} finally {
			HibernateUtil.closeSession(session);
		}
	}

	private void saveAndCommitTransaction(Note note) {
		note.setId((Long) session.save(note));
		transaction.commit();
	}

	public Note getNoteById(Long notesId) throws DataAccessException {
		HibernateUtil.initializeSessionAndTransaction(session,transaction);
		return getNoteByIdWithTryCatchFinally(notesId);
	}

	private Note getNoteByIdWithTryCatchFinally(Long notesId) throws DataAccessException {
		try {
			return (Note) session.get(Note.class, notesId);
		} catch (DataAccessException exception) {
			throw returnDataAccessExceptionWithMessage(exception);
		} finally {
			HibernateUtil.closeSession(session);
		}
	}

	private DataAccessResourceFailureException returnDataAccessExceptionWithMessage(DataAccessException exception) {
		return new DataAccessResourceFailureException(exception.getMessage());
	}

	@SuppressWarnings("unchecked")
	public List<Note> getAllNotesOfUser(Long userId) throws DataAccessException {
		session = HibernateUtil.getSession();
		try {
			return (List<Note>) createAndReturnGetAllNotesOfUserQuery(userId).list();
		} catch (DataAccessException exception) {
			throw returnDataAccessExceptionWithMessage(exception);
		} finally {
			HibernateUtil.closeSession(session);
		}
	}

	private Query createAndReturnGetAllNotesOfUserQuery(Long userId) {
		Query query = session.createQuery("from note where userid = ?");
		query.setParameter(0, userId);
		return query;
	}

	public void updateNote(Note note) throws TransactionException {
		HibernateUtil.initializeSessionAndTransaction(session,transaction);
		updateNoteWithTryCatchFinally(note);
	}

	private void updateNoteWithTryCatchFinally(Note note) throws TransactionException {
		try {
			updateAndCommitTransaction(note);
		} catch (TransactionException exception) {
			HibernateUtil.rollbackTransactionAndThrowException(exception,transaction);
		} finally {
			HibernateUtil.closeSession(session);
		}
	}

	private void updateAndCommitTransaction(Note note) {
		session.update(note);
		transaction.commit();
	}

	public void deleteNote(Note note) throws TransactionException {
		HibernateUtil.initializeSessionAndTransaction(session,transaction);
		deleteNoteWithTryCatchFinally(note);
	}

	private void deleteNoteWithTryCatchFinally(Note note) throws TransactionException {
		try {
			deleteAndCommitTransaction(note);
		} catch (TransactionException exception) {
			HibernateUtil.rollbackTransactionAndThrowException(exception,transaction);
		} finally {
			HibernateUtil.closeSession(session);
		}
	}

	private void deleteAndCommitTransaction(Note note) {
		session.delete(note);
		transaction.commit();
	}
}
