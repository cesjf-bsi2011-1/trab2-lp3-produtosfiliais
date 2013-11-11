package br.lp3tvc2.app;

import br.lp3tvc2.db.ProdutoDAO;
import br.lp3tvc2.entity.Estoque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author tassio
 */
public class DistribuirEstoque extends javax.swing.JFrame {

    /**
     * Creates new form DistribuirEstoque
     */
    public DistribuirEstoque() {
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

        textFieldFilial = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        textFieldProduto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        buttonCancelar = new javax.swing.JButton();
        buttonDistribuir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Distribuir da filial:");

        jLabel2.setText("O produto:");

        buttonCancelar.setText("Cancelar");
        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        buttonDistribuir.setText("Distribuir");
        buttonDistribuir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDistribuirActionPerformed(evt);
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
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldFilial))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(textFieldProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(buttonDistribuir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonCancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textFieldFilial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textFieldProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonCancelar)
                    .addComponent(buttonDistribuir))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelarActionPerformed

    private void buttonDistribuirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDistribuirActionPerformed
        String nomeFilial = textFieldFilial.getText();
        String nomeProduto = textFieldProduto.getText();

        //Verificando se os campos da interface estão vazios.
        if (nomeFilial == null || nomeProduto == null) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos", "Campos obrigatórios", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            new ProdutoDAO().begin();
            
            //Verificando se o produto existe na filial
            Estoque produto = new ProdutoDAO().findBYNomeAndFilial(nomeProduto, nomeFilial);
            if (produto == null) {
                JOptionPane.showMessageDialog(null, "Produto " + nomeProduto
                        + " da filial " + nomeFilial + " não foi encontrado.", "OPS!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Capturando a existência do tal produto a ser transferido em outras as filiais.
            List<Estoque> listaProdutoEmOutrasFiliais = new ProdutoDAO().findByProduto(nomeProduto);
            //Removendo da lista o produto que será transferido. Isso para que ele não receba transferência dele mesmo.
            for (int i = 0; i < listaProdutoEmOutrasFiliais.size(); i++) {
                if (listaProdutoEmOutrasFiliais.get(i).getEstoquePK().getFilial().equals(produto.getEstoquePK().getFilial())) {
                    listaProdutoEmOutrasFiliais.remove(i);
                }
            }
            
            //NÃO TENTE ENTENDER! 3:00 a.m e eu não sabia mais o que estava fazendo....
            //Imagino que isso aí distribua o produto nas outras filiais.
            int index = 0;
            for (int i = 0; i < produto.getQuantidade(); i++) {
                if(index >= listaProdutoEmOutrasFiliais.size()) {
                    index = 0;
                }
                
                Estoque produtoOutraFilial = listaProdutoEmOutrasFiliais.get(index);
                produtoOutraFilial.setQuantidade(produtoOutraFilial.getQuantidade() + 1);
                index += 1;
            }
            
            //Zerando a quantidade do produto
            produto.setQuantidade(0);
            new ProdutoDAO().updateProduto(produto);
            
            for (Estoque produtoParaAtualizar : listaProdutoEmOutrasFiliais) {
                new ProdutoDAO().updateProduto(produtoParaAtualizar);
            }
            
            new ProdutoDAO().commit();
            
            //Mensagem de sucesso e limpando os campos
            JOptionPane.showMessageDialog(null, "Produtos Distribuidos com Sucesso!", "SUCESSO", JOptionPane.INFORMATION_MESSAGE);
            textFieldFilial.setText("");
            textFieldProduto.setText("");
        } catch (Exception ex) {
            try {
                new ProdutoDAO().rollback();
            } catch (Exception ex1) {
                Logger.getLogger(DistribuirEstoque.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ListaEstoque.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao distribuir o produto entre as demais filiais", "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonDistribuirActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JButton buttonDistribuir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField textFieldFilial;
    private javax.swing.JTextField textFieldProduto;
    // End of variables declaration//GEN-END:variables
}
