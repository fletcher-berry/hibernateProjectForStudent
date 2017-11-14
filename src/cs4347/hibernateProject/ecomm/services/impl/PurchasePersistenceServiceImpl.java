package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService{
	@PersistenceContext 
	private EntityManager em; 
	
	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException{
		//attempt to create a purchase object
		try {
			//beginning of the transaction
			em.getTransaction().begin();
			//persist purchase
			em.persist(purchase);
			//finalize transaction
			em.getTransaction().commit();
		}
		//rollback if creation fails to prevent corruption
		catch (Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException{
		try{
			//start a new transaction
			em.getTransaction().begin();
			//retrieve the purchase object by given id
			Purchase purchase = em.find(Purchase.class, id);
			//commit transaction
			em.getTransaction().commit();
			//return purchase object to the user
			return purchase;
		//if transaction goes wrong, rollback to prevent corruption
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException{
		try{
			//start new transaction
			em.getTransaction().begin();
			//find old purchase object by id
			Purchase old=(Purchase)em.find(Purchase.class, purchase.getId());
			//go over each attribute in new object and replace the corresponding attribute
			//in the old object (not including id)
			old.setProduct(purchase.getProduct()); 
			old.setPurchaseAmount(purchase.getPurchaseAmount());
			old.setPurchaseDate(purchase.getPurchaseDate());
			//Persist new state
			em.getTransaction().commit();
		//Rollback in event of failure to prevent DB corruption
		}catch (Exception e){
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException{
		try{
			//start new transaction
			em.getTransaction().begin();
			//find purchase to be deleted
			Purchase del=(Purchase)em.find(Purchase.class, id);
			//delete purchase
			em.remove(del);
			//commite operation
			em.getTransaction().commit();
		//rollback if error occurs	
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException{
		try{
			//start new transaction
			em.getTransaction().begin();
			List<Purchase> purchases=em.createQuery(
					"from Purchase as p where p.customer.id = :cID")
					.setParameter("cID",customerID).getResultList();
			em.getTransaction().commit();
			return purchases;
		//rollback if error occurs
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException{
		return null;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException{
		try{
			//start new transaction
			em.getTransaction().begin();
			List<Purchase> purchases=em.createQuery(
					"from Purchase as p where p.product.id = :pID")
					.setParameter("pID",productID).getResultList();
			em.getTransaction().commit();
			return purchases;
		//rollback if error occurs
		}catch(Exception e){
			em.getTransaction().rollback();
			throw e;
		}
	}
}
