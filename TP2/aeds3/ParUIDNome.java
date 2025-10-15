package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class ParUIDNome implements RegistroArvoreBMais<ParUIDNome> {
    
    protected int uid;   // chave; id usuario
    protected String nome;    // valor; nome lista
    protected final short TAMANHO = 104;  // tamanho em bytes

    public ParUIDNome() {
        this.uid = -1;
        this.nome = "";
    }

    public ParUIDNome(int uid, String nome) {
        this.uid = uid;
        this.nome = nome;
    }

    public int getUId() {
        return uid;
    }

    public String getNome() {
        return nome;
    }

    public static int hash(String codigo) throws Exception {
        MessageDigest digestor = MessageDigest.getInstance("MD5");
        digestor.update(codigo.getBytes());
        ByteArrayInputStream bais = new ByteArrayInputStream(digestor.digest());
        DataInputStream dis = new DataInputStream(bais);
        return dis.readInt();
    }

    @Override
    public int hashCode() {
        return uid;
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.nome + ";" + this.uid+")";
    }

    @Override
    public int compareTo(ParUIDNome obj) { // compara dois elementos
        if ( this.uid != obj.uid ) {
            return this.uid - obj.uid;
        } else {
            return this.nome.compareTo("") == 0 ? 0 : this.nome.compareTo(obj.nome);
        }
    }

    @Override
    public ParUIDNome clone() { // clonagem de objetos
        return new ParUIDNome(this.uid,this.nome);
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.uid);
        dos.writeUTF(this.nome);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.uid = dis.readInt();
        this.nome = dis.readUTF();
    }

}
