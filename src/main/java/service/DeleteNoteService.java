package main.java.service;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.dao.DataAccessException;

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.model.User;
import main.java.model.exception.DataPersistanceException;
import main.java.model.exception.NoteNotFoundException;
import main.java.model.exception.UserNotFoundException;
import main.java.model.rest.RestAddNote;
import main.java.util.AuthenticationUtil;
import main.java.util.ResponseUtil;

/**
 * A web service EndPoint class with below service
 * 1. Delete Note
 * @Author shreyas patil
 */
@Path("/note")
public class DeleteNoteService {

	@Inject
	private User user;
	@Inject
	private NoteDao noteDao;
	@Inject
	private ResponseUtil responseUtil;
	@Inject
	private AuthenticationUtil authenticationUtil;

	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNote(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdDeleteAndReturnResponse(noteId, authString);
		}catch(IOException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataAccessException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataPersistanceException e){
			return responseUtil.buildFailureResponse("Delete not successful");
		}catch(UserNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}catch(NoteNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}
	}
	
	private Response validateUserForAuthStringNoteIdDeleteAndReturnResponse(Long noteId, String authString) 
			throws IOException,DataAccessException,UserNotFoundException,NoteNotFoundException{
		if(!authenticationUtil.isUserAuthenticated(authString,user))
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else if(null == noteId|| 0l == noteId)
			return responseUtil.buildFailureResponse("Note Id can not be empty.");
		else
			return validateAndDeleteNoteAndReturnResponse(noteId);
	}

	private Response validateAndDeleteNoteAndReturnResponse(Long noteId)throws NoteNotFoundException {
		Note note = findAndReturnNoteByNoteId(noteId);
		return validateDeleteAndReturnResponseForNote(note);
	}

	private Note findAndReturnNoteByNoteId(Long noteId)throws NoteNotFoundException {
		for(Note curNote:user.getNotes()){
			if(curNote.getId().equals(noteId)){
				return curNote;
			}
		}
		throw new NoteNotFoundException("Note not found for note id"+noteId);
	}

	private Response validateDeleteAndReturnResponseForNote(Note note) {
		if(note == null)
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else{
			noteDao.deleteAndReturnNote(note);
			return responseUtil.buildSuccessResponse(note);
		}
	}

	private Response validateNoteAndReturnResponse(Note note) {
		if(note == null)
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return responseUtil.buildSuccessResponse(note);
	}

	private Note createAndReturnNoteToSave(RestAddNote addNote) {
		Note note = new Note();
		fillNoteWithNoteStringNoteTitleNoteCreatedDate(addNote, note);
		return note;
	}

	private void fillNoteWithNoteStringNoteTitleNoteCreatedDate(RestAddNote addNote, Note note) {
		validateAndSetNoteString(addNote, note);
		validateAndSetNoteTitle(addNote, note);
		note.setNoteCreatedTime(new Date());
	}

	private void validateAndSetNoteTitle(RestAddNote addNote, Note note) {
		if(null != addNote.getNoteTitle() && addNote.getNoteTitle().isEmpty()){
			note.setNoteTitle(addNote.getNoteTitle());
		}
	}

	private void validateAndSetNoteString(RestAddNote addNote, Note note) {
		if(null != addNote.getNoteString() && addNote.getNoteString().isEmpty()){
			note.setNoteString(addNote.getNoteString());
		}
	}
}
