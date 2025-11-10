package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class ParCodigoID implements RegistroHashExtensivel {
    
    protected int id;   // chave; id lista
    protected String codigo;    // valor; codigo lista
    protected final short TAMANHO = 14;  // tamanho em bytes

    public ParCodigoID() {
        this.codigo = "";
        this.id = -1;
    }

    public ParCodigoID(String codigo, int id) {
        this.codigo = codigo;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    @Override
    public int hashCode() {
        int value = -1;
        try {
            value = hash(codigo);
        } catch (Exception e) {
            System.err.println("Hash gerado incorretamente: ");
            e.printStackTrace();
        }
        return value;
    }

    public static int hash(String codigo) throws Exception {
        MessageDigest digestor = MessageDigest.getInstance("MD5");
        digestor.update(codigo.getBytes());
        ByteArrayInputStream bais = new ByteArrayInputStream(digestor.digest());
        DataInputStream dis = new DataInputStream(bais);
        return dis.readInt();
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.codigo + ";" + this.id+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(this.codigo.getBytes());
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] bCodigo = new byte[10];
        dis.read(bCodigo);
        this.codigo = new String(bCodigo);
        this.id = dis.readInt();
    }

}
