package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.CustomerPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class CustomerPersistenceServiceImpl implements CustomerPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public CustomerPersistenceServiceImpl(EntityManager em) 
	{
		this.em = em;
	}
	
	@Override
	public void create(Customer customer) throws SQLException, DAOException
	{
		try 
		{
			//start transaction
			em.getTransaction().begin();
			
			em.persist(customer);

			//end transaction
			em.getTransaction().commit();
		}
		catch (Exception ex) 
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Customer retrieve(Long id) 
	{
		try 
		{
			//start transaction
			em.getTransaction().begin();
			
			Customer customer = em.find(Customer.class, id);

			//end transaction
			em.getTransaction().commit();
			
			return customer;
		}
		catch (Exception ex) 
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Customer c1) throws SQLException, DAOException
	{
		try 
		{
			//start transaction
			em.getTransaction().begin();
			
			Customer c2 = em.find(Customer.class, c1.getId());
			
			c2.setFirstName(c1.getFirstName());
			c2.setLastName(c1.getLastName());
			c2.setGender(c1.getGender());
			c2.setDob(c1.getDob());
			c2.setEmail(c1.getEmail());
			c2.setAddress(c1.getAddress());
			c2.setCreditCard(c1.getCreditCard());

			//end transaction
			em.getTransaction().commit();
		}
		catch (Exception ex) 
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try 
		{
			//start transaction
			em.getTransaction().begin();
			
			Customer cust = em.find(Customer.class, id);
			
			em.remove(cust);

			//end transaction
			em.getTransaction().commit();
		}
		catch (Exception ex) 
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException
	{
		try
		{
			//start transaction
			em.getTransaction().begin();
			// build query and set parameters
			List<Customer> customers = em.createQuery("from Customer as c where c.address.zipcode = :cZIP")
					.setParameter("cZIP", zipCode)
					.getResultList();
			//end transaction
			em.getTransaction().commit();
			return customers;
		}
		catch(Exception e)
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException
	{
		try
		{
			//start transaction
			em.getTransaction().begin();
			// build query and set parameters
			List<Customer> customers = em.createQuery("from Customer as c where c.dob between :cSD and :cED")
					.setParameter("cSD", startDate)
					.setParameter("cED", endDate)
					.getResultList();
			//end transaction
			em.getTransaction().commit();
			return customers;
		}
		catch(Exception e)
		{
			//rollback transaction if failure occurs
			em.getTransaction().rollback();
			throw e;
		}
	}
}
