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
import main.java.model.rest.RestUpdateNote;
import main.java.util.AuthenticationUtil;
import main.java.util.ResponseUtil;

/**
 * @Author shreyas patil
 */
@Path("/note")
public class NoteService {

	/**
	 * A web service EndPoint class with below services
	 * 1. get Notes By UserId
	 * 2. get Note By NoteId
	 * 3. Save Note
	 * 4. Update Note
	 * 5. Delete Note
	 * @Author shreyas patil
	 */
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

	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveNote(RestAddNote addNote, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdSaveAndReturnResponse(addNote, authString);
		}catch(IOException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataAccessException e){
			return responseUtil.buildFailureResponse("No notes with userId "+user.getId());
		}catch(DataPersistanceException e){
			return responseUtil.buildFailureResponse("Insert not successful");
		}catch(UserNotFoundException e){
			return responseUtil.buildFailureResponse(e.getMessage());
		}
	}
	
	private Response validateUserForAuthStringNoteIdSaveAndReturnResponse(RestAddNote addNote, String authString)
			throws IOException,DataAccessException,UserNotFoundException {
		if(!authenticationUtil.isUserAuthenticated(authString,user))
			return responseUtil.buildFailureResponse("User authentication unsuccessful.");
		else
			return createSaveAndReturnSavedNote(addNote);
	}

	private Response createSaveAndReturnSavedNote(RestAddNote addNote) {
		Note note = noteDao.saveNoteAndReturnSavedNote(createAndReturnNoteToSave(addNote));
		return responseUtil.buildSuccessResponse(note);
	}

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
		noteDao.updateNote(note);
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
			noteDao.deleteNote(note);
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
