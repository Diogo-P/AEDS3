package model;

import aeds3.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ArquivoProduto extends Arquivo<Produto> {

    private HashExtensivel<ParGtinID> indiceIndiretoGtin;
    private ArvoreBMais<ParNomeId> indiceIndiretoNome;

    public ArquivoProduto() throws Exception {
        super("produto", Produto.class.getConstructor());
        
        indiceIndiretoGtin = new HashExtensivel<>(
            ParGtinID.class.getConstructor(),
            3,
            "./dados/produto.gtin.d.db",
            "./dados/produto.gtin.c.db"
        );
        
        indiceIndiretoNome = new ArvoreBMais<>(
            ParNomeId.class.getConstructor(),
            5,
            "./dados/produto.nome.db"
        );
    }

    public int create(Produto produto) throws Exception {
        int id = super.create(produto);
        indiceIndiretoGtin.create(new ParGtinID(produto.getGtin13(), id));
        indiceIndiretoNome.create(new ParNomeId(produto.getNome(), id));
        return id;
    }

    public Produto readGtin(String gtin13) throws Exception {
        ParGtinID pgi = indiceIndiretoGtin.read(ParGtinID.hash(gtin13));
        if (pgi == null)
            return null;
        int id = pgi.getID();
        return super.read(id);
    }

    public Produto readId(int id) throws Exception {
        return super.read(id);
    }

    public Produto[] listProdutosInativados() throws Exception {
        Produto p = new Produto();
        ArrayList<Produto> produtoList = new ArrayList<>();

        int id = 1;
        while (p != null) {
            p = super.read(id);
            if ( p != null ) {
                produtoList.add(p);
            }
            id++;
        }

        Produto[] produtosDoSistema = new Produto[produtoList.size()]; 
        
        int i = 0;
        for ( Produto valor : produtoList ) {
            produtosDoSistema[i++] = valor;
        }

        Arrays.sort(
            produtosDoSistema,
            Comparator.nullsLast( // se o objeto Lista for null, manda pro final
                Comparator.comparing(
                    Produto::getNome,
                    Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER) // se o nome for null, fica no início
                )
            )
        );

        return produtosDoSistema;
    }

    public Produto[] listProdutos() throws Exception {
        Produto p = new Produto();
        ArrayList<Produto> produtoList = new ArrayList<>();

        int id = 1;
        while (p != null) {
            p = super.read(id);
            if ( p != null && p.getInativo() == false ) {
                produtoList.add(p);
            }
            id++;
        }

        Produto[] produtosDoSistema = new Produto[produtoList.size()]; 
        
        int i = 0;
        for ( Produto valor : produtoList ) {
            produtosDoSistema[i++] = valor;
        }

        Arrays.sort(
            produtosDoSistema,
            Comparator.nullsLast( // se o objeto Lista for null, manda pro final
                Comparator.comparing(
                    Produto::getNome,
                    Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER) // se o nome for null, fica no início
                )
            )
        );

        return produtosDoSistema;
    }

    public Produto[] readNome(String nome) throws Exception {
        ArrayList<ParNomeId> pnis = indiceIndiretoNome.read(new ParNomeId(nome, -1));
        Produto[] produtos = new Produto[pnis.size()];
        int i = 0;
        for (ParNomeId pni : pnis) {
            produtos[i++] = super.read(pni.getId());
        }
        return produtos;
    }

    public boolean update(Produto novoProduto) throws Exception {
        Produto produtoAntigo = super.read(novoProduto.getID());
        String gtinAntigo = produtoAntigo.getGtin13();
        String nomeAntigo = produtoAntigo.getNome();

        if (super.update(novoProduto)) {
            if (!novoProduto.getGtin13().equals(gtinAntigo)) {
                indiceIndiretoGtin.delete(ParGtinID.hash(gtinAntigo));
                indiceIndiretoGtin.create(new ParGtinID(novoProduto.getGtin13(), novoProduto.getID()));
            }
            if (!novoProduto.getNome().equals(nomeAntigo)) {
                indiceIndiretoNome.delete(new ParNomeId(nomeAntigo, novoProduto.getID()));
                indiceIndiretoNome.create(new ParNomeId(novoProduto.getNome(), novoProduto.getID()));
            }
            return true;
        } else {
            return false;
        }
    }

    /* METODO QUE NAO DEVE SER USADO MAS NÃO DELETAR O COMENTÁRIO
    @Override
    public boolean delete(int id) throws Exception {
        Produto produto = super.read(id);
        String gtin13 = produto.getGtin13();
        String nome = produto.getNome();
        if (super.delete(id)) {
            if (indiceIndiretoGtin.delete(ParGtinID.hash(gtin13))
                    && indiceIndiretoNome.delete(new ParNomeId(nome, id)))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
    */
}
