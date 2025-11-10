package model;

import aeds3.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Produto implements Entidade {

    private int id;
    private String gtin13;
    private String nome;
    private String descricao;
    private boolean inativo;

    // Construtores
    public Produto() {
        this(-1, "", "", "", false);
    }

    public Produto(String gtin13, String nome, String descricao, boolean inativo) {
        this(-1, gtin13, nome, descricao, inativo);
    }

    public Produto(int id, String gtin13, String nome, String descricao, boolean inativo) {
        this.id = id;
        this.gtin13 = gtin13;
        this.nome = nome;
        this.descricao = descricao;
        this.inativo = inativo;
    }

    // Implementacao da interface Entidade
    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    // Getters e Setters
    public boolean getInativo() {
        return this.inativo;
    }

    public void setInativo(boolean inativo) {
        this.inativo = inativo;
    }

    public String getGtin13() {
        return gtin13;
    }

    public void setGtin13(String gtin13) {
        this.gtin13 = gtin13;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Serializacao
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.gtin13);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeBoolean(this.inativo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.gtin13 = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.inativo = dis.readBoolean();
    }

    // Exibicao textual
    @Override
    public String toString() {
        String status = this.inativo ? "Nao" : "Sim";
        return "\nID..............: " + this.id
             + "\nATIVADO: " + status
             + "\nGTIN-13.........: " + this.gtin13
             + "\nNome............: " + this.nome
             + "\nDescricao.......: " + this.descricao;
    }
}
