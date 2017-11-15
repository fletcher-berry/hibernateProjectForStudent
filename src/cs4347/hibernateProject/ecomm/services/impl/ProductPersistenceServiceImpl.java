package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

// coded by Fletcher
public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext 
	private EntityManager em; 
	
	public ProductPersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try {
			// persist the product and commit transaction
			em.getTransaction().begin();	
			em.persist(product);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		try {
			// retrieve product by id
			em.getTransaction().begin();
			Product employee = em.find(Product.class, id);
			em.getTransaction().commit();
			return employee;
		}
		catch (Exception ex) {
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();							// start transaction
			Product prod = em.find(Product.class, product.getId());	// find the product
			prod.setProdCategory(product.getProdCategory());		// set the product's attributes
			prod.setProdDescription(product.getProdDescription());
			prod.setProdName(product.getProdName());
			prod.setProdUPC(product.getProdUPC());		
			em.getTransaction().commit();							// commit changes
		}
		catch (Exception ex) {
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		try {
			// remove product and commit transaction
			em.getTransaction().begin();
			Product prod = (Product)em.find(Product.class, id);
			em.remove(prod);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		try 
		{
			em.getTransaction().begin();
			// retrieve a product by UPC
			List<Product> products = em.createQuery("from Product as p where p.prodUPC = :upc")
					.setParameter("upc", upc).getResultList();
			em.getTransaction().commit();
			if(products.size() != 1)	// return null if UPC not found
				return null;
			return products.get(0);
				
		}
		catch (Exception ex) 
		{
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		try 
		{
			// retrieve products by category
			em.getTransaction().begin();
			List<Product> products = em.createQuery("from Product as p where p.prodCategory = :category")
					.setParameter("category", category).getResultList();
			em.getTransaction().commit();
			return products;
			
		}
		catch (Exception ex) 
		{
			// if there is a failure, rollback
			em.getTransaction().rollback();
			throw ex;
		}
		
		
	}

}
