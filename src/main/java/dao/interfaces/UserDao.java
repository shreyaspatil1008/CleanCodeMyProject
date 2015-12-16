package main.java.dao.interfaces;

import main.java.model.User;

/**
 * A UserDao interface class with below operation
 * 1. get User By Email And Password
 * @Author shreyas patil
 */
public interface UserDao {
	
	User getUserByEmailAndPassword(String email, String password);
}
