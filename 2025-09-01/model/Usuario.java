package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import aeds3.Entidade;

public class Usuario implements Entidade {

    private int id;
    private String nome;
    private String email;
    private int hashSenha;
    private String perguntaSecreta;
    private String respostaSecreta;
    private boolean ativo;

    public Usuario() {
        this(-1, "", "", 0, "", "", true);
    }

    public Usuario(String nome, String email, int hashSenha, String perguntaSecreta, String respostaSecreta, boolean ativo) {
        this(-1, nome, email, hashSenha, perguntaSecreta, respostaSecreta, ativo);
    }

    public Usuario(int id, String nome, String email, int hashSenha, String perguntaSecreta, String respostaSecreta, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perguntaSecreta = perguntaSecreta;
        this.respostaSecreta = respostaSecreta;
        this.ativo = ativo;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHashSenha() {
        return hashSenha;
    }

    public void setHashSenha(int hashSenha) {
        this.hashSenha = hashSenha;
    }

    public String getPerguntaSecreta() {
        return perguntaSecreta;
    }

    public void setPerguntaSecreta(String perguntaSecreta) {
        this.perguntaSecreta = perguntaSecreta;
    }

    public String getRespostaSecreta() {
        return respostaSecreta;
    }

    public void setRespostaSecreta(String respostaSecreta) {
        this.respostaSecreta = respostaSecreta;
    }

    public boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeInt(this.hashSenha);
        dos.writeUTF(this.perguntaSecreta);
        dos.writeUTF(this.respostaSecreta);
        dos.writeBoolean(this.ativo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readInt();
        this.perguntaSecreta = dis.readUTF();
        this.respostaSecreta = dis.readUTF();
        this.ativo = dis.readBoolean();
    }

    @Override
    public String toString() {
        return "\nID.................: " + this.id
                + "\nNome...............: " + this.nome
                + "\nEmail..............: " + this.email
                + "\nHash da Senha......: " + this.hashSenha
                + "\nPergunta Secreta...: " + this.perguntaSecreta
                + "\nResposta Secreta...: " + this.respostaSecreta
                + "\nAtivo..............: " + (this.ativo ? "Sim" : "Não");
    }
}
