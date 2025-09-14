import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import nanoid.NanoId;

class Lista {
    private int id;
    private String nome;
    private String descricao;
    private LocalDate criacao;
    private LocalDate limite;
    private String codigo;
    
    public Lista ( ) {
        this(-1,"","",LocalDate.of(1970, 1, 1),LocalDate.of(1970, 1, 1),"");
    }

    // P apos variavel indica parametro
    public Lista ( int idP, String nomeP, String descricaoP, LocalDate criacaoP, LocalDate limiteP, String codigoP ) {
        id = idP;
        nome = nomeP;
        descricao = descricaoP;
        criacao = criacaoP;
        limite = limiteP;
        codigo = codigoP;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCodigo() {
        this.codigo = NanoId.generate(10);
    }

    public String ListaToString() {
        return "\nCÓDIGO: " + codigo +
                "\nNOME: " + nome +
                "\nDESCRIÇÃO: " + descricao +
                "\nDATA DE CRIAÇÃO: " + criacao +
                "\nDATA LIMITE: " + limite;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
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
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
        this.criacao = LocalDate.ofEpochDay(dis.readInt());
        this.limite = LocalDate.ofEpochDay(dis.readInt());
        dis.read(codigo);
        this.codigo = new String(codigo);
    }
}
