package model;

import aeds3.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import nanoid.NanoId;


public class Lista implements Entidade {
    protected int ID;
    protected String nome;
    protected String descricao;
    protected LocalDate criacao;
    protected LocalDate limite;
    protected String codigo;
    protected int UID;
    
    public Lista ( ) {
        this(-1,"","",LocalDate.of(1970, 1, 1),LocalDate.of(1970, 1, 1),"",-1);
    }

    public Lista ( String nomeP ) {
        this(-1,nomeP,"",LocalDate.of(1970, 1, 1),LocalDate.of(1970, 1, 1),"",-1);
    }

    // P apos variavel indica parametro
    public Lista ( int idP, String nomeP, String descricaoP, LocalDate criacaoP, LocalDate limiteP, String codigoP, int UIDP ) {
        ID = idP;
        nome = nomeP;
        descricao = descricaoP;
        criacao = criacaoP;
        limite = limiteP;
        codigo = codigoP;
        UID = UIDP;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return ID;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCriacao(LocalDate criacao) {
        this.criacao = criacao;
    }

    public LocalDate getCriacao() {
        return criacao;
    }

    public void setLimite(LocalDate limite) {
        this.limite = limite;
    }

    public LocalDate getLimite() {
        return limite;
    }

    public void setCodigo() {
        this.codigo = NanoId.generate(10);
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getUID() {
        return UID;
    }

    @Override
    public String toString() {
        return "\nCÓDIGO: " + this.codigo +
                "\nNOME: " + this.nome +
                "\nDESCRIÇÃO: " + this.descricao +
                "\nDATA DE CRIAÇÃO: " + this.criacao +
                "\nDATA LIMITE: " + this.limite;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int) this.criacao.toEpochDay());
        dos.writeInt((int) this.limite.toEpochDay());
        dos.write(this.codigo.getBytes());
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        byte[] codigo = new byte[10];
        this.ID = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.criacao = LocalDate.ofEpochDay(dis.readInt());
        this.limite = LocalDate.ofEpochDay(dis.readInt());
        dis.read(codigo);
        this.codigo = new String(codigo);
    }
}
