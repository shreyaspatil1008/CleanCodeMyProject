package main.java.validator;

import javax.inject.Inject;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;

/**
 * A validator class to validate provided email and password
 * @Author shreyas patil
 */
public class EmailValidator {
	@Inject
	private UserDao userDao;
	
	public User findValidateAndReturnByEmailAndPasswordUser(String email, String password){
		return userDao.getUserByEmailAndPassword(email,password);
	}
}
