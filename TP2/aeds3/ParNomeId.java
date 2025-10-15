package aeds3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParNomeId implements RegistroArvoreBMais<ParNomeId> {

    private String nome;
    private int id;
    private final short TAMANHO = 104; // 100 para nome + 4 para ID

    public ParNomeId() {
        this("", -1);
    }

    public ParNomeId(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    @Override
    public ParNomeId clone() {
        return new ParNomeId(this.nome, this.id);
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public int compareTo(ParNomeId outro) {
        int comp = this.nome.compareToIgnoreCase(outro.nome);
        if (comp != 0) return comp;
        return this.id == -1 ? 0 : this.id - outro.id;
    }

    @Override
    public String toString() {
        return "(" + this.nome.trim() + ";" + this.id + ")";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve nome com tamanho fixo (100 bytes)
        StringBuilder sb = new StringBuilder(this.nome);
        while (sb.length() < 100) sb.append(' ');
        dos.write(sb.toString().getBytes());

        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] vb_nome = new byte[100];
        dis.read(vb_nome);
        this.nome = new String(vb_nome).trim();

        this.id = dis.readInt();
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
