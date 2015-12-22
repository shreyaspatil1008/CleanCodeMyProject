package main.java.dao.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import main.java.dao.impl.UserDaoImpl;
import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.model.exception.UserNotFoundException;

public class TestGetUserByEmailAndPassword {

	private UserDao userDao;
	
	@Before
	public void setUp() throws Exception {
		userDao = new UserDaoImpl();
	}

	@After
	public void tearDown() throws Exception {
		userDao = null;
	}
	
	@Test
	public void testGetUserByEmailAndPassword() {
		try {
			User user = new User();
			user.setId(1l);
			assertEquals(user, userDao.getUserByEmailAndPassword("shreyas.patil@gmail.com", "test@1234")); 
		} catch (DataAccessException e) {
			e.getMessage();
		} catch (UserNotFoundException e) {
			e.getMessage();
		}
	}
	
	@Test
	public void testGetUserByEmailAndPasswordUserNotFoundException() {
		try {
			User user = new User();
			user.setId(1l);
			assertEquals(user, userDao.getUserByEmailAndPassword("shreyas.patil@gmail.com", "test@1236")); 
		} catch (UserNotFoundException e) {
			e.getMessage();
		}
	}

}
