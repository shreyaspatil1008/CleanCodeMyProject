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

public class NoteDaoImpl implements NoteDao{
	
	/**
	 * @Author shreyas patil
	 */
	private Session session;
	private Transaction transaction;
	
	//public methods section
	public Note saveNoteAndReturnSavedNote(Note note)throws TransactionException{
		session = getSession();
        transaction = session.beginTransaction();
		saveNote(note);
		return note;
    }

	public Note getNoteById(Long notesId) throws DataAccessException{
        session = getSession();
        session.beginTransaction();
        return getNoteByIdWithTryCatchFinally(notesId);
    }

	@SuppressWarnings("unchecked")
	public List<Note> getAllNotesOfUser(Long userId) throws DataAccessException{
        session = getSession();
        try{
            return (List<Note>)createAndReturnGetAllNotesOfUserQuery(userId).list();
        }catch(DataAccessException exception){
        	throw returnDataAccessExceptionWithMessage(exception);
        }finally{
            closeSession();
        }
    }

	public void updateNote(Note note)throws TransactionException{
        session = getSession();
        transaction = session.beginTransaction();
        updateNoteWithTryCatchFinally(note);
    }

	public void deleteNote(Note note)throws TransactionException{
        session = getSession();
        transaction = session.beginTransaction();
        deleteNoteWithTryCatchFinally(note);
    }

	//private methods section
	private void saveNote(Note note) throws TransactionException{
		try{
        	saveAndCommitTransaction(note);
        }catch(TransactionException exception){
            rollbackTransactionAndThrowException(exception);
        }finally{
            closeSession();
        }
	}

	private void saveAndCommitTransaction(Note note) {
		note.setId((Long)session.save(note));
		transaction.commit();
	}

	private Session getSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}

    private Note getNoteByIdWithTryCatchFinally(Long notesId) throws DataAccessException{
		try{
            return (Note)session.get(Note.class,notesId);
        }catch(DataAccessException exception){
            throw returnDataAccessExceptionWithMessage(exception);
        }finally{
            closeSession();
        }
	}

	private DataAccessResourceFailureException returnDataAccessExceptionWithMessage(DataAccessException exception) {
		return new DataAccessResourceFailureException(exception.getMessage());
	}

    private Query createAndReturnGetAllNotesOfUserQuery(Long userId) {
		Query query = session.createQuery("from note where userid = ?");
		query.setParameter(0,userId);
		return query;
	}

	private void updateNoteWithTryCatchFinally(Note note) throws TransactionException{
		try{
            updateAndCommitTransaction(note);
        }catch(TransactionException exception){
            rollbackTransactionAndThrowException(exception);
        }finally {
            closeSession();
        }
	}

	private void updateAndCommitTransaction(Note note) {
		session.update(note);
		transaction.commit();
	}

	private void rollbackTransactionAndThrowException(TransactionException exception) {
		transaction.rollback();
		throw returnTransactionExceptionWithMessage(exception);
	}

	private UnexpectedRollbackException returnTransactionExceptionWithMessage(TransactionException exception) {
		return new UnexpectedRollbackException(exception.getMessage());
	}
	
    private void deleteNoteWithTryCatchFinally(Note note) throws TransactionException{
		try{
            deleteAndCommitTransaction(note);
        }catch(TransactionException exception){
            rollbackTransactionAndThrowException(exception);
        }finally {
            closeSession();
        }
	}

	private void deleteAndCommitTransaction(Note note) {
		session.delete(note);
		transaction.commit();
	}
	
	private void closeSession() {
		session.close();
	}
}
