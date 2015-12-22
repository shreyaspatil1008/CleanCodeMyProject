package main.java.service;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.dao.DataAccessException;

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.model.User;
import main.java.model.exception.DataPersistanceException;
import main.java.model.exception.NoteNotFoundException;
import main.java.model.exception.UserNotFoundException;
import main.java.model.rest.RestUpdateNote;
import main.java.util.AuthenticationUtil;
import main.java.util.ResponseUtil;

/**
 * A web service EndPoint class with below services
 * 1. Update Note
 * @Author shreyas patil
 */
@Path("/note")
public class UpdateNoteService {

	@Inject
	private User user;
	@Inject
	private NoteDao noteDao;
	@Inject
	private ResponseUtil responseUtil;
	@Inject
	private AuthenticationUtil authenticationUtil;

	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNote(RestUpdateNote updateNote, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdUpdateAndReturnResponse(updateNote, authString);
		}catch(IOException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataAccessException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataPersistanceException e){
			return responseUtil.buildFailureResponse("Update not successful");
		}catch(UserNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}catch(NoteNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}
	}
	
	private Response validateUserForAuthStringNoteIdUpdateAndReturnResponse(RestUpdateNote updateNote,
			String authString) throws IOException,DataAccessException,UserNotFoundException,NoteNotFoundException{
		if(null == updateNote.getId() || 0l == updateNote.getId())
			return responseUtil.buildFailureResponse("Note Id can not be empty.");
		else if(!authenticationUtil.isUserAuthenticated(authString,user))
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return validateNoteUpdateAndReturnResponse(updateNote);
	}

	private Response validateNoteUpdateAndReturnResponse(RestUpdateNote updateNote)throws NoteNotFoundException{
		Note note = findAndReturnNote(updateNote);
		return validateNoteUpdateAndReturnResponse(updateNote, note);
	}

	private Note findAndReturnNote(RestUpdateNote updateNote)throws NoteNotFoundException {
		for(Note curNote:user.getNotes()){
			if(curNote.getId().equals(updateNote.getId())){
				return curNote;
			}
		}
		throw new NoteNotFoundException("Note not found for note id"+updateNote.getId());
	}

	private Response validateNoteUpdateAndReturnResponse(RestUpdateNote updateNote, Note note) {
		if(note == null)
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return fillAndUpdateNoteAndReturnResponse(updateNote, note);
	}

	private Response fillAndUpdateNoteAndReturnResponse(RestUpdateNote updateNote, Note note) {
		fillNoteWithNoteStringNoteTitleNoteLastUpdatedTime(updateNote, note);
		noteDao.updateAndReturnNote(note);
		return responseUtil.buildSuccessResponse(note);
	}

	private void fillNoteWithNoteStringNoteTitleNoteLastUpdatedTime(RestUpdateNote updateNote, Note note) {
		fillNoteWithNoteString(updateNote, note);
		fillNoteWithNoteTitle(updateNote, note);
		note.setNoteLastUpdatedTime(new Date());
	}

	private void fillNoteWithNoteTitle(RestUpdateNote updateNote, Note note) {
		if(null != updateNote.getNoteTitle() && updateNote.getNoteTitle().isEmpty()){
			note.setNoteTitle(updateNote.getNoteTitle());
		}
	}

	private void fillNoteWithNoteString(RestUpdateNote updateNote, Note note) {
		if(null != updateNote.getNoteString() && updateNote.getNoteString().isEmpty()){
			note.setNoteString(updateNote.getNoteString());
		}
	}
}
