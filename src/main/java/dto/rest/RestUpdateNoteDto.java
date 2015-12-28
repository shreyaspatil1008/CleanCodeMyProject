package main.java.dto.rest;

import javax.inject.Named;

/**
 * This is Rest Data Transfer Object for Note Update Service with below attributes
 * 1. Id
 * 2. Note Title
 * 3. Note String
 * @Author shreyas patil
 */
@Named
public class RestUpdateNoteDto {
	public Long id;
	public String noteTitle;
	public String noteString;
}
