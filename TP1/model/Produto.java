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

    // Construtores
    public Produto() {
        this(-1, "", "", "");
    }

    public Produto(String gtin13, String nome, String descricao) {
        this(-1, gtin13, nome, descricao);
    }

    public Produto(int id, String gtin13, String nome, String descricao) {
        this.id = id;
        this.gtin13 = gtin13;
        this.nome = nome;
        this.descricao = descricao;
    }

    // Implementação da interface Entidade
    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    // Getters e Setters
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

    // Serialização
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.gtin13);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.gtin13 = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
    }

    // Exibição textual
    @Override
    public String toString() {
        return "\nID..............: " + this.id
             + "\nGTIN-13.........: " + this.gtin13
             + "\nNome............: " + this.nome
             + "\nDescrição.......: " + this.descricao;
    }
}
