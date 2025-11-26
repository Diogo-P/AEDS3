package model;
import aeds3.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import nanoid.NanoId;

public class Lista implements Entidade{
    
    private int id; //4 bytes
    private String nome; //20 bytes
    private String descricao; //30 bytes
    private LocalDate criacao;//24 bytes
    private LocalDate limite;//24 bytes
    private String codigo;//10 bytes
    //Chave estrangeira
    private int idUsuario;//4 bytes
    //boolean ativo;
    private boolean ativo;// 1 byte

    public Lista(){
        this.id=-1;
        this.nome="";
        this.descricao="";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.of(1970, 1, 1);
        String tmp = data.format(formatter);
        this.criacao = data;
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
        this.criacao=criacao;
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
        return this.codigo;
    }

    public void setCodigo() {
        this.codigo = NanoId.generate(10);
    }

    public LocalDate getCriacao() {
        return criacao;
    }public void setCriacao(LocalDate criacao) {
        this.criacao = criacao;
    }

    public String getDescricao() {
        return descricao;
    }public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getUID() {
        return this.idUsuario;
    }

    public void setUID(int uid) {
        this.idUsuario = uid;
    }

    public String getNome() {
        return nome;
    }public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getLimite() {
        return limite;
    }public void setLimite(LocalDate limite) {
        this.limite = limite;
    }

   public byte[] toByteArray() throws Exception { // --> ALTERADO POR FABIANO
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);
        dos.writeInt((int)this.criacao.toEpochDay());
        dos.writeInt((int)this.limite.toEpochDay());
        dos.write(this.codigo.getBytes()); 
        dos.writeInt(this.idUsuario);
        dos.writeBoolean(this.ativo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception { // --> ALTERADO POR FABIANO
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        byte[] codigo = new byte[10];
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.criacao = LocalDate.ofEpochDay(dis.readInt());
        this.limite = LocalDate.ofEpochDay(dis.readInt());
        dis.read(codigo);
        this.codigo = new String(codigo);
        this.idUsuario = dis.readInt();
        this.ativo = dis.readBoolean();
    }

    @Override
    public String toString() {
        return "\nCÓDIGO: " + this.codigo +
                "\nNOME: " + this.nome +
                "\nDESCRIÇÃO: " + this.descricao +
                "\nDATA DE CRIAÇÃO: " + this.criacao +
                "\nDATA LIMITE: " + this.limite;
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