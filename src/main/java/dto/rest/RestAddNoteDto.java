package main.java.dto.rest;

import javax.inject.Named;

/**
 * This is Rest Data Transfer Object for Note Add Service with below attributes
 * 1. Note Title
 * 2. Note String
 * @Author shreyas patil
 */
@Named
public class RestAddNoteDto {
	public String noteTitle;
	public String noteString;
}
