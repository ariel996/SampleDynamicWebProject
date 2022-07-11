package service;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import service.DbUtil;
import model.Bhuser;

public class DbUser {
	
	public static Bhuser getUser(long userID)
	{
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		Bhuser user = em.find(Bhuser.class, userID);
		return user;
	}
	
	public static void insert(Bhuser bhUser) {
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(bhUser);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
		} finally {
			em.close();
		}
	}
	
	public static String getGravatarURL(String email, Integer size) {
		StringBuilder url = new StringBuilder();
		url.append("http://www.gravatar.com/avatar/");
		url.append(email);
		url.append("?s=" + size.toString());
		return url.toString();
	}
	
	public static void update(Bhuser bhUser) {
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(bhUser);
			trans.commit();
		} catch (Exception e) {
			System.out.println(e);
			trans.rollback();
		} finally {
			em.close();
		}
	}
	
	public static void delete(Bhuser bhUser) {
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.remove(em.merge(bhUser));
			trans.commit();
		} catch (Exception e) {
			System.out.println(e);
			trans.rollback();
		} finally {
			em.close();
		}
	}
	
	public static Bhuser getUserByEmail(String email)
	{
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		String qString = "Select u from Bhuser u " + "where u.useremail=:useremail";
		TypedQuery<Bhuser> q = em.createQuery(qString, Bhuser.class);
		q.setParameter("useremail", email);
		Bhuser user = null;
		try {
			System.out.println("Getting single user");
			user = q.getSingleResult();
			System.out.println(user.getUsername());
		} catch (NoResultException e) {
			System.out.println(e);
		} finally {
			em.close();
		}
		return user;
	}
	
	public static boolean isValidUser(String userEmail, String userPassword) {
		EntityManager em = DbUtil.getEntityManager("Bullhorn");
		String qString = "Select count(b.bhuserid) from Bhuser b " + "where b.useremail = :useremail and b.userpassword = :userpass";
		TypedQuery<Long> q = em.createQuery(qString, Long.class);
		boolean result = false;
		q.setParameter("useremail", userEmail);
		q.setParameter("userpass", userPassword);
		try {
			long userId = q.getSingleResult();
			if(userId > 0)
			{
				result = true;
			}
		} catch (Exception e) {
			result = false;
		} finally {
			em.close();
		}
		return result;
	}
}
