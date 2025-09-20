package model;
import aeds3.*;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;
import java.util.RandomAccess;



public class ArquivoLista extends Arquivo<Lista>{
    ArvoreBMais<ParIdId> indiceIndiretoNome;
    ArvoreBMais<ParIdId> indiceIndiretoDescricao;
    public short length;
    public ArquivoLista() throws Exception{
        super("lista", Lista.class.getConstructor());

        indiceIndiretoDescricao = new ArvoreBMais<>(
            ParIdId.class.getConstructor(), 
            5, 
            "./dados/lista.descricao.db"
        );

        indiceIndiretoNome = new ArvoreBMais<>(
            ParIdId.class.getConstructor(), 
            5, 
            "./dados/lista.nome.db"
        );
    }

    public int create(Lista lista) throws Exception {
        int id = super.create(lista);
        indiceIndiretoNome.create(new ParIdId(lista.getID(),id));
        //indiceIndiretoNome.create(new ParNomeId(lista.getNome(), id));
        return id;
    }

    // public T read(int id) throws Exception {
    //     arquivo.seek(TAM_CABECALHO);
    //     ParIDEndereco pie = indiceDireto.read(id);
    //     if(pie!=null) {
    //         arquivo.seek(pie.getEndereco());
    //         byte lapide = arquivo.readByte();
    //         short tam = arquivo.readShort();
    //         if(lapide==' ') {
    //             byte[] vb = new byte[tam];
    //             arquivo.read(vb);
    //             T entidade = construtor.newInstance();
    //             entidade.fromByteArray(vb);
    //             if(entidade.getID() == id) {
    //                 return entidade;
    //             }
    //         }
    //     }
    //     return null;
    // }

    @Override
    public Lista read(int id)throws Exception{
        Lista lista = null;

        return lista;
    }



    // public boolean update(Lista novaLista) throws Exception {
    //     Lista listaAntiga = super.read(novaLista.getID());
    //     String nomeAnterior = listaAntiga.getNome();
    //     String descricaoAnterior = listaAntiga.getDescricao();

    //     if (super.update(novaLista)) {
    //         // if (!novaLista.getDescricao().equals(descricaoAnterior)) {
    //         //     indiceIndiretoEmail.delete(ParEmailID.hash(emailAnterior));
    //         //     indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getID()));
    //         // }
    //         if (!novaLista.getNome().equals(nomeAnterior)) {
    //             indiceIndiretoNome.delete(new ParIdId(nomeAnterior, novaLista.getID()));
    //             indiceIndiretoNome.create(new ParNomeId(novaLista.getNome(), novaLista.getID()));
    //         }
    //         return true;
    //     } else {
    //         return false;
    //     }
        

    //     // if (super.update(novaLista)) {
    //     //     if (!novaLista.getDescricao().equals(descricaoAnterior)) {
    //     //         indiceIndiretoDescricao.delete(ParIdDescricao.hash(descricaoAnterior));
    //     //         indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getID()));
    //     //     }
    //     //     if (!novoUsuario.getNome().equals(nomeAnterior)) {
    //     //         indiceIndiretoNome.delete(new ParNomeId(nomeAnterior, novoUsuario.getID()));
    //     //         indiceIndiretoNome.create(new ParNomeId(novoUsuario.getNome(), novoUsuario.getID()));
    //     //     }
    //     //     return true;
    //     // } else {
    //     //     return false;
    //     // }
    // }

    // @Override
    // public static void delete(int pos){
    //     try(RandomAccessFile raf = new RandomAccessFile("dados\lista.db")){
    //         int tam_registro = 117;
    //         long i=0;
    //         long regs  = raf.length()/tam_registro;
    //         while (i<regs) {
                
    //         }
    //         for(long i=0;i<regs;i++){
    //             long pos = i*tam_registro;
    //             byte lapide = arq.readByte();
    //             int 
    //         }
    //     }
    // }

}