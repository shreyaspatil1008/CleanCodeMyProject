package main.java.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

	/**
	 * @Author shreyas patil
	 */
	private static SessionFactory sessionFactory = buildAndReturnSessionFactoryWithTryCatch();
	
	//public methods section
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtil.sessionFactory = sessionFactory;
	}
	
	private static SessionFactory buildAndReturnSessionFactoryWithTryCatch(){
		try{
			return buildAndReturnSessionFactory();
		}catch(Throwable ex){
			throw new ExceptionInInitializerError(ex);
		}
	}

	//private methods section
	private static SessionFactory buildAndReturnSessionFactory() {
		Configuration configuration = new Configuration().configure();
		ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
		registry.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
		return configuration.buildSessionFactory(serviceRegistry);
	}
}
