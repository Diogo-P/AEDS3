package aeds3;

import java.io.*;

public class ParSenhaCodigo implements RegistroHashExtensivel {

    private String senha;    // chave (a senha original)
    private int codigo;      // valor (inteiro gerado a partir da senha)
    private final short TAMANHO = 40; // tamanho aproximado

    public ParSenhaCodigo() {
        this.senha = "";
        this.codigo = -1;
    }

    public ParSenhaCodigo(String senha) {
        this.senha = senha;
        this.codigo = senha.hashCode(); // gera o inteiro baseado na String
    }

    public String getSenha() {
        return senha;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public int hashCode() {
        return this.codigo;
    }

    public short size() {
        return this.TAMANHO;
    }

    @Override
    public String toString() {
        return "(" + this.senha + "; " + this.codigo + ")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.senha);
        dos.writeInt(this.codigo);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.senha = dis.readUTF();
        this.codigo = dis.readInt();
    }
}
