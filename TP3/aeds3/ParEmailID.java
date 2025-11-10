package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParEmailID implements RegistroHashExtensivel {

    private String email;  // chave
    private int id;        // valor
    private final short TAMANHO = 128; 

    public ParEmailID() {
        this("", -1);
    }

    public ParEmailID(String email, int id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getID() {
        return id;
    }

    public static int hash(String email) {
        if (email == null) return 0;
        String norm = email.trim().toLowerCase();
        return Math.abs(norm.hashCode());
    }

    @Override
    public int hashCode() {
        return hash(this.email);
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public String toString() {
        return "(" + this.email.trim() + ";" + this.id + ")";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve email com tamanho fixo (100 bytes)
        StringBuilder sb = new StringBuilder(this.email);
        while (sb.length() < 100) sb.append(' ');
        dos.write(sb.toString().getBytes());

        // Escreve o ID
        dos.writeInt(this.id);

        // Preenche até o tamanho fixo TAMANHO
        int written = 100 + 4; // email bytes + int
        int padding = this.TAMANHO - written;
        if (padding > 0) {
            byte[] pad = new byte[padding];
            dos.write(pad);
        }

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb_email = new byte[100];
        dis.read(vb_email);
        this.email = new String(vb_email).trim();

        this.id = dis.readInt();
    }
}
