package main.java.validator;

import javax.inject.Inject;
import javax.inject.Named;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.model.exception.UserNotFoundException;

/**
 * A validator class to validate provided email and password
 * @Author shreyas patil
 */
@Named
public class EmailValidator {
	@Inject
	private UserDao userDao;
	
	public User findValidateAndReturnByEmailAndPasswordUser(String email, String password)throws UserNotFoundException{
		return userDao.getUserByEmailAndPassword(email,password);
	}
}
