package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUIDID implements RegistroHashExtensivel {
    
    protected int uid;   // chave; id lista
    protected int id;    // valor; codigo lista
    protected final short TAMANHO = 8;  // tamanho em bytes

    public ParUIDID() {
        this(-1,-1);
    }

    public ParUIDID(int id) {
        this(id,-1);
    }

    public ParUIDID(int uid, int id) {
        this.uid = uid;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getUID() {
        return this.uid;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.id + ";" + this.uid+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.uid);
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.uid = dis.readInt();
        this.id = dis.readInt();
    }
}
