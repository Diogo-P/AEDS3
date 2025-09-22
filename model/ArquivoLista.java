package model;

import aeds3.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ArquivoLista extends Arquivo<Lista> {

    HashExtensivel<ParCodigoID> indiceIndiretoCodigo; // para buscar listas a partir de CODIGO COMPARTILHAVEL
    //ArvoreBMais<ParIdId> indiceIndiretoID;
    //ArvoreBMais<ParNomeId> indiceIndiretoNome;
    ArvoreBMais<ParIdId> indiceIndiretoIdId; // para links de NOME DE LISTAS a usuarios 
    public short length;

    public ArquivoLista() throws Exception {
        super("lista", Lista.class.getConstructor());
        // indiceIndiretoDescricao = new ArvoreBMais<>(
        //     ParIdId.class.getConstructor(), 
        //     5, 
        //     "./dados/lista.descricao.db"
        // );
        indiceIndiretoCodigo = new HashExtensivel<>(
                ParCodigoID.class.getConstructor(),
                3,
                "./dados/lista.codigoID.d.db",
                "./dados/lista.codigoID.c.db"
        );
        indiceIndiretoIdId = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                5,
                "./dados/lista.id.db"
        );

        //indiceIndiretoID = new ArvoreBMais<>(
        //    ParIdId.class.getConstructor(), 
        //    5, 
        //    "./dados/lista.nome.db"
        //);
    }

   @Override
public boolean update(Lista novaLista) throws Exception {
    // Lê a versão antiga da lista antes de atualizar
    Lista listaAntiga = super.read(novaLista.getID());

    String codigoAnterior = listaAntiga.getCodigo();
    String descricaoAnterior = listaAntiga.getDescricao();
    String nomeAnterior = listaAntiga.getNome();
    LocalDate limiteAnterior = listaAntiga.getLimite();

    // Atualiza no arquivo principal
    if (super.update(novaLista)) {
        // Atualiza índice de código, se mudou
        if (!novaLista.getCodigo().equals(codigoAnterior)) {
            indiceIndiretoCodigo.delete(ParCodigoID.hash(codigoAnterior));
            indiceIndiretoCodigo.create(new ParCodigoID(novaLista.getCodigo(), novaLista.getID()));
        }

        // Se você também tiver índice para nome ou descrição,
        // pode atualizar da mesma forma:
        /*
        if (!novaLista.getNome().equals(nomeAnterior)) {
            indiceIndiretoNome.delete(new ParNomeId(nomeAnterior, novaLista.getID()));
            indiceIndiretoNome.create(new ParNomeId(novaLista.getNome(), novaLista.getID()));
        }

        if (!novaLista.getDescricao().equals(descricaoAnterior)) {
            indiceIndiretoDescricao.delete(new ParDescricaoId(descricaoAnterior, novaLista.getID()));
            indiceIndiretoDescricao.create(new ParDescricaoId(novaLista.getDescricao(), novaLista.getID()));
        }
        */

        return true;
    } else {
        return false;
    }
}

    public boolean deleteIndices(Lista lista) throws Exception {
        String codigoAntigo = lista.getCodigo();
        String nomeAntigo = lista.getNome();
        int UIDantigo = lista.getUID();
        int IDantigo = lista.getUID();
        if ( indiceIndiretoCodigo.delete(ParCodigoID.hash(codigoAntigo)) && indiceIndiretoIdId.delete(new ParIdId(UIDantigo,IDantigo)) ) {
            return true;
        }
        else {
            return false;
        }
    }


    public int create(Lista lista, int uid) throws Exception {
        int id = super.create(lista);
        indiceIndiretoCodigo.create(new ParCodigoID(lista.getCodigo(), id));
        indiceIndiretoIdId.create(new ParIdId(lista.getUID(), lista.getID()));
        //indiceIndiretoID.create(new ParIdId(lista.getID(),id));  possivelmente sem logica; parea dois ids identicos de lista
        //indiceIndiretoNome.create(new ParIdId(lista.getNome(), id));
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
    // public Lista[] carregaListas(int id){
    // }

    /*
    public Lista[] carregaListas(int uid) throws Exception {
        ArrayList<ParIdId> pnis = indiceIndiretoIdId.read(new ParIdId(uid, -1));
        Usuario[] usuarios = new Usuario[pnis.size()];
        int i = 0;
        for (ParIdId pni : pnis) {
            usuarios[i++] = super.read(pni.getId2());
        }
        return null;
    }*/
    public Lista[] carregaListas(int idUsuario) throws Exception {
        ParIdId chaveDeBusca = new ParIdId(idUsuario, -1);
        ArrayList<ParIdId> paresDeIds = indiceIndiretoIdId.read(chaveDeBusca);
        Lista[] listasDoUsuario = new Lista[paresDeIds.size()];
        int i = 0;
        for (ParIdId par : paresDeIds) {
            int idDaLista = par.getId2(); 
            listasDoUsuario[i] = super.read(idDaLista);
            i++;
        }

        return listasDoUsuario;
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
    /*public boolean delete(int id) {
    try (RandomAccessFile raf = new RandomAccessFile("dados/lista.db", "rw")) {
        int tam_registro = 117;
        long regs = raf.length() / tam_registro;

        for (long i = 0; i < regs; i++) {
            long pos = i * tam_registro;
            raf.seek(pos);
            
            byte lapide = raf.readByte(); 
            System.out.println(lapide);
            int currentId = raf.readInt(); 
            System.out.println(currentId);

            if (lapide == ' ' ) { 
                
                raf.seek(pos);
                raf.writeByte('*'); 
                return true; 
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false; 
}
     */
}
