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

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.model.User;
import main.java.model.rest.RestAddNote;
import main.java.model.rest.RestUpdateNote;
import main.java.validator.EmailValidator;
import sun.misc.BASE64Decoder;


@Path("/note")
public class NoteService {

	/**
	 * @Author shreyas patil
	 */
	@Inject
	private User user;
	@Inject
	private EmailValidator emailValidator;
	@Inject
	private NoteDao noteDao;
	
	//public methods section
	@GET
	@Path("/getNotes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNotesByUserId(@QueryParam("userId")Long userId, @HeaderParam("authorization") String authString){
		try{
			return validateAndReturnResponse(userId, authString);
		}catch(Exception e){
			return buildFailureResponse("No notes with userId "+userId);
		}
	}


	@GET
	@Path("/getNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNoteByNoteId(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		
		try{
			return validateUserNoteAndReturnResponse(noteId, authString);
		}catch(Exception e){
			return buildFailureResponse("No notes with userId "+noteId);
		}
	}


	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveNote(RestAddNote addNote, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdSaveAndReturnResponse(addNote, authString);
		}catch(Exception e){
			return buildFailureResponse("Insert not successful.");
		}
	}


	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNote(RestUpdateNote updateNote, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdUpdateAndReturnResponse(updateNote, authString);
		}catch(Exception e){
			return buildFailureResponse("Update not successful.");
		}
	}


	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteNote(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		try{
			return validateUserForAuthStringNoteIdDeleteAndReturnResponse(noteId, authString);
		}catch(Exception e){
			return buildFailureResponse("Delete not successful.");
		}
	}


	//private methods section
	private Response validateAndReturnResponse(Long userId, String authString) {
		if(null == userId || 0l == userId){
			return buildFailureResponse("Note Id can not be empty.");
		}
		return authenticateUserAndReturnResponse(userId, authString);
	}


	private Response authenticateUserAndReturnResponse(Long userId, String authString) {
		if(!isUserAuthenticated(authString) && !user.getId().equals(userId)){
			return buildFailureResponse("User authentication unsuccessful.");
		}else{
			return buildSuccessResponse(user.getNotes());
		}
	}
	
	
	private Response validateUserNoteAndReturnResponse(Long noteId, String authString) {
		if(null == noteId || 0l == noteId){
			return buildFailureResponse("Note Id can not be empty.");
		}
		if(!isUserAuthenticated(authString)){
			return buildFailureResponse("User authentication unsuccessful.");
		}
		return validateAndReturnNotes(noteId);
	}


	private Response validateAndReturnNotes(Long noteId) {
		return validateNoteAndReturnResponse(findAndReturnNoteByNoteId(noteId));
	}


	private Response validateNoteAndReturnResponse(Note note) {
		if(note == null){
			return buildFailureResponse("User authentication unsuccessful.");
		}else{
			return buildSuccessResponse(note);
		}
	}


	private Note findAndReturnNoteByNoteId(Long noteId) {
		for(Note curNote:user.getNotes()){
			if(curNote.getId().equals(noteId)){
				return curNote;
			}
		}
		return null;
	}
	
	private Response validateUserForAuthStringNoteIdSaveAndReturnResponse(RestAddNote addNote, String authString) {
		if(!isUserAuthenticated(authString)){
			return buildFailureResponse("User authentication unsuccessful.");
		}
		return createSaveAndReturnSavedNote(addNote);
	}


	private Response createSaveAndReturnSavedNote(RestAddNote addNote) {
		Note note = noteDao.saveNoteAndReturnSavedNote(createAndReturnNoteToSave(addNote));
		return buildSuccessResponse(note);
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
	
	private Response validateUserForAuthStringNoteIdUpdateAndReturnResponse(RestUpdateNote updateNote,
			String authString) {
		if(null == updateNote.getId() || 0l == updateNote.getId()){
			return buildFailureResponse("Note Id can not be empty.");
		}
		
		if(!isUserAuthenticated(authString)){
			return buildFailureResponse("User authentication unsuccessful.");
		}
		return validateNoteUpdateAndReturnResponse(updateNote);
	}


	private Response validateNoteUpdateAndReturnResponse(RestUpdateNote updateNote) {
		Note note = findAndReturnNote(updateNote);
		return validateNoteUpdateAndReturnResponse(updateNote, note);
	}


	private Response validateNoteUpdateAndReturnResponse(RestUpdateNote updateNote, Note note) {
		if(note == null){
			return buildFailureResponse("User authentication unsuccessful.");
		}else{
			return fillAndUpdateNoteAndReturnResponse(updateNote, note);
		}
	}


	private Response fillAndUpdateNoteAndReturnResponse(RestUpdateNote updateNote, Note note) {
		fillNoteWithNoteStringNoteTitleNoteLastUpdatedTime(updateNote, note);
		noteDao.updateNote(note);
		return buildSuccessResponse(note);
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


	private Note findAndReturnNote(RestUpdateNote updateNote) {
		for(Note curNote:user.getNotes()){
			if(curNote.getId().equals(updateNote.getId())){
				return curNote;
			}
		}
		return null;
	}


	private Response buildFailureResponse(String message) {
		return Response.status(401).entity(message).build();
	}


	private Response buildSuccessResponse(Object object) {
		return Response.ok().entity(object).build();
	}
	
	private Response validateUserForAuthStringNoteIdDeleteAndReturnResponse(Long noteId, String authString) {
		if(!isUserAuthenticated(authString)){
			return buildFailureResponse("User authentication unsuccessful.");
		}
		
		if(null == noteId|| 0l == noteId){
			return buildFailureResponse("Note Id can not be empty.");
		}
		return validateAndDeleteNoteAndReturnResponse(noteId);
	}


	private Response validateAndDeleteNoteAndReturnResponse(Long noteId) {
		Note note = findAndReturnNoteByNoteId(noteId);
		return validateDeleteAndReturnResponseForNote(note);
	}


	private Response validateDeleteAndReturnResponseForNote(Note note) {
		if(note == null){
			return buildFailureResponse("User authentication unsuccessful.");
		}else{
			noteDao.deleteNote(note);
			return buildSuccessResponse(note);
		}
	}
	
	private boolean isUserAuthenticated(String authString){
		return fillAndValidateAuthParts(authString);
	}


	private boolean fillAndValidateAuthParts(String authString) {
		String[] authParts = authString.split("\\s+");
		return validateAuthParts(authParts);
	}


	private boolean validateAuthParts(String[] authParts) {
		if(authParts.length>1){
			return fillAndValidateAuthInfoWithTryCatch(authParts);
		}else{
			return false;	
		}
	}


	private boolean fillAndValidateAuthInfoWithTryCatch(String[] authParts) {
		String authInfo = authParts[1];
		try{
			return fillAndValidateDecodeAuth(authInfo);
		}catch(Exception e){
			return false;
		}
	}


	@SuppressWarnings("restriction")
	private boolean fillAndValidateDecodeAuth(String authInfo) throws IOException {
		String decodeAuth = new String(new BASE64Decoder().decodeBuffer(authInfo));
		return validateDecodeAuth(decodeAuth);
	}


	private boolean validateDecodeAuth(String decodeAuth) {
		if(decodeAuth.contains(":")){
			return fillAndValidateAuthPart(decodeAuth);
		}else{
			return false;
		}
	}


	private boolean fillAndValidateAuthPart(String decodeAuth) {
		String[] authPart;
		authPart = decodeAuth.split(":");
		return validateAuthPart(authPart);
	}


	private boolean validateAuthPart(String[] authPart) {
		if(authPart.length>1){
			return fillAndValidateUser(authPart);					
		}else{
			return false;
		}
	}


	private boolean fillAndValidateUser(String[] authPart) {
		user = emailValidator.findValidateAndReturnByEmailAndPasswordUser(authPart[0],authPart[1]);
		return validateUser();
	}


	private boolean validateUser() {
		if(null!=user){
			return true;
		}else{
			return false;
		}
	}

}
