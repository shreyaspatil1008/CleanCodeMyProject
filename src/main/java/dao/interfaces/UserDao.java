package main.java.dao.interfaces;

import main.java.model.User;

public interface UserDao {
	
	/**
	 * @Author shreyas patil
	 */
	User getUserByEmailAndPassword(String email, String password);
}
