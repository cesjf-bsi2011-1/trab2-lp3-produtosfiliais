package br.lp3tvc2.db;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexaoJavaDB {

    static EntityManager entityManager = null;

    public static EntityManager getConexao() throws ClassNotFoundException, SQLException {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tbr2PU");
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }

    public static void fechaConexao() throws SQLException {
        if (entityManager != null) {
            entityManager.close();
        }
    }
}
