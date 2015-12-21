package main.java.util;

import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.transaction.UnexpectedRollbackException;

/**
 * A utility class to 
 * 1. Build Session
 * 2. Build SessionFactory
 * 3. Begin Transaction
 * 4. Rollabck Transaction
 * 5. Throw UnexpectedRollbackException
 * @Author shreyas patil
 */
@Named
public class HibernateUtil {
	private static SessionFactory sessionFactory = buildAndReturnSessionFactoryWithTryCatch();
	
	private static SessionFactory buildAndReturnSessionFactoryWithTryCatch(){
		try{
			return buildAndReturnSessionFactory();
		}catch(Throwable ex){
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	private static SessionFactory buildAndReturnSessionFactory() {
		Configuration configuration = new Configuration().configure();
		ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
		registry.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
		return configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}
	
	public static void initializeSessionAndTransaction(Session session,Transaction transaction) {
		session = getSession();
		transaction = session.beginTransaction();
	}

	public static void closeSession(Session session) {
		session.close();
	}

	public static void rollbackTransactionAndThrowException(TransactionException exception,Transaction transaction) {
		transaction.rollback();
		throw returnTransactionExceptionWithMessage(exception);
	}

	private static UnexpectedRollbackException returnTransactionExceptionWithMessage(TransactionException exception) {
		return new UnexpectedRollbackException(exception.getMessage());
	}
}
