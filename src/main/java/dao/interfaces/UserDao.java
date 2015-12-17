package main.java.dao.interfaces;

import org.springframework.dao.DataAccessException;

import main.java.model.User;
import main.java.model.exception.UserNotFoundException;

/**
 * A UserDao interface class with below operation
 * 1. get User By Email And Password
 * @Author shreyas patil
 */
public interface UserDao {
	
	User getUserByEmailAndPassword(String email, String password)throws DataAccessException,UserNotFoundException;
}
