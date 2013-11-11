package br.lp3tvc2.db;

import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Tássio Auad
 */
public class FilialDAO {

    private EntityManager entityManager;

    public FilialDAO() throws Exception {
        entityManager = ConexaoJavaDB.getConexao();
    }

    public List<String> findAll() throws SQLException {
        return entityManager.createNamedQuery("Estoque.findAllFilial").getResultList();
    }

    public FilialDAO deleteFilial(String filial) throws SQLException {
        entityManager.createNamedQuery("Estoque.deleteAllByFilial")
                .setParameter("filial", filial).executeUpdate();

        return this;
    }
    
    public void begin() {
        entityManager.getTransaction().begin();
    }
    
    public void commit(){
        entityManager.getTransaction().commit();
    }

    public void rollback() {
        entityManager.getTransaction().rollback();
    }
}
