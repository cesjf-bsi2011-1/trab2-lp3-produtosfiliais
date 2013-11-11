/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.lp3tvc2.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author tassioauad
 */
@Embeddable
public class EstoquePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "FILIAL")
    private String filial;
    @Basic(optional = false)
    @Column(name = "PRODUTO")
    private String produto;

    public EstoquePK() {
    }

    public EstoquePK(String filial, String produto) {
        this.filial = filial;
        this.produto = produto;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (filial != null ? filial.hashCode() : 0);
        hash += (produto != null ? produto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstoquePK)) {
            return false;
        }
        EstoquePK other = (EstoquePK) object;
        if ((this.filial == null && other.filial != null) || (this.filial != null && !this.filial.equals(other.filial))) {
            return false;
        }
        if ((this.produto == null && other.produto != null) || (this.produto != null && !this.produto.equals(other.produto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.lp3tvc2.entity.EstoquePK[ filial=" + filial + ", produto=" + produto + " ]";
    }
    
}
