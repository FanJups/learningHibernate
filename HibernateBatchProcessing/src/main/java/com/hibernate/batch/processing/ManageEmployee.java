package com.hibernate.batch.processing;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ManageEmployee {
	private static SessionFactory factory;

	public static void main(String[] args) {

		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManageEmployee ME = new ManageEmployee();

		/* Add employee records in batches */
		ME.addEmployees();
	}

	/* Method to create employee records in batches */
	public void addEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;

		try {
			tx = session.beginTransaction();
			for (int i = 0; i < 100000; i++) {
				String fname = "First Name " + i;
				String lname = "Last Name " + i;
				Integer salary = i;
				Employee employee = new Employee(fname, lname, salary);
				session.save(employee);
				if (i % 50 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return;
	}
}
