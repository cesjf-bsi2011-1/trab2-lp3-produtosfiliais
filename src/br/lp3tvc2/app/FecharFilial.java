/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.lp3tvc2.app;

import br.lp3tvc2.db.FilialDAO;
import br.lp3tvc2.db.ProdutoDAO;
import br.lp3tvc2.entity.Estoque;
import br.lp3tvc2.entity.EstoquePK;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author curso
 */
public class FecharFilial extends javax.swing.JFrame {

    /**
     * Creates new form FecharFilial
     */
    public FecharFilial() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelFecharAFilial = new javax.swing.JLabel();
        textFieldFecharAFilial = new javax.swing.JTextField();
        labelTransferirParaFilial = new javax.swing.JLabel();
        textFieldTransferirParaFilial = new javax.swing.JTextField();
        buttonFecharFilial = new javax.swing.JButton();
        buttonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Fechar Filial");

        labelFecharAFilial.setText("Fechar a filial:");

        labelTransferirParaFilial.setText("Transferir produtos para a filial:");

        buttonFecharFilial.setText("Fechar Filial");
        buttonFecharFilial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFecharFilialActionPerformed(evt);
            }
        });

        buttonCancelar.setText("Cancelar");
        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFecharAFilial)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldFecharAFilial))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTransferirParaFilial)
                            .addComponent(buttonFecharFilial))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textFieldTransferirParaFilial)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 111, Short.MAX_VALUE)
                                .addComponent(buttonCancelar)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFecharAFilial)
                    .addComponent(textFieldFecharAFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTransferirParaFilial)
                    .addComponent(textFieldTransferirParaFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonFecharFilial)
                    .addComponent(buttonCancelar))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelarActionPerformed

    private void buttonFecharFilialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFecharFilialActionPerformed
        String fecharFilial = textFieldFecharAFilial.getText();
        String transferirParaFilial = textFieldTransferirParaFilial.getText();

        if (fecharFilial == null || transferirParaFilial == null) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos", "Campos obrigatórios", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Estoque> produtosDaFilialAFechar = new ProdutoDAO().findByFilial(fecharFilial);
            List<Estoque> produtosDaFilialAReceber = new ProdutoDAO().findByFilial(transferirParaFilial);
            List<Estoque> produtosParaInserir = new ArrayList<>();
            for (Estoque produtoFilialAFechar : produtosDaFilialAFechar) {
                boolean existeNaOutraFilial = false;
                for (Estoque produtoFilialAReceber : produtosDaFilialAReceber) {
                    if (produtoFilialAReceber.getEstoquePK().getProduto().equals(produtoFilialAFechar.getEstoquePK().getProduto())) {
                        existeNaOutraFilial = true;
                        produtoFilialAReceber.setQuantidade(produtoFilialAReceber.getQuantidade()
                                + produtoFilialAFechar.getQuantidade());
                        produtosParaInserir.add(produtoFilialAReceber);
                    }
                }

                if (!existeNaOutraFilial) {
                    produtoFilialAFechar.getEstoquePK().setFilial(transferirParaFilial);
                    produtosParaInserir.add(produtoFilialAFechar);
                }
            }

            new ProdutoDAO().begin();
            for (Estoque produto : produtosParaInserir) 
            {
                new ProdutoDAO().updateProduto(produto);
            }
            new ProdutoDAO().commit();
            
            new FilialDAO().begin();
            new FilialDAO().deleteFilial(fecharFilial);
            new FilialDAO().commit();
            
            JOptionPane.showMessageDialog(null, "Filial " + fecharFilial + " fechada com sucesso.","SUCESSO", JOptionPane.INFORMATION_MESSAGE);
            textFieldFecharAFilial.setText("");
            textFieldTransferirParaFilial.setText("");
        } catch (Exception ex) {
            try {
                new ProdutoDAO().rollback();
                new FilialDAO().rollback();
            } catch (Exception ex1) {
                Logger.getLogger(ListaEstoque.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(TransporteProduto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro ao tranferir", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonFecharFilialActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonFecharFilial;
    private javax.swing.JLabel labelFecharAFilial;
    private javax.swing.JLabel labelTransferirParaFilial;
    private javax.swing.JTextField textFieldFecharAFilial;
    private javax.swing.JTextField textFieldTransferirParaFilial;
    // End of variables declaration//GEN-END:variables
}