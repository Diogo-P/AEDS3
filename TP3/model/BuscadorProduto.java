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
        "tu", "tua", "tuas", "um", "uma", "umas", "uns", "você", "vocês", "vos",
        "à", "às", "é", "são"
    ));

    public BuscadorProduto(String nomeArquivoDicionario, String nomeArquivoBlocos, ArquivoProduto arquivoProduto) throws Exception {
        this.listaInvertida = new ListaInvertida(TAMANHO_BLOCO, nomeArquivoDicionario, nomeArquivoBlocos);
        this.arquivoProduto = arquivoProduto;
    }

    // Resultado detalhado da busca 
    public static class ResultadoBusca {
        private Produto produto;
        private final Map<String, Float> tfs = new HashMap<>();
        private final Map<String, Float> idfs = new HashMap<>();
        private final Map<String, Float> tfIdfs = new HashMap<>();
        private float pontuacaoTotal = 0f;

        public ResultadoBusca(Produto produto) {
            this.produto = produto;
        }

        public Produto getProduto() { return produto; }
        public Map<String, Float> getTfs() { return tfs; }
        public Map<String, Float> getIdfs() { return idfs; }
        public Map<String, Float> getTfIdfs() { return tfIdfs; }
        public float getPontuacaoTotal() { return pontuacaoTotal; }
        private void addTermo(String termo, float tf, float idf, float tfIdf) {
            tfs.put(termo, tf);
            idfs.put(termo, idf);
            tfIdfs.put(termo, tfIdf);
            pontuacaoTotal += tfIdf;
        }
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
            // Aceita também tokens curtos  desde que não sejam stopwords
            if (palavra.length() > 0 && !PALAVRAS_VAZIAS.contains(palavra)) {
                termos.add(palavra);
            }
        }
        return termos;
    }

    
    // Calcula as frequências dos termos em um texto

    private Map<String, Float> calcularFrequenciasTermos(List<String> termos) {
        // Contagem dos termos
        Map<String, Integer> contagens = new HashMap<>();
        for (String termo : termos) {
            contagens.merge(termo, 1, Integer::sum);
        }

        // Converte para frequências usando TF = count / totalTerms 
        Map<String, Float> frequencias = new HashMap<>();
        float totalTermos = 0f;
        for (Integer v : contagens.values()) totalTermos += v;
        if (totalTermos <= 0f) return frequencias;
        for (Map.Entry<String, Integer> entrada : contagens.entrySet()) {
            frequencias.put(entrada.getKey(), entrada.getValue() / totalTermos);
        }
        return frequencias;
    }

   
    // Indexa um produto na estrutura de lista invertida usando o nome
    public void indexarProduto(Produto produto) throws Exception {
        // Obtém texto do nome do produto
        String texto = produto.getNome();
        List<String> termos = processarTexto(texto);
        Map<String, Float> frequencias = calcularFrequenciasTermos(termos);

        // Indexa cada termo com sua frequência
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
        boolean removedAny = false;
        for (String termo : termos) {
            boolean removed = listaInvertida.delete(termo, produto.getID());
            if (removed) removedAny = true;
        }

        // Atualiza contagem de documentos somente se ao menos um termo foi removido
        if (removedAny) {
            listaInvertida.decrementaEntidades();
        }
    }

    // Reconstrói o índice para refletir possíveis mudanças no cálculo de TF/IDF (Usado para Teste)
    public void reconstruirIndice() throws Exception {
        Produto[] produtos = arquivoProduto.listProdutos();
        for (Produto p : produtos) {
            try {
                removerProduto(p);
            } catch (Exception e) {
                
            }
            indexarProduto(p);
        }
    }

    // Calcula IDF com IDF = 1 + log10(N / df)
    private float calcularIDF(String termo) throws Exception {
        ElementoLista[] documentos = listaInvertida.read(termo);
        int documentosComTermo = documentos.length;
        int totalDocumentos = listaInvertida.numeroEntidades();
        if (totalDocumentos <= 0) return 1f;
        double df = documentosComTermo <= 0 ? 1.0 : (double) documentosComTermo;
        double ratio = ((double) totalDocumentos) / df;
        return 1f + (float) Math.log10(ratio);
    }

    
    // Busca produtos por palavras, retornando resultados ordenados com detalhes TF/IDF
    public ResultadoBusca[] buscarPorPalavras(String consulta) throws Exception {
        List<String> termosConsulta = processarTexto(consulta);

        // Mapa temporário: produtoId -> ResultadoBusca
        Map<Integer, ResultadoBusca> mapaResultados = new HashMap<>();

        for (String termo : termosConsulta) {
            ElementoLista[] documentos = listaInvertida.read(termo);
            float idfTermo = calcularIDF(termo);

            for (ElementoLista doc : documentos) {
                int idDoc = doc.getId();
                float tf = doc.getFrequencia();
                float tfIdfTermo = tf * idfTermo;

                ResultadoBusca res = mapaResultados.get(idDoc);
                if (res == null) {
                    Produto p = arquivoProduto.read(idDoc);
                    if (p == null || p.getInativo()) continue; // ignora produtos inexistentes ou inativos
                    res = new ResultadoBusca(p);
                    mapaResultados.put(idDoc, res);
                }
                res.addTermo(termo, tf, idfTermo, tfIdfTermo);
            }
        }

        // Ordena por pontuação 
        List<ResultadoBusca> resultados = new ArrayList<>(mapaResultados.values());
        resultados.sort((a, b) -> Float.compare(b.getPontuacaoTotal(), a.getPontuacaoTotal()));

        return resultados.toArray(new ResultadoBusca[0]);
    }
}