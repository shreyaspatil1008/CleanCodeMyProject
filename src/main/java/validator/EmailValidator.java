package main.java.validator;

import javax.inject.Inject;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;

public class EmailValidator {

	/**
	 * @Author shreyas patil
	 */
	@Inject
	private UserDao userDao;
	
	public User findValidateAndReturnByEmailAndPasswordUser(String email, String password){
		return userDao.getUserByEmailAndPassword(email,password);
	}
}
