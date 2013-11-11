package br.lp3tvc2.db;

import br.lp3tvc2.entity.Estoque;
import br.lp3tvc2.entity.ProdutoFilial;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Tássio Auad
 */
public class ProdutoDAO {

    private EntityManager entityManager;

    public ProdutoDAO() throws Exception {
        entityManager = ConexaoJavaDB.getConexao();
    }

    public ProdutoDAO insertProduto(Estoque estoqueItem) throws SQLException {
        entityManager.persist(estoqueItem);

        return this;
    }

    public ProdutoDAO updateProduto(Estoque estoqueItem) throws SQLException {
        Estoque estoqueItemFromDb = findBYNomeAndFilial(
                estoqueItem.getEstoquePK().getProduto(),
                estoqueItem.getEstoquePK().getFilial());

        estoqueItemFromDb.setQuantidade(estoqueItem.getQuantidade());

        return this;
    }

    public ProdutoDAO deleteProduto(Estoque estoqueItem) throws SQLException {
        Estoque estoque = findBYNomeAndFilial(
                estoqueItem.getEstoquePK().getProduto(),
                estoqueItem.getEstoquePK().getFilial());

        entityManager.remove(estoque);

        return this;
    }

    public List<Estoque> findAll() throws SQLException {
        return (List<Estoque>) entityManager.createNamedQuery("Estoque.findAll")
                .getResultList();
    }

    public List<ProdutoFilial> findProdutoETotalEmFiliais() throws SQLException {

        Query query = entityManager.createQuery("select e.estoquePK.produto, count(*) as totalfilial from Estoque e group by e.estoquePK.produto");
        List<Object[]> resultList = query.getResultList();
        List<ProdutoFilial> produtoFilialList = new ArrayList<ProdutoFilial>();
        for (Object[] obj : resultList) {
            ProdutoFilial produtoFilial = new ProdutoFilial();
            produtoFilial.setNomeProduto((String) obj[0]);
            produtoFilial.setTotalFilial(String.valueOf(obj[1]));
            produtoFilialList.add(produtoFilial);
        }

        return produtoFilialList;

        /*String sql = "";
         List<ProdutoFilial> produtoTotalFilial = new ArrayList<>();


         selectProdutoETotalEmFiliais.clearParameters();
         ResultSet resultSet = selectProdutoETotalEmFiliais.executeQuery();
         while (resultSet.next()) {
         ProdutoFilial produtoFilial = new ProdutoFilial();
         produtoFilial.setNomeProduto(resultSet.getString("produto"));
         produtoFilial.setTotalFilial(resultSet.getString("totalfilial"));

         produtoTotalFilial.add(produtoFilial);
         }
         return produtoTotalFilial;*/
    }

    public void begin() {
        entityManager.getTransaction().begin();
    }

    public void commit() {
        entityManager.getTransaction().commit();
    }

    public void rollback() {
        entityManager.getTransaction().rollback();
    }

    public void clear() {
        entityManager.clear();
    }

    public Estoque findBYNomeAndFilial(String nomeProduto, String filial) throws SQLException {
        return (Estoque) entityManager.createNamedQuery("Estoque.findByProdutoAndFilial")
                .setParameter("filial", filial).setParameter("produto", nomeProduto)
                .getSingleResult();
    }

    public List<Estoque> findByFilial(String filial) throws Exception {
        return (List<Estoque>) entityManager.createNamedQuery("Estoque.findByFilial")
                .setParameter("filial", filial).getResultList();
    }

    public List<Estoque> findByProduto(String nomeProduto) throws Exception {
        return (List<Estoque>) entityManager.createNamedQuery("Estoque.findByProduto")
                .setParameter("produto", nomeProduto).getResultList();
    }
}
