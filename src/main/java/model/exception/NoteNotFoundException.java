package main.java.model.exception;

import javax.inject.Named;

/**
 * This is a custom exception for if note is not found in the system
 * @Author shreyas patil
 */
@Named
public class NoteNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NoteNotFoundException(String message){
		super(message);
	}

}
