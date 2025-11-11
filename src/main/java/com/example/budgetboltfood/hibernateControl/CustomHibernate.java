package com.example.budgetboltfood.hibernateControl;

import com.example.budgetboltfood.model.Cuisine;
import com.example.budgetboltfood.model.Cart;
import com.example.budgetboltfood.model.Restaurant;
import com.example.budgetboltfood.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate{

    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByUsername(String login, String password) {
        User user = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            user = entityManager.createQuery(
                            "SELECT u FROM User u WHERE u.email = :login AND u.password = :password",
                            User.class
                    )
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();

            System.out.println("Prisijungė kaip: " + user.getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("Nepavyko prisijungti: " + e.getMessage());
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return user;
    }


    public List<Cart> getRestourantOrders(Restaurant restaurant) {
        List<Cart> orders = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cart> query = cb.createQuery(Cart.class);
            Root<Cart> root = query.from(Cart.class);

            query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
            Query q = entityManager.createQuery(query);
            orders = q.getResultList();
        } catch (Exception e){}
        return orders;
    }


}
