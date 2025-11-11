package com.example.budgetboltfood.hibernateControl;

import com.example.budgetboltfood.model.Cuisine;
import com.example.budgetboltfood.model.User;
import com.example.budgetboltfood.utils.FxUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class GenericHibernate {
    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    private GenericHibernate genericHibernate;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void create (T entity) {
        try
        {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity); //INSERT
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error creating user");
            alert.setContentText("There was an error creating user:\n" + e.getMessage());
        }
        finally { if (entityManager != null) entityManager.close(); }
    }
    public <T> void update (T entity) {
        try
        {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity); //UPDATE
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error creating user");
            alert.setContentText("There was an error creating user:\n" + e.getMessage());
        }
        finally { if (entityManager != null) entityManager.close(); }
    }
    public <T> void delete (T entity) {
        try
        {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(entity); //DELETE
            entityManager.getTransaction().commit();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error creating user");
            alert.setContentText("There was an error creating user:\n" + e.getMessage());
        }
        finally { if (entityManager != null) entityManager.close(); }
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();
        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error!", "DB ERORR", "Something went wrong on the insert");
        }
        return list;
    }

}
