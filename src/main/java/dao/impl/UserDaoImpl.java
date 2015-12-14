package main.java.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import main.java.dao.interfaces.UserDao;
import main.java.model.User;
import main.java.util.HibernateUtil;

public class UserDaoImpl implements UserDao{
	
	/**
	 * @Author shreyas patil
	 */
	private Session session;
	
	//public methods section
	public User getUserByEmailAndPassword(String email, String password)throws DataAccessException{
		session = getSession();
		return getUserByEmailAndPasswordWithTryCatchFinally(email, password);
	}

	//private methods section

	private User getUserByEmailAndPasswordWithTryCatchFinally(String email, String password) {
		try{
			return returnValidUserOrNull(email, password);
		}catch(DataAccessException exception){
            throw returnDataAccessExceptionWithMessage(exception);
        }finally{
			session.close();
		}
	}
	
	private DataAccessResourceFailureException returnDataAccessExceptionWithMessage(DataAccessException exception) {
		return new DataAccessResourceFailureException(exception.getMessage());
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
	
	private Session getSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}

	private Query getQuery(String email, String password) {
		Query query = session.createQuery("from user where email = ? and password = ?");
		query.setParameter(0, email);
		query.setParameter(1, password);
		return query;
	}

}
