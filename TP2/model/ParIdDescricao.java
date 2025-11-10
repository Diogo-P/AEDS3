package model;

import java.nio.charset.StandardCharsets;

public class ParIdDescricao {
    private String descricao;
    private int id;
    private short TAMANHO=34;

    public ParIdDescricao()throws Exception{
        this("",-1);
    }

    public ParIdDescricao(String t, int i) throws Exception {

        if(!t.isEmpty()) {

// Converte o titulo para um vetor de bytes
            byte[] vb = t.getBytes(StandardCharsets.UTF_8);

// Garante que o vetor de bytes tenha no maximo TAMANHO_TITULO bytes
            if(vb.length > TAMANHO) {

// Cria um vetor do tamanho maximo
                byte[] vb2 = new byte[TAMANHO];
                System.arraycopy(vb, 0, vb2, 0, vb2.length);

        // Verifica se os ultimos bytes estão fora do intervalo de 0 a 127 (o que indicaria que o ultimo caractere é um caractere acentuado)
                int n = TAMANHO-1;
                while(n>0 && (vb2[n]<0 || vb2[n]>127))n--;

        // Cria um novo array de bytes removendo o ultimo byte
                byte[] vb3 = new byte[n+1];
                System.arraycopy(vb2, 0, vb3, 0, vb3.length);
                vb2 = vb3;

        // Cria uma nova string para o titulo a partir desse vetor de no maximo TAMANHO_TITULO bytes
                t = new String(vb2);
            }
        }
        this.descricao = t; // ID do Usuario
        this.id = i; // ID da Pergunta
    }
}
