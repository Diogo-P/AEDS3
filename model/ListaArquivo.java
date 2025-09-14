package model;

import aeds3.*;
import java.util.ArrayList;

public class ListaArquivo extends Arquivo<Lista> {

    HashExtensivel<ParCodigoID> indiceIndiretoCodigo; // para crud de listas
    HashExtensivel<ParUIDID> indiceIndiretoUId; // para auxiliar operações de 
    ArvoreBMais<ParUIDNome> indiceIndiretoNome; // para links de listas a usuarios

    public ListaArquivo() throws Exception {
        super("lista", Lista.class.getConstructor());
        indiceIndiretoCodigo = new HashExtensivel<>(
            ParCodigoID.class.getConstructor(), 
            3, 
            "./dados/listaIndiceCodigoID.d.db", 
            "./dados/listaIndiceCodigoID.c.db"
        );
        indiceIndiretoUId = new HashExtensivel<>(
            ParUIDID.class.getConstructor(),
            3,
            "./dados/listaIndiceIDUID.d.db", 
            "./dados/listaIndiceIDUID.c.db"
        );
        indiceIndiretoNome = new ArvoreBMais<>(
            ParUIDNome.class.getConstructor(),
            5,
            "./dados/listaIndiceUIDNome"
        );
    }

    public int create(Lista lista, int uid) throws Exception {
        int id = super.create(lista);
        /*
        ArrayList<ParUIDNome> buscar = indiceIndiretoNome.read(new ParUIDNome(UID,lista.getNome()));
        for ( ParUIDNome buscando : buscar ) {
            if ( buscando.getUId() == UID && buscando.getNome().compareTo(lista.getNome()) == 0 ) {
                return -1;
            }
        } */
        //lista.setNome(nomeU);
        lista.setUID(uid);
        indiceIndiretoUId.create(new ParUIDID(lista.getID(),uid));
        indiceIndiretoNome.create(new ParUIDNome(lista.UID,lista.getNome()));
        indiceIndiretoCodigo.create(new ParCodigoID(lista.codigo,id));
        return id;
    }

    public Lista read(String codigo) throws Exception { // leitura por CÓDIGO
        ParCodigoID pci = indiceIndiretoCodigo.read(ParCodigoID.hash(codigo));
        if ( pci == null ) {
            return null;
        } else {
            int id = pci.getId();
            return super.read(id);
        }
    }

  

    public boolean update(Lista lista) throws Exception {
//
        Lista listaAntiga = super.read(lista.getID());
        String nomeAntigo = listaAntiga.getNome();
        String codigoAntigo = listaAntiga.getCodigo();

        if (super.update(lista)) {
            if ( ! nomeAntigo.equals(lista.getNome()) ) {
                indiceIndiretoNome.delete(new ParUIDNome(lista.getUID(),nomeAntigo));
                indiceIndiretoNome.create(new ParUIDNome(lista.getUID(),lista.getNome()));
            }
            if ( ! codigoAntigo.equals(lista.getCodigo()) ) { // este metodo na pratica não é necessario pois codigos supostamente nunca mudam
                indiceIndiretoCodigo.delete(ParCodigoID.hash(codigoAntigo));
                indiceIndiretoCodigo.create(new ParCodigoID(lista.getCodigo(),lista.getID()));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(Lista lista) throws Exception {
        Lista listaAntiga = super.read(lista.getID());
        String codigoAntigo = listaAntiga.getCodigo();
        String nomeAntigo = listaAntiga.getNome();
        int UIDantigo = listaAntiga.getUID();
        int IDantigo = listaAntiga.getID();
        if (super.delete(lista.getID())) {
            if ( indiceIndiretoCodigo.delete(ParCodigoID.hash(codigoAntigo)) && indiceIndiretoNome.delete(new ParUIDNome(UIDantigo,nomeAntigo)) 
                && indiceIndiretoUId.delete(IDantigo)
            ) {
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Lista[] getAllLists(int UID) throws Exception {
        ArrayList<ParUIDNome> listas = indiceIndiretoNome.read(new ParUIDNome(UID,"") );
        Lista[] output = new Lista[listas.size()];
        int i = 0;
        for ( ParUIDNome pin : listas ) {
            output[i++] = super.read(indiceIndiretoUId.read(pin.getUId()).getId());
        }
        return output;
    }
}
