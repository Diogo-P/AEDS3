package model;

import aeds3.ElementoLista;
import aeds3.ListaInvertida;
import java.text.Normalizer;
import java.util.*;

public class BuscadorProduto {
    private final ListaInvertida listaInvertida;
    private final ArquivoProduto arquivoProduto;
    private static final int TAMANHO_BLOCO = 100; // Tamanho dos blocos na lista invertida
    private static final Set<String> PALAVRAS_VAZIAS = new HashSet<>(Arrays.asList(
        "a", "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "até",
        "com", "como", "da", "das", "de", "dela", "delas", "dele", "deles", "depois",
        "do", "dos", "e", "ela", "elas", "ele", "eles", "em", "entre", "era",
        "eram", "essa", "essas", "esse", "esses", "esta", "estas", "este", "estes", "eu",
        "isso", "isto", "já", "lhe", "lhes", "mais", "mas", "me", "mesmo", "meu",
        "meus", "minha", "minhas", "muito", "na", "nas", "nem", "no", "nos", "nós",
        "nossa", "nossas", "nosso", "nossos", "num", "numa", "o", "os", "ou", "para",
        "pela", "pelas", "pelo", "pelos", "por", "qual", "quando", "que", "quem", "se",
        "seu", "seus", "sua", "suas", "só", "tal", "também", "te", "teu", "teus",
        "tu", "tua", "tuas", "um", "uma", "umas", "uns", "voce", "voces", "vos",
        "à", "às", "é", "são"
    ));

    public BuscadorProduto(String nomeArquivoDicionario, String nomeArquivoBlocos, ArquivoProduto arquivoProduto) throws Exception {
        this.listaInvertida = new ListaInvertida(TAMANHO_BLOCO, nomeArquivoDicionario, nomeArquivoBlocos);
        this.arquivoProduto = arquivoProduto;
    }

    
     //Retorna o número atual de documentos indexados
     
    public int getNumeroDocumentos() throws Exception {
        return listaInvertida.numeroEntidades();
    }

   
    private List<String> processarTexto(String texto) {
        // Converte para minúsculas e remove acentos
        texto = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Divide em palavras e filtra
        List<String> termos = new ArrayList<>();
        for (String palavra : texto.split("[^a-z0-9]+")) {
            if (palavra.length() > 2 && !PALAVRAS_VAZIAS.contains(palavra)) {
                termos.add(palavra);
            }
        }
        return termos;
    }

    
    // Calcula as frequencias dos termos em um texto

    private Map<String, Float> calcularFrequenciasTermos(List<String> termos) {
        // Contagem dos termos
        Map<String, Integer> contagens = new HashMap<>();
        for (String termo : termos) {
            contagens.merge(termo, 1, Integer::sum);
        }

        // Converte para frequencias
        Map<String, Float> frequencias = new HashMap<>();
        float contagemMaxima = Collections.max(contagens.values());
        for (Map.Entry<String, Integer> entrada : contagens.entrySet()) {
            frequencias.put(entrada.getKey(), entrada.getValue() / contagemMaxima);
        }
        return frequencias;
    }

   
     // Indexa um produto na estrutura de lista invertida
    public void indexarProduto(Produto produto) throws Exception {
        // Obtém texto do nome e descrição do produto
        String texto = produto.getNome() + " " + produto.getDescricao();
        List<String> termos = processarTexto(texto);
        Map<String, Float> frequencias = calcularFrequenciasTermos(termos);

        // Indexa cada termo com sua frequencia
        for (Map.Entry<String, Float> entrada : frequencias.entrySet()) {
            ElementoLista elemento = new ElementoLista(produto.getID(), entrada.getValue());
            listaInvertida.create(entrada.getKey(), elemento);
        }

        // Atualiza contagem de documentos
        listaInvertida.incrementaEntidades();
    }

    
    // Remove um produto do índice da lista invertida
    
    public void removerProduto(Produto produto) throws Exception {
        // Obtém texto do nome e descrição do produto
        String texto = produto.getNome() + " " + produto.getDescricao();
        List<String> termos = processarTexto(texto);

        // Remove cada termo para este produto
        for (String termo : termos) {
            listaInvertida.delete(termo, produto.getID());
        }

        // Atualiza contagem de documentos
        listaInvertida.decrementaEntidades();
    }

    //Calcula IDF  para um termo
    private float calcularIDF(String termo) throws Exception {
        ElementoLista[] documentos = listaInvertida.read(termo);
        int documentosComTermo = documentos.length;
        int totalDocumentos = listaInvertida.numeroEntidades();
        
        
        return (float) Math.log((totalDocumentos + 1) / (documentosComTermo + 1));
    }

    
     //Busca produtos por palavras, retornando resultados ordenados 
    public Produto[] buscarPorPalavras(String consulta) throws Exception {
        List<String> termosConsulta = processarTexto(consulta);
        Map<Integer, Float> pontuacoes = new HashMap<>();

        // Calcula  as pontuações TF-IDF
        for (String termo : termosConsulta) {
            ElementoLista[] documentos = listaInvertida.read(termo);
            float idfTermo = calcularIDF(termo);

            for (ElementoLista doc : documentos) {
                int idDoc = doc.getId();
                float tfIdfTermo = doc.getFrequencia() * idfTermo;
                
                pontuacoes.merge(idDoc, tfIdfTermo, Float::sum);
            }
        }

        // Ordena resultados por relevância
        List<Map.Entry<Integer, Float>> resultados = new ArrayList<>(pontuacoes.entrySet());
        resultados.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));
        System.out.println(resultados);

        // Converte para array de produtos
        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Map.Entry<Integer, Float> entrada : resultados) {
            Produto p = arquivoProduto.read(entrada.getKey());
            if (p != null && !p.getInativo()) {
                produtosEncontrados.add(p);
            }
        }

        return produtosEncontrados.toArray(new Produto[0]);
    }
}