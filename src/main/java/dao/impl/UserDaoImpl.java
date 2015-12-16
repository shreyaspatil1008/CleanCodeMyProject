package main.java.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.util.HibernateUtil;

/**
 * A UserDao implementation class with below operation
 * 1. get User By Email And Password
 * @Author shreyas patil
 */
public class UserDaoImpl implements UserDao{
	private Session session;
	
	public User getUserByEmailAndPassword(String email, String password)throws DataAccessException{
		session = HibernateUtil.getSession();
		return getUserByEmailAndPasswordWithTryCatchFinally(email, password);
	}

	private User getUserByEmailAndPasswordWithTryCatchFinally(String email, String password) {
		try{
			return returnValidUserOrNull(email, password);
		}catch(DataAccessException exception){
            throw returnDataAccessExceptionWithMessage(exception);
        }finally{
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private User returnValidUserOrNull(String email, String password) {
		List<User> users = (List<User>)getQuery(email, password).list();
		if(!users.isEmpty()){
			return users.get(0);
		}else{
			return null;
		}
	}

	private Query getQuery(String email, String password) {
		Query query = session.createQuery("from user where email = ? and password = ?");
		query.setParameter(0, email);
		query.setParameter(1, password);
		return query;
	}
	
	private DataAccessResourceFailureException returnDataAccessExceptionWithMessage(DataAccessException exception) {
		return new DataAccessResourceFailureException(exception.getMessage());
	}

}
