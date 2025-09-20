package model;
<<<<<<< HEAD
=======

import aeds3.Entidade;
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
<<<<<<< HEAD
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import aeds3.Entidade;

public class Lista implements Entidade{
    //boolean ativo;
    private int id; //4 bytes
    private String nome; //20 bytes
    private String descricao; //30 bytes
    private LocalDate criacao;//24 bytes
    private LocalDate limite;//24 bytes
    private String codigo;//10 bytes
//Chave estrangeira
    private int idUsuario;//4 bytes
    private boolean ativo;// 1 byte

    public Lista(){
        this.id=-1;
        this.nome="";
        this.descricao="";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.of(1970, 1, 1);
        String tmp = data.format(formatter);
        this.limite= LocalDate.parse(tmp,formatter);
        this.codigo="";
        this.idUsuario=-1;
        this.ativo=false;
    }

    public Lista(int id, String nome, String descricao,
    LocalDate criacao, LocalDate limite, String codigo, int idUsuario, boolean ativo){
        this.id=id;
        this.nome=nome;
        this.descricao=descricao;
        this.limite=limite;
        this.codigo=codigo;
        this.idUsuario=idUsuario;
        this.ativo=ativo;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getCriacao() {
        return criacao;
    }public void setCriacao(LocalDate criacao) {
        this.criacao = criacao;
=======
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
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
    }

    public String getDescricao() {
        return descricao;
<<<<<<< HEAD
    }public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    

    public String getNome() {
        return nome;
    }public void setNome(String nome) {
        this.nome = nome;
=======
    }

    public void setCriacao(LocalDate criacao) {
        this.criacao = criacao;
    }

    public LocalDate getCriacao() {
        return criacao;
    }

    public void setLimite(LocalDate limite) {
        this.limite = limite;
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
    }

    public LocalDate getLimite() {
        return limite;
<<<<<<< HEAD
    }public void setLimite(LocalDate limite) {
        this.limite = limite;
    }

   public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(this.ativo);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int)this.criacao.toEpochDay());
        dos.writeInt((int)this.limite.toEpochDay());
        dos.writeUTF(this.codigo);
        dos.writeBoolean(this.ativo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
=======
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
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.criacao = LocalDate.ofEpochDay(dis.readInt());
        this.limite = LocalDate.ofEpochDay(dis.readInt());
<<<<<<< HEAD
        this.codigo = dis.readUTF();
        this.ativo = dis.readBoolean();
    }

    


    // @Override
    // public byte[] toByteArray() throws Exception {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'toByteArray'");
    // }

    // @Override
    // public void fromByteArray(byte[] vb) throws Exception {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'fromByteArray'");
    // }

    
}
=======
        dis.read(codigo);
        this.codigo = new String(codigo);
    }
}
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
