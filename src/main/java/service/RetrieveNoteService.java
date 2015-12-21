package main.java.service;

import java.io.IOException;

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
import main.java.model.exception.NoteNotFoundException;
import main.java.model.exception.UserNotFoundException;
import main.java.util.AuthenticationUtil;
import main.java.util.ResponseUtil;

/**
 * A web service EndPoint class with below services
 * 1. get Notes By UserId
 * 2. get Note By NoteId
 * @Author shreyas patil
 */
@Path("/note")
public class RetrieveNoteService {

	@Inject
	private User user;
	@Inject
	private NoteDao noteDao;
	@Inject
	private ResponseUtil responseUtil;
	@Inject
	private AuthenticationUtil authenticationUtil;

	@GET
	@Path("/getNotes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNotesByUserId(@QueryParam("userId")Long userId, @HeaderParam("authorization") String authString){
		try{
			return validateAndReturnResponse(userId, authString);
		}catch(IOException e){
			return responseUtil.buildFailureResponse("No notes with userId "+userId);
		}catch(DataAccessException e){
			return responseUtil.buildFailureResponse("No notes with userId "+userId);
		}catch(UserNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}
	}

	private Response validateAndReturnResponse(Long userId, String authString) 
			throws IOException,DataAccessException,UserNotFoundException{
		if(null == userId || 0l == userId)
			return responseUtil.buildFailureResponse("Note Id can not be empty.");
		else
			return authenticateUserAndReturnResponse(userId, authString);
	}

	private Response authenticateUserAndReturnResponse(Long userId, String authString) 
			throws IOException,DataAccessException,UserNotFoundException{
		if(!authenticationUtil.isUserAuthenticated(authString,user) && !user.getId().equals(userId))
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return responseUtil.buildSuccessResponse(user.getNotes());
	}

	@GET
	@Path("/getNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNoteByNoteId(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		
		try{
			return validateUserNoteAndReturnResponse(noteId, authString);
		}catch(IOException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataAccessException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(UserNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}catch(NoteNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}
	}
	
	private Response validateUserNoteAndReturnResponse(Long noteId, String authString) 
			throws IOException,DataAccessException,UserNotFoundException,NoteNotFoundException{
		if(null == noteId || 0l == noteId)
			return responseUtil.buildFailureResponse("Note Id can not be empty.");
		else if(!authenticationUtil.isUserAuthenticated(authString,user))
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return validateAndReturnNotes(noteId);
		
	}

	private Response validateAndReturnNotes(Long noteId) throws NoteNotFoundException{
		return validateNoteAndReturnResponse(findAndReturnNoteByNoteId(noteId));
	}

	private Note findAndReturnNoteByNoteId(Long noteId)throws NoteNotFoundException {
		for(Note curNote:user.getNotes()){
			if(curNote.getId().equals(noteId)){
				return curNote;
			}
		}
		throw new NoteNotFoundException("Note not found for note id"+noteId);
	}
	
	private Response validateNoteAndReturnResponse(Note note) {
		if(note == null)
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return responseUtil.buildSuccessResponse(note);
	}
}
