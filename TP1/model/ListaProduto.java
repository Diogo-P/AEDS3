package model;

import aeds3.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Classe que representa o relacionamento N:N entre Lista e Produto.
 * Cada registro liga uma lista a um produto, com quantidade e observações.
 */
public class ListaProduto implements Entidade {

    private int id;             // ID do relacionamento (único)
    private int idLista;        // chave estrangeira para Lista
    private int idProduto;      // chave estrangeira para Produto
    private int quantidade;     // quantidade do produto na lista
    private String observacoes; // campo opcional

    // ==========================
    // CONSTRUTORES
    // ==========================

    public ListaProduto() {
        this(-1, -1, -1, 1, "");
    }

    public ListaProduto(int idLista, int idProduto, int quantidade, String observacoes) {
        this(-1, idLista, idProduto, quantidade, observacoes);
    }

    public ListaProduto(int id, int idLista, int idProduto, int quantidade, String observacoes) {
        this.id = id;
        this.idLista = idLista;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }

    // ==========================
    // MÉTODOS DA INTERFACE Entidade
    // ==========================

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    // ==========================
    // GETTERS E SETTERS
    // ==========================

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // ==========================
    // SERIALIZAÇÃO
    // ==========================

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idLista);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.observacoes != null ? this.observacoes : "");
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idLista = dis.readInt();
        this.idProduto = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacoes = dis.readUTF();
    }

    // ==========================
    // EXIBIÇÃO
    // ==========================

    @Override
    public String toString() {
        return "\nID.............: " + this.id +
               "\nID Lista.......: " + this.idLista +
               "\nID Produto.....: " + this.idProduto +
               "\nQuantidade.....: " + this.quantidade +
               "\nObservações....: " + (this.observacoes.isEmpty() ? "(nenhuma)" : this.observacoes);
    }
}
