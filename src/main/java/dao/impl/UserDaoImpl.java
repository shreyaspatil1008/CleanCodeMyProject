package main.java.dao.impl;

import java.util.List;

import javax.inject.Named;

import org.hibernate.Query;
import org.hibernate.Session;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.model.exception.UserNotFoundException;
import main.java.util.HibernateUtil;

/**
 * A UserDao implementation class with below operation
 * 1. get User By Email And Password
 * @Author shreyas patil
 */
@Named
public class UserDaoImpl implements UserDao{
	private Session session;
	
	public User getUserByEmailAndPassword(String email, String password)throws UserNotFoundException{
		session = HibernateUtil.getSession();
		return getUserByEmailAndPasswordWithTryCatchFinally(email, password);
	}

	private User getUserByEmailAndPasswordWithTryCatchFinally(String email, String password)throws UserNotFoundException {
		try{
			return returnValidUserOrThrowException(email, password);
		}catch(UserNotFoundException exception){
            throw exception;
        }finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	private User returnValidUserOrThrowException(String email, String password) throws UserNotFoundException{
		List<User> users = (List<User>)getQuery(email, password).list();
		if(!users.isEmpty())
			return users.get(0);
		else
			throw new UserNotFoundException("User not found.");
	}

	private Query getQuery(String email, String password) {
		Query query = session.createQuery("from user where email = ? and password = ?");
		query.setParameter(0, email);
		query.setParameter(1, password);
		return query;
	}

}
