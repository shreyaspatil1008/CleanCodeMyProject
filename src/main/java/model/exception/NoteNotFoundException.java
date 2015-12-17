package main.java.model.exception;

/**
 * This is a custom exception for if note is not found in the system
 * @Author shreyas patil
 */
public class NoteNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public NoteNotFoundException(String message){
		super(message);
	}

}
