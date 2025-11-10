/*********
 * ARVORE B+ 
 * 
 * Os nomes dos metodos foram mantidos em ingles
 * apenas para manter a coerencia com o resto da
 * disciplina:
 * - boolean create(RegistroArvoreBMais objeto)   
 * - int[] read(RegistroArvoreBMais objeto)
 * - boolean delete(RegistroArvoreBMais objeto)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v2.0 - 2021
 */
package aeds3;

import java.io.*;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

// Esta versao da arvore funciona apenas como um conjunto de par de chaves.
// A primeira chave pode repetir na arvore, mas nao o par de chaves, 
// isto e, quando a primeira chave de dois elementos for igual, a segunda chave,
// deve ser necessariamente diferente.

public class ArvoreBMais<T extends RegistroArvoreBMais<T>> {

    private int ordem; // Numero maximo de filhos que uma pagina pode conter
    private int maxElementos; // Variavel igual a ordem - 1 para facilitar a clareza do codigo
    private int maxFilhos; // Variavel igual a ordem para facilitar a clareza do codigo
    private RandomAccessFile arquivo; // Arquivo em que a arvore sera armazenada
    private String nomeArquivo;
    private Constructor<T> construtor;

    // Variaveis usadas nas funções recursivas (ja que nao e possível passar valores
    // por referencia)
    private T elemAux;
    private long paginaAux;
    private boolean cresceu;
    private boolean diminuiu;

    // Esta classe representa uma pagina da arvore (folha ou nao folha).
    private class Pagina {

        protected int ordem; // Numero maximo de filhos que uma pagina pode ter
        protected Constructor<T> construtor;
        protected int maxElementos; // Variavel igual a ordem - 1 para facilitar a clareza do codigo
        protected int maxFilhos; // Variavel igual a ordem para facilitar a clareza do codigo
        protected int TAMANHO_ELEMENTO; // Os elementos sao de tamanho fixo
        protected int TAMANHO_PAGINA; // A pagina sera de tamanho fixo, calculado a partir da ordem

        protected ArrayList<T> elementos; // Elementos da pagina
        protected ArrayList<Long> filhos; // Vetor de ponteiros para os filhos
        protected long proxima; // Proxima folha, quando a pagina for uma folha

        // Construtor da pagina
        public Pagina(Constructor<T> ct, int o) throws Exception {

            // Inicializaçao dos atributos
            this.construtor = ct;
            this.ordem = o;
            this.maxFilhos = this.ordem;
            this.maxElementos = this.ordem - 1;
            this.elementos = new ArrayList<>(this.maxElementos);
            this.filhos = new ArrayList<>(this.maxFilhos);
            this.proxima = -1;

            // Calculo do tamanho (fixo) da pagina
            // cada elemento -> depende do objeto
            // cada ponteiro de filho -> 8 bytes
            // ultimo filho -> 8 bytes
            // ponteiro proximo -> 8 bytes
            this.TAMANHO_ELEMENTO = this.construtor.newInstance().size();
            this.TAMANHO_PAGINA = 4 + this.maxElementos * this.TAMANHO_ELEMENTO + this.maxFilhos * 8 + 8;
        }

        // Retorna o vetor de bytes que representa a pagina para armazenamento em
        // arquivo
        protected byte[] toByteArray() throws IOException {

            // Um fluxo de bytes e usado para construçao do vetor de bytes
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(ba);

            // Quantidade de elementos presentes na pagina
            out.writeInt(this.elementos.size());

            // Escreve todos os elementos
            int i = 0;
            while (i < this.elementos.size()) {
                out.writeLong(this.filhos.get(i).longValue());
                out.write(this.elementos.get(i).toByteArray());
                i++;
            }
            if (this.filhos.size() > 0)
                out.writeLong(this.filhos.get(i).longValue());
            else
                out.writeLong(-1L);

            // Completa o restante da pagina com registros vazios
            byte[] registroVazio = new byte[TAMANHO_ELEMENTO];
            while (i < this.maxElementos) {
                out.write(registroVazio);
                out.writeLong(-1L);
                i++;
            }

            // Escreve o ponteiro para a proxima pagina
            out.writeLong(this.proxima);

            // Retorna o vetor de bytes que representa a pagina
            return ba.toByteArray();
        }

        // Reconstroi uma pagina a partir de um vetor de bytes lido no arquivo
        public void fromByteArray(byte[] buffer) throws Exception {

            // Usa um fluxo de bytes para leitura dos atributos
            ByteArrayInputStream ba = new ByteArrayInputStream(buffer);
            DataInputStream in = new DataInputStream(ba);

            // Le a quantidade de elementos da pagina
            int n = in.readInt();

            // Le todos os elementos (reais ou vazios)
            int i = 0;
            this.elementos = new ArrayList<>(this.maxElementos);
            this.filhos = new ArrayList<>(this.maxFilhos);
            T elem;
            while (i < n) {
                this.filhos.add(in.readLong());
                byte[] registro = new byte[TAMANHO_ELEMENTO];
                in.read(registro);
                elem = this.construtor.newInstance();
                elem.fromByteArray(registro);
                this.elementos.add(elem);
                i++;
            }
            this.filhos.add(in.readLong());
            in.skipBytes((this.maxElementos - i) * (TAMANHO_ELEMENTO + 8));
            this.proxima = in.readLong();
        }
    }

    // ------------------------------------------------------------------------------

    public ArvoreBMais(Constructor<T> c, int o, String na) throws Exception {

        // Inicializa os atributos da arvore
        construtor = c;
        ordem = o;
        maxElementos = o - 1;
        maxFilhos = o;
        nomeArquivo = na;

        // Abre (ou cria) o arquivo, escrevendo uma raiz empty, se necessario.
        arquivo = new RandomAccessFile(nomeArquivo, "rw");
        if (arquivo.length() < 16) {
            arquivo.writeLong(-1); // raiz empty
            arquivo.writeLong(-1); // pointeiro lista excluídos
        }
    }

    // Testa se a arvore esta empty. Uma arvore empty e identificada pela raiz == -1
    public boolean empty() throws IOException {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        return raiz == -1;
    }

    // Busca recursiva por um elemento a partir da chave. Este metodo invoca
    // o metodo recursivo read1, passando a raiz como referencia.
    // O metodo retorna a lista de elementos que possuem a chave (considerando
    // a possibilidade chaves repetidas)
    public ArrayList<T> read(T elem) throws Exception {

        // Recupera a raiz da arvore
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();

        // Executa a busca recursiva
        if (raiz != -1)
            return read1(elem, raiz);
        else {
            ArrayList<T> resposta = new ArrayList<>();
            return resposta;
        }
    }

    // Busca recursiva. Este metodo recebe a referencia de uma pagina e busca
    // pela chave na mesma. A busca continua pelos filhos, se houverem.
    private ArrayList<T> read1(T elem, long pagina) throws Exception {

        // Como a busca e recursiva, a descida para um filho inexistente
        // (filho de uma pagina folha) retorna um vetor vazio.
        if (pagina == -1) {
            ArrayList<T> resposta = new ArrayList<>();
            return resposta;
        }

        // Reconstroi a pagina passada como referencia a partir
        // do registro lido no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(construtor, ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArray(buffer);

        // Encontra o ponto em que a chave deve estar na pagina
        // Nesse primeiro passo, todas as chaves menores que a chave buscada
        // sao ultrapassadas
        int i = 0;
        while (elem!=null && i < pa.elementos.size() && elem.compareTo(pa.elementos.get(i)) > 0) {
            i++;
        }

        // Chave encontrada (ou pelo menos o ponto onde ela deveria estar).
        // Segundo passo - testa se a chave e a chave buscada e se esta em uma folha
        // Obs.: em uma arvore B+, todas as chaves validas estao nas folhas
        if (i < pa.elementos.size() && pa.filhos.get(0) == -1 && (elem==null || elem.compareTo(pa.elementos.get(i)) == 0)) {

            // Cria a lista de retorno e insere os elementos encontrados
            ArrayList<T> lista = new ArrayList<>();
            while (elem==null || elem.compareTo(pa.elementos.get(i)) <= 0) {

                if (elem==null || elem.compareTo(pa.elementos.get(i)) == 0)
                    lista.add(pa.elementos.get(i));
                i++;

                // Se chegar ao fim da folha, entao avança para a folha seguinte
                if (i == pa.elementos.size()) {
                    if (pa.proxima == -1)
                        break;
                    arquivo.seek(pa.proxima);
                    arquivo.read(buffer);
                    pa.fromByteArray(buffer);
                    i = 0;
                }
            }
            return lista;
        }

        // Terceiro passo - se a chave nao tiver sido encontrada nesta folha,
        // testa se ela esta na proxima folha. Isso pode ocorrer devido ao
        // processo de ordenaçao.
        else if (i == pa.elementos.size() && pa.filhos.get(0) == -1) {

            // Testa se ha uma proxima folha. Nesse caso, retorna um vetor vazio
            if (pa.proxima == -1) {
                ArrayList<T> resposta = new ArrayList<>();
                return resposta;
            }

            // Le a proxima folha
            arquivo.seek(pa.proxima);
            arquivo.read(buffer);
            pa.fromByteArray(buffer);

            // Testa se a chave e a primeira da proxima folha
            i = 0;
            if (elem.compareTo(pa.elementos.get(i)) <= 0) {

                // Cria a lista de retorno
                ArrayList<T> lista = new ArrayList<>();

                // Testa se a chave foi encontrada, e adiciona todas as chaves
                // secundarias
                while (elem.compareTo(pa.elementos.get(i)) <= 0) {
                    if (elem.compareTo(pa.elementos.get(i)) == 0)
                        lista.add(pa.elementos.get(i));
                    i++;
                    if (i == pa.elementos.size()) {
                        if (pa.proxima == -1)
                            break;
                        arquivo.seek(pa.proxima);
                        arquivo.read(buffer);
                        pa.fromByteArray(buffer);
                        i = 0;
                    }
                }

                return lista;
            }

            // Se nao houver uma proxima pagina, retorna um vetor vazio
            else {
                ArrayList<T> resposta = new ArrayList<>();
                return resposta;
            }
        }

        // Chave ainda nao foi encontrada, continua a busca recursiva pela arvore
        if (elem==null || i == pa.elementos.size() || elem.compareTo(pa.elementos.get(i)) <= 0)
            return read1(elem, pa.filhos.get(i));
        else
            return read1(elem, pa.filhos.get(i + 1));
    }

    // Inclusao de novos elementos na arvore. A inclusao e recursiva. A primeira
    // funçao chama a segunda recursivamente, passando a raiz como referencia.
    // Eventualmente, a arvore pode crescer para cima.
    public boolean create(T elem) throws Exception {

        // Carrega a raiz
        arquivo.seek(0);
        long pagina;
        pagina = arquivo.readLong();

        // O processo de inclusao permite que os valores passados como referencia
        // sejam substituídos por outros valores, para permitir a divisao de paginas
        // e crescimento da arvore. Assim, sao usados os valores globais elemAux
        // e chave2Aux. Quando ha uma divisao, as chaves promovidas sao armazenadas
        // nessas variaveis.
        elemAux = elem.clone();

        // Se houver crescimento, entao sera criada uma pagina extra e sera mantido um
        // ponteiro para essa pagina. Os valores tambem sao globais.
        paginaAux = -1;
        cresceu = false;

        // Chamada recursiva para a inserçao do par de chaves
        boolean inserido = create1(pagina);

        // Testa a necessidade de criaçao de uma nova raiz.
        if (cresceu) {

            // Cria a nova pagina que sera a raiz. O ponteiro esquerdo da raiz
            // sera a raiz antiga e o seu ponteiro direito sera para a nova pagina.
            Pagina novaPagina = new Pagina(construtor, ordem);
            novaPagina.elementos = new ArrayList<>(this.maxElementos);
            novaPagina.elementos.add(elemAux);
            novaPagina.filhos = new ArrayList<>(this.maxFilhos);
            novaPagina.filhos.add(pagina);
            novaPagina.filhos.add(paginaAux);

            // Acha o espaço em disco. Testa se ha paginas excluídas.
            arquivo.seek(8);
            long end = arquivo.readLong();
            if(end==-1) {
                end = arquivo.length();
            } else { // reusa um endereço e atualiza a lista de excluídos no cabeçalho
                arquivo.seek(end);
                Pagina pa_excluida = new Pagina(construtor, ordem);
                byte[] buffer = new byte[pa_excluida.TAMANHO_PAGINA];
                arquivo.read(buffer);
                pa_excluida.fromByteArray(buffer);
                arquivo.seek(8);
                arquivo.writeLong(pa_excluida.proxima);
            }
            arquivo.seek(end);
            long raiz = arquivo.getFilePointer();
            arquivo.write(novaPagina.toByteArray());
            arquivo.seek(0);
            arquivo.writeLong(raiz);
            inserido = true;
        }

        return inserido;
    }

    // Funçao recursiva de inclusao. A funçao passa uma pagina de referencia.
    // As inclusões sao sempre feitas em uma folha.
    private boolean create1(long pagina) throws Exception {

        // Testa se passou para o filho de uma pagina folha. Nesse caso,
        // inicializa as variaveis globais de controle.
        if (pagina == -1) {
            cresceu = true;
            paginaAux = -1;
            return false;
        }

        // Le a pagina passada como referencia
        arquivo.seek(pagina);
        Pagina pa = new Pagina(construtor, ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArray(buffer);

        // Busca o proximo ponteiro de descida. Como pode haver repetiçao
        // da primeira chave, a segunda tambem e usada como referencia.
        // Nesse primeiro passo, todos os pares menores sao ultrapassados.
        int i = 0;
        while (i < pa.elementos.size() && (elemAux.compareTo(pa.elementos.get(i)) > 0)) {
            i++;
        }

        // Testa se o registro ja existe em uma folha. Se isso acontecer, entao
        // a inclusao e cancelada.
        if (i < pa.elementos.size() && pa.filhos.get(0) == -1 && elemAux.compareTo(pa.elementos.get(i)) == 0) {
            cresceu = false;
            return false;
        }

        // Continua a busca recursiva por uma nova pagina. A busca continuara ate o
        // filho inexistente de uma pagina folha ser alcançado.
        boolean inserido;
        if (i == pa.elementos.size() || elemAux.compareTo(pa.elementos.get(i)) < 0)
            inserido = create1(pa.filhos.get(i));
        else
            inserido = create1(pa.filhos.get(i + 1));

        // A partir deste ponto, as chamadas recursivas ja foram encerradas.
        // Assim, o proximo codigo so e executado ao retornar das chamadas recursivas.

        // A inclusao ja foi resolvida por meio de uma das chamadas recursivas. Nesse
        // caso, apenas retorna para encerrar a recursao.
        // A inclusao pode ter sido resolvida porque o par de chaves ja existia
        // (inclusao invalida)
        // ou porque o novo elemento coube em uma pagina existente.
        if (!cresceu)
            return inserido;

        // Se tiver espaço na pagina, faz a inclusao nela mesmo
        if (pa.elementos.size() < maxElementos) {

            // Puxa todos elementos para a direita, começando do ultimo
            // para gerar o espaço para o novo elemento e insere o novo elemento
            pa.elementos.add(i, elemAux);
            pa.filhos.add(i + 1, paginaAux);

            // Escreve a pagina atualizada no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.toByteArray());

            // Encerra o processo de crescimento e retorna
            cresceu = false;
            return true;
        }

        // O elemento nao cabe na pagina. A pagina deve ser dividida e o elemento
        // do meio deve ser promovido (sem retirar a referencia da folha).

        // Cria uma nova pagina
        Pagina np = new Pagina(construtor, ordem);

        // Move a metade superior dos elementos para a nova pagina,
        // considerando que maxElementos pode ser ímpar
        int meio = maxElementos / 2;
        np.filhos.add(pa.filhos.get(meio)); // COPIA o primeiro ponteiro
        for (int j = 0; j < (maxElementos - meio); j++) {
            np.elementos.add(pa.elementos.remove(meio)); // MOVE os elementos
            np.filhos.add(pa.filhos.remove(meio + 1)); // MOVE os demais ponteiros
        }

        // Testa o lado de inserçao
        // Caso 1 - Novo registro deve ficar na pagina da esquerda
        if (i <= meio) {
            pa.elementos.add(i, elemAux);
            pa.filhos.add(i + 1, paginaAux);

            // Se a pagina for folha, seleciona o primeiro elemento da pagina
            // da direita para ser promovido, mantendo-o na folha
            if (pa.filhos.get(0) == -1)
                elemAux = np.elementos.get(0).clone();

            // caso contrario, promove o maior elemento da pagina esquerda
            // removendo-o da pagina
            else {
                elemAux = pa.elementos.remove(pa.elementos.size() - 1);
                pa.filhos.remove(pa.filhos.size() - 1);
            }
        }

        // Caso 2 - Novo registro deve ficar na pagina da direita
        else {

            int j = maxElementos - meio;
            while (elemAux.compareTo(np.elementos.get(j - 1)) < 0)
                j--;
            np.elementos.add(j, elemAux);
            np.filhos.add(j + 1, paginaAux);

            // Seleciona o primeiro elemento da pagina da direita para ser promovido
            elemAux = np.elementos.get(0).clone();

            // Se nao for folha, remove o elemento promovido da pagina
            if (pa.filhos.get(0) != -1) {
                np.elementos.remove(0);
                np.filhos.remove(0);
            }

        }

        // Obtem um endereço para a nova pagina (pagina excluída ou fim do arquivo)
        arquivo.seek(8);
        long end = arquivo.readLong();
        if(end==-1) {
            end = arquivo.length();
        } else { // reusa um endereço e atualiza a lista de excluídos no cabeçalho
            arquivo.seek(end);
            Pagina pa_excluida = new Pagina(construtor, ordem);
            buffer = new byte[pa_excluida.TAMANHO_PAGINA];
            arquivo.read(buffer);
            pa_excluida.fromByteArray(buffer);
            arquivo.seek(8);
            arquivo.writeLong(pa_excluida.proxima);
        }

        // Se a pagina era uma folha e apontava para outra folha,
        // entao atualiza os ponteiros dessa pagina e da pagina nova
        if (pa.filhos.get(0) == -1) {
            np.proxima = pa.proxima;
            pa.proxima = end;
        }

        // Grava as paginas no arquivo
        paginaAux = end;
        arquivo.seek(paginaAux);
        arquivo.write(np.toByteArray());

        arquivo.seek(pagina);
        arquivo.write(pa.toByteArray());

        return true;
    }

    // Remoçao elementos na arvore. A remoçao e recursiva. A primeira
    // funçao chama a segunda recursivamente, passando a raiz como referencia.
    // Eventualmente, a arvore pode reduzir seu tamanho, por meio da exclusao da
    // raiz.
    public boolean delete(T elem) throws Exception {

        // Encontra a raiz da arvore
        arquivo.seek(0);
        long pagina;
        pagina = arquivo.readLong();

        // variavel global de controle da reduçao do tamanho da arvore
        diminuiu = false;

        // Chama recursivamente a exclusao de registro (na elemAux e no
        // chave2Aux) passando uma pagina como referencia
        boolean excluido = delete1(elem, pagina);

        // Se a exclusao tiver sido possível e a pagina tiver reduzido seu tamanho,
        // por meio da fusao das duas paginas filhas da raiz, elimina essa raiz
        if (excluido && diminuiu) {

            // Le a raiz
            arquivo.seek(pagina);
            Pagina pa = new Pagina(construtor, ordem);
            byte[] buffer = new byte[pa.TAMANHO_PAGINA];
            arquivo.read(buffer);
            pa.fromByteArray(buffer);

            // Se a pagina tiver 0 elementos, apenas atualiza o ponteiro para a raiz,
            // no cabeçalho do arquivo, para o seu primeiro filho e insere a raiz velha
            // na lista de paginas excluídas
            if (pa.elementos.size() == 0) {
                arquivo.seek(0);
                arquivo.writeLong(pa.filhos.get(0));

                arquivo.seek(8);
                long end = arquivo.readLong();  // cabeça da lista de paginas excluídas
                pa.proxima = end;
                arquivo.seek(8);
                arquivo.writeLong(pagina);
                arquivo.seek(pagina);
                arquivo.write(pa.toByteArray());
            }
        }

        return excluido;
    }

    // Funçao recursiva de exclusao. A funçao passa uma pagina de referencia.
    // As exclusões sao sempre feitas em folhas e a fusao e propagada para cima.
    private boolean delete1(T elem, long pagina) throws Exception {

        // Declaraçao de variaveis
        boolean excluido = false;
        int diminuido;

        // Testa se o registro nao foi encontrado na arvore, ao alcançar uma folha
        // inexistente (filho de uma folha real)
        if (pagina == -1) {
            diminuiu = false;
            return false;
        }

        // Le o registro da pagina no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(construtor, ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArray(buffer);

        // Encontra a pagina em que o par de chaves esta presente
        // Nesse primeiro passo, salta todas os pares de chaves menores
        int i = 0;
        while (i < pa.elementos.size() && elem.compareTo(pa.elementos.get(i)) > 0) {
            i++;
        }

        // Chaves encontradas em uma folha
        if (i < pa.elementos.size() && pa.filhos.get(0) == -1 && elem.compareTo(pa.elementos.get(i)) == 0) {

            // Puxa todas os elementos seguintes para uma posiçao anterior, sobrescrevendo
            // o elemento a ser excluído
            pa.elementos.remove(i);
            pa.filhos.remove(i + 1);

            // Atualiza o registro da pagina no arquivo
            arquivo.seek(pagina);
            arquivo.write(pa.toByteArray());

            // Se a pagina contiver menos elementos do que o mínimo necessario,
            // indica a necessidade de fusao de paginas
            diminuiu = pa.elementos.size() < maxElementos / 2;
            return true;
        }

        // Se a chave nao tiver sido encontrada (observar o return true logo acima),
        // continua a busca recursiva por uma nova pagina. A busca continuara ate o
        // filho inexistente de uma pagina folha ser alcançado.
        // A variavel diminuído mantem um registro de qual pagina eventualmente
        // pode ter ficado com menos elementos do que o mínimo necessario.
        // Essa pagina sera filha da pagina atual
        if (i == pa.elementos.size() || elem.compareTo(pa.elementos.get(i)) < 0) {
            excluido = delete1(elem, pa.filhos.get(i));
            diminuido = i;
        } else {
            excluido = delete1(elem, pa.filhos.get(i + 1));
            diminuido = i + 1;
        }

        // A partir deste ponto, o codigo e executado apos o retorno das chamadas
        // recursivas do metodo

        // Testa se ha necessidade de fusao de paginas
        if (diminuiu) {

            // Carrega a pagina filho que ficou com menos elementos do
            // do que o mínimo necessario
            long paginaFilho = pa.filhos.get(diminuido);
            Pagina pFilho = new Pagina(construtor, ordem);
            arquivo.seek(paginaFilho);
            arquivo.read(buffer);
            pFilho.fromByteArray(buffer);

            // Cria uma pagina para o irmao (da direita ou esquerda)
            long paginaIrmaoEsq = -1, paginaIrmaoDir = -1;
            Pagina pIrmaoEsq = null, pIrmaoDir = null; // inicializados com null para controle de existencia

            // Carrega os irmaos (que existirem)
            if (diminuido > 0) { // possui um irmao esquerdo, pois nao e a primeira filho do pai
                paginaIrmaoEsq = pa.filhos.get(diminuido - 1);
                pIrmaoEsq = new Pagina(construtor, ordem);
                arquivo.seek(paginaIrmaoEsq);
                arquivo.read(buffer);
                pIrmaoEsq.fromByteArray(buffer);
            }
            if (diminuido < pa.elementos.size()) { // possui um irmao direito, pois nao e o ultimo filho do pai
                paginaIrmaoDir = pa.filhos.get(diminuido + 1);
                pIrmaoDir = new Pagina(construtor, ordem);
                arquivo.seek(paginaIrmaoDir);
                arquivo.read(buffer);
                pIrmaoDir.fromByteArray(buffer);
            }

            // Verifica se o irmao esquerdo existe e pode ceder algum elemento
            if (pIrmaoEsq != null && pIrmaoEsq.elementos.size() > maxElementos / 2) {

                // Se for folha, copia o elemento do irmao, ja que o do pai sera extinto ou
                // repetido
                if (pFilho.filhos.get(0) == -1)
                    pFilho.elementos.add(0, pIrmaoEsq.elementos.remove(pIrmaoEsq.elementos.size() - 1));

                // Se nao for folha, desce o elemento do pai
                else
                    pFilho.elementos.add(0, pa.elementos.get(diminuido - 1));

                // Copia o elemento vindo do irmao para o pai (pagina atual)
                pa.elementos.set(diminuido - 1, pFilho.elementos.get(0));

                // Reduz o elemento no irmao
                pFilho.filhos.add(0, pIrmaoEsq.filhos.remove(pIrmaoEsq.filhos.size() - 1));

            }

            // Senao, verifica se o irmao direito existe e pode ceder algum elemento
            else if (pIrmaoDir != null && pIrmaoDir.elementos.size() > maxElementos / 2) {
                // Se for folha
                if (pFilho.filhos.get(0) == -1) {

                    // move o elemento do irmao
                    pFilho.elementos.add(pIrmaoDir.elementos.remove(0));
                    pFilho.filhos.add(pIrmaoDir.filhos.remove(0));

                    // sobe o proximo elemento do irmao
                    pa.elementos.set(diminuido, pIrmaoDir.elementos.get(0));
                }

                // Se nao for folha, rotaciona os elementos
                else {
                    // Copia o elemento do pai, com o ponteiro esquerdo do irmao
                    pFilho.elementos.add(pa.elementos.get(diminuido));
                    pFilho.filhos.add(pIrmaoDir.filhos.remove(0));

                    // Sobe o elemento esquerdo do irmao para o pai
                    pa.elementos.set(diminuido, pIrmaoDir.elementos.remove(0));
                }
            }

            // Senao, faz a fusao com o irmao esquerdo, se ele existir
            else if (pIrmaoEsq != null) {
                // Se a pagina reduzida nao for folha, entao o elemento
                // do pai deve descer para o irmao
                if (pFilho.filhos.get(0) != -1) {
                    pIrmaoEsq.elementos.add(pa.elementos.remove(diminuido - 1));
                    pIrmaoEsq.filhos.add(pFilho.filhos.remove(0));
                }
                // Senao, apenas remove o elemento do pai
                else {
                    pa.elementos.remove(diminuido - 1);
                    pFilho.filhos.remove(0);
                }
                pa.filhos.remove(diminuido); // remove o ponteiro para a propria pagina

                // Copia todos os registros para o irmao da esquerda
                pIrmaoEsq.elementos.addAll(pFilho.elementos);
                pIrmaoEsq.filhos.addAll(pFilho.filhos);
                pFilho.elementos.clear(); 
                pFilho.filhos.clear();

                // Se as paginas forem folhas, copia o ponteiro para a folha seguinte
                if (pIrmaoEsq.filhos.get(0) == -1)
                    pIrmaoEsq.proxima = pFilho.proxima;

                // Insere o filho na lista de paginas excluídas
                arquivo.seek(8);
                pFilho.proxima = arquivo.readLong();
                arquivo.seek(8);
                arquivo.writeLong(paginaFilho);

            }

            // Senao, faz a fusao com o irmao direito, assumindo que ele existe
            else {
                // Se a pagina reduzida nao for folha, entao o elemento
                // do pai deve descer para o irmao
                if (pFilho.filhos.get(0) != -1) {
                    pFilho.elementos.add(pa.elementos.remove(diminuido));
                    pFilho.filhos.add(pIrmaoDir.filhos.remove(0));
                }
                // Senao, apenas remove o elemento do pai
                else {
                    pa.elementos.remove(diminuido);
                    pFilho.filhos.remove(0);
                }
                pa.filhos.remove(diminuido + 1); // remove o ponteiro para o irmao direito

                // Move todos os registros do irmao da direita
                pFilho.elementos.addAll(pIrmaoDir.elementos);
                pFilho.filhos.addAll(pIrmaoDir.filhos);
                pIrmaoDir.elementos.clear(); 
                pIrmaoDir.filhos.clear();

                // Se a pagina for folha, copia o ponteiro para a proxima pagina
                pFilho.proxima = pIrmaoDir.proxima;

                // Insere o irmao da direita na lista de paginas excluídas
                arquivo.seek(8);
                pIrmaoDir.proxima = arquivo.readLong();
                arquivo.seek(8);
                arquivo.writeLong(paginaIrmaoDir);

            }

            // testa se o pai tambem ficou sem o numero mínimo de elementos
            diminuiu = pa.elementos.size() < maxElementos / 2;

            // Atualiza os demais registros
            arquivo.seek(pagina);
            arquivo.write(pa.toByteArray());
            arquivo.seek(paginaFilho);
            arquivo.write(pFilho.toByteArray());
            if (pIrmaoEsq != null) {
                arquivo.seek(paginaIrmaoEsq);
                arquivo.write(pIrmaoEsq.toByteArray());
            }
            if (pIrmaoDir != null) {
                arquivo.seek(paginaIrmaoDir);
                arquivo.write(pIrmaoDir.toByteArray());
            }
        }
        return excluido;
    }

    // Imprime a arvore, usando uma chamada recursiva.
    // A funçao recursiva e chamada com uma pagina de referencia (raiz)
    public void print() throws Exception {
        long raiz;
        arquivo.seek(0);
        raiz = arquivo.readLong();
        System.out.println("Raiz: " + String.format("%04d", raiz));
        if (raiz != -1)
            print1(raiz);
        System.out.println();
    }

    // Impressao recursiva
    private void print1(long pagina) throws Exception {

        // Retorna das chamadas recursivas
        if (pagina == -1)
            return;
        int i;

        // Le o registro da pagina passada como referencia no arquivo
        arquivo.seek(pagina);
        Pagina pa = new Pagina(construtor, ordem);
        byte[] buffer = new byte[pa.TAMANHO_PAGINA];
        arquivo.read(buffer);
        pa.fromByteArray(buffer);

        // Imprime a pagina
        String endereco = String.format("%04d", pagina);
        System.out.print(endereco + "  " + pa.elementos.size() + ":"); // endereço e numero de elementos
        for (i = 0; i < pa.elementos.size(); i++) {
            System.out.print("(" + String.format("%04d", pa.filhos.get(i)) + ") " + pa.elementos.get(i) + " ");
        }
        if (i > 0)
            System.out.print("(" + String.format("%04d", pa.filhos.get(i)) + ")");
        else
            System.out.print("(-001)");
        for (; i < maxElementos; i++) {
            System.out.print(" ------- (-001)");
        }
        if (pa.proxima == -1)
            System.out.println();
        else
            System.out.println(" --> (" + String.format("%04d", pa.proxima) + ")");

        // Chama recursivamente cada filho, se a pagina nao for folha
        if (pa.filhos.get(0) != -1) {
            for (i = 0; i < pa.elementos.size(); i++)
                print1(pa.filhos.get(i));
            print1(pa.filhos.get(i));
        }
    }

}
