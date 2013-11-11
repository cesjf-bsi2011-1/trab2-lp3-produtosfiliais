/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.lp3tvc2.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tassioauad
 */
@Entity
@Table(name = "ESTOQUE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estoque.findAll", query = "SELECT e FROM Estoque e"),
    @NamedQuery(name = "Estoque.findByFilial", query = "SELECT e FROM Estoque e WHERE e.estoquePK.filial = :filial"),
    @NamedQuery(name = "Estoque.findAllFilial", query = "SELECT DISTINCT e.estoquePK.filial FROM Estoque e"),
    @NamedQuery(name = "Estoque.findByProduto", query = "SELECT e FROM Estoque e WHERE e.estoquePK.produto = :produto"),
    @NamedQuery(name = "Estoque.findByProdutoAndFilial", query = "SELECT e FROM Estoque e WHERE e.estoquePK.produto = :produto and e.estoquePK.filial = :filial"),
    @NamedQuery(name = "Estoque.findByQuantidade", query = "SELECT e FROM Estoque e WHERE e.quantidade = :quantidade"),
    @NamedQuery(name = "Estoque.deleteAllByFilial", query = "DELETE FROM Estoque e WHERE e.estoquePK.filial = :filial")
})
public class Estoque implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EstoquePK estoquePK;
    @Basic(optional = false)
    @Column(name = "QUANTIDADE")
    private int quantidade;

    public Estoque() {
    }

    public Estoque(EstoquePK estoquePK) {
        this.estoquePK = estoquePK;
    }

    public Estoque(EstoquePK estoquePK, int quantidade) {
        this.estoquePK = estoquePK;
        this.quantidade = quantidade;
    }

    public Estoque(String filial, String produto) {
        this.estoquePK = new EstoquePK(filial, produto);
    }

    public EstoquePK getEstoquePK() {
        return estoquePK;
    }

    public void setEstoquePK(EstoquePK estoquePK) {
        this.estoquePK = estoquePK;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estoquePK != null ? estoquePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estoque)) {
            return false;
        }
        Estoque other = (Estoque) object;
        if ((this.estoquePK == null && other.estoquePK != null) || (this.estoquePK != null && !this.estoquePK.equals(other.estoquePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.lp3tvc2.entity.Estoque[ estoquePK=" + estoquePK + " ]";
    }
    
}
