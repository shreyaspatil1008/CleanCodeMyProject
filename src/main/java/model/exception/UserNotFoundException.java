package main.java.model.exception;

import javax.inject.Named;

/**
 * This is a custom exception for if user is not found in the system
 * @Author shreyas patil
 */
@Named
public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(String message){
		super(message);
	}

}
