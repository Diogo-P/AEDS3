package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParGtinID implements RegistroHashExtensivel {

    private String gtin13;  // chave (GTIN-13)
    private int id;         // valor
    private final short TAMANHO = 32; // tamanho fixo do registro em bytes

    public ParGtinID() {
        this("", -1);
    }

    public ParGtinID(String gtin13, int id) {
        this.gtin13 = gtin13;
        this.id = id;
    }

    public String getGtin13() {
        return gtin13;
    }

    public int getID() {
        return id;
    }

    public static int hash(String gtin13) {
        return Math.abs(gtin13.hashCode());
    }

    @Override
    public int hashCode() {
        return hash(this.gtin13);
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public String toString() {
        return "(" + this.gtin13.trim() + ";" + this.id + ")";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // GTIN-13 sempre tem 13 caracteres, mas usamos campo fixo de 20 bytes
        StringBuilder sb = new StringBuilder(this.gtin13);
        while (sb.length() < 20) sb.append(' ');
        dos.write(sb.toString().getBytes());

        // Escreve o ID
        dos.writeInt(this.id);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb_gtin = new byte[20];
        dis.read(vb_gtin);
        this.gtin13 = new String(vb_gtin).trim();

        this.id = dis.readInt();
    }
}
