package model;

import aeds3.*;
import java.util.ArrayList;

public class ArquivoListaProduto extends Arquivo<ListaProduto> {

    // Índices para as relações N:N
    private ArvoreBMais<ParIdId> indicePorLista;   // (idLista → idListaProduto)
    private ArvoreBMais<ParIdId> indicePorProduto; // (idProduto → idListaProduto)

    public ArquivoListaProduto() throws Exception {
        super("listaproduto", ListaProduto.class.getConstructor());

        indicePorLista = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/listaproduto.lista.db"
        );

        indicePorProduto = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            5,
            "./dados/listaproduto.produto.db"
        );
    }

    // ================================
    // CREATE
    // ================================
    @Override
    public int create(ListaProduto lp) throws Exception {
        int id = super.create(lp);

        indicePorLista.create(new ParIdId(lp.getIdLista(), id));
        indicePorProduto.create(new ParIdId(lp.getIdProduto(), id));

        return id;
    }

    // ================================
    // READ por Lista
    // ================================
    public ListaProduto[] readPorLista(int idLista) throws Exception {
        ArrayList<ParIdId> pares = indicePorLista.read(new ParIdId(idLista, -1));
        ListaProduto[] relacoes = new ListaProduto[pares.size()];
        int i = 0;
        for (ParIdId par : pares)
            relacoes[i++] = super.read(par.getId2());
        return relacoes;
    }

    // ================================
    // READ por Produto
    // ================================
    public ListaProduto[] readPorProduto(int idProduto) throws Exception {
        ArrayList<ParIdId> pares = indicePorProduto.read(new ParIdId(idProduto, -1));
        ListaProduto[] relacoes = new ListaProduto[pares.size()];
        int i = 0;
        for (ParIdId par : pares)
            relacoes[i++] = super.read(par.getId2());
        return relacoes;
    }

    // ================================
    // UPDATE
    // ================================
    @Override
    public boolean update(ListaProduto novoLP) throws Exception {
        ListaProduto antigo = super.read(novoLP.getID());
        if (antigo == null) return false;

        if (super.update(novoLP)) {

            // Se mudou o idLista, atualiza o índice
            if (antigo.getIdLista() != novoLP.getIdLista()) {
                indicePorLista.delete(new ParIdId(antigo.getIdLista(), novoLP.getID()));
                indicePorLista.create(new ParIdId(novoLP.getIdLista(), novoLP.getID()));
            }

            // Se mudou o idProduto, atualiza o índice
            if (antigo.getIdProduto() != novoLP.getIdProduto()) {
                indicePorProduto.delete(new ParIdId(antigo.getIdProduto(), novoLP.getID()));
                indicePorProduto.create(new ParIdId(novoLP.getIdProduto(), novoLP.getID()));
            }

            return true;
        }
        return false;
    }

    // ================================
    // DELETE
    // ================================
    @Override
    public boolean delete(int id) throws Exception {
        ListaProduto lp = super.read(id);
        if (lp == null) return false;

        if (super.delete(id)) {
            indicePorLista.delete(new ParIdId(lp.getIdLista(), id));
            indicePorProduto.delete(new ParIdId(lp.getIdProduto(), id));
            return true;
        }
        return false;
    }
}
