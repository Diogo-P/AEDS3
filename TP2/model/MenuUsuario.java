package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuUsuario {

    private static int id; // ID DO USUARIO LOGADO
    private final ArquivoUsuario arquivoUsuario;
    private final ArquivoLista arquivoLista;
    private final ArquivoProduto arqProduto;
    private final ArquivoListaProduto arquivoListaProduto;
    Lista[] usuarioListas;

    Scanner sc = new Scanner(System.in);

    private static String gerarGtin13() {
        StringBuilder gtin = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            gtin.append((int)(Math.random() * 10));
        }
        
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = gtin.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        gtin.append(checkDigit);
        
        return gtin.toString();
    }

    public MenuUsuario(int id) throws Exception {
        this.id = id;
        arquivoUsuario = new ArquivoUsuario();
        arquivoLista = new ArquivoLista();
        arqProduto = new ArquivoProduto();
        arquivoListaProduto = new ArquivoListaProduto();
        usuarioListas = arquivoLista.carregaListas(id);
    }

    //Carregar listas do usuário para memória principal        
    public Lista lista_opcao_c() throws Exception {
        //Apenas esses 3 dados estarão no input, os outros são automáticos
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Data Limite (dd/MM/yyyy): ");
        String limite = sc.nextLine();
        //Manipulando datas
        LocalDate dataCriacao = LocalDate.now();
        System.out.println(dataCriacao);
        LocalDate dataLimite = LocalDate.now();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String tmp = dataCriacao.format(formatter);
            dataCriacao = LocalDate.parse(tmp, formatter);
            dataLimite = LocalDate.parse(limite, formatter);
        } catch (Exception e) {
            System.out.println("Erro na formatação da data");
        }

        //Cria o objeto e dá sequência ao fluxo
        Lista lista = new Lista(-1, nome, descricao, dataCriacao, dataLimite, " ", -1, true);
        lista.setCodigo();
        lista.setUID(id);
        return lista;

    }

    public Lista lista_opcao_b(String codigo) {
        Lista lista = arquivoLista.lerCodigo(codigo);
        return lista;
    }

    public void lista_opcao_r(Lista lista) throws Exception {

        try {
            System.out.println(lista.toString());

            /*
            MenuUsuario menu = new MenuUsuario();
            //O tamanho do registro é 117 bytes
            short tam = 117;
            //É definida a quantidade de registros do arquivo
            long regs = arquivoLista.length()/tam;
            //Lê o nome da lista a ser buscada
            Scanner sc = new Scanner(System.in);
            String nome;
            nome=sc.nextLine();
            //Printa a(s) lista(s) com nome igual ao buscado
            for(long i=0;i<regs;i++){
                if(nome.equals())
                Lista[] listas  = arquivoLista.read();
                i=i*tam;
            }

            short tam = 117;
            long regs = arquivoLista.length/tam;
            for(long i = 0; i<regs;i++){
                arquivoLista.read(id);
            } */

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 
        //Apenas esses 3 dados estarão no input, os outros são automáticos
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Data Limite: ");
        String limite = sc.nextLine();
        //Manipulando datas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCriacao = LocalDate.now();
        String tmp = dataCriacao.format(formatter);
        dataCriacao = LocalDate.parse(tmp,formatter);
        LocalDate dataLimite = LocalDate.parse(limite,formatter);
        //Cria o objeto e dá sequência ao fluxo
        Lista lista =  new Lista(-1,nome,descricao,dataCriacao,dataLimite,"",-1,true);
        // return lista;
        */
    }

    private static void mostrarListas(Lista[] listas) {
        System.out.println("\n=== Listas cadastradas ===");
        for (int i = 0; i < listas.length; i++) {
            System.out.println((i + 1) + " - " + listas[i].getNome() + " (ID: " + listas[i].getID() + ")");
        }
    }

    public boolean update(Lista lista) throws Exception {
        return arquivoLista.update(lista);
    }

    public static void lista_delete(Lista lista) throws Exception {
        ArquivoLista arqLista = new ArquivoLista();

        Lista[] listas = arqLista.carregaListas(id);

        if (listas == null || listas.length == 0) {
            System.out.println("Nenhuma lista encontrada.");
            return;
        }

        // Pega o ID da lista
        int id = lista.getID();
        boolean sucesso = arqLista.delete(id);

        if (sucesso) {
            if (!arqLista.deleteIndices(lista)) {
              //  System.err.println("Erro na remoção dos indices");
            }
            System.out.println("Lista removida com sucesso!");
        } else {
            System.out.println("Lista não encontrada ou já foi excluída.");
        }
    }

    public void telaProdutoSelecionadoPelaLista(Produto produto, ListaProduto listaProduto, int caminho, Lista listaParametro) throws Exception {
        boolean runningSelecionado = true;

        Lista lista;

        while (runningSelecionado) {
            String op;

            lista = listaParametro;

            if ( listaProduto == null ) { // quando relacao nao existe, ocorre dentro de Procurar por Gtin-13 e Listagem de produtos
                if ( caminho == 2 && lista != null) {
                    System.out.println("\n> Início > Listas > " + lista.getNome() + " > Gerenciar produtos > Vincular produtos > Listar produtos > " + produto.getNome());
                } else if ( lista != null ) {
                    System.out.println("\n> Início > Listas > " + lista.getNome() + " > Gerenciar produtos > Vincular produtos > Procurar por GTIN-13 > " + produto.getNome());
                } else {
                    System.err.println("Erro ao achar nome da lista selecionada para acrescentar produto");
                    runningSelecionado = false;
                    break;
                }

                System.out.println("\n===============================");
                System.out.println("       PresenteFácil 1.0       ");
                System.out.println("===============================");
                System.out.println(produto.toString());
                System.out.println("\nDeseja adicionar esse produto a lista " + lista.getNome() + "?\n");
                System.out.println("(A) Aceitar");
                System.out.println();
                System.out.println("(S) Sair");
                System.out.println();
                System.out.print("Opção: ");
                op = sc.nextLine().trim().toUpperCase();

                switch (op) {
                    case "A":
                        System.out.println("Quantidade do produto desejada: ");
                        int qtd = Integer.parseInt(sc.nextLine());
                        System.out.println("Observações (opcional): ");
                        String obs = sc.nextLine();

                        ListaProduto lp = new ListaProduto(lista.getID(), produto.getID(), qtd, obs);
                        int id = arquivoListaProduto.create(lp);
                        System.out.println("Produto adicionado à lista!");
                        runningSelecionado = false;
                        break;
                    case "S":
                        runningSelecionado = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }

            } else { // quando relacao existe, ocorre dentro de Operacoes dentro da Lista, ou seja selecionando produto da lista
                if ( lista == null ) {
                    System.err.println("Erro ao achar nome da lista selecionada para acrescentar produto");
                    runningSelecionado = false;
                    break;
                }
                System.out.println("\n> Início > Listas > " + lista.getNome() + " > Gerenciar produtos > " + produto.getNome());
                System.out.println("\n===============================");
                System.out.println("       PresenteFácil 1.0       ");
                System.out.println("===============================");
                System.out.println(produto.toString());
                // Mostrar dados da associação (quantidade e observações)
                if (listaProduto != null) {
                    System.out.println("\n-- Dados desta lista --");
                    System.out.println("Quantidade: " + listaProduto.getQuantidade());
                    System.out.println("Observações: " + (listaProduto.getObservacoes() != null ? listaProduto.getObservacoes() : ""));
                }
                System.out.println("\nO que deseja realizar com a linkagem do produto acima com a lista " + lista.getNome() + "?\n");
                System.out.println("(A) Alterar linkagem");
                System.out.println("(R) Excluir linkagem / Remover produto da lista");
                System.out.println();
                System.out.println("(S) Sair");
                System.out.println();
                System.out.print("Opção: ");
                op = sc.nextLine().trim().toUpperCase();

                switch (op) {
                    case "A":
                        ListaProduto lp = arquivoListaProduto.read(listaProduto.getID());
                        if (lp == null) {
                            System.out.println("Associação não encontrada.");
                            return;
                        }
                        System.out.println("Nova quantidade (Qnt atual: " + lp.getQuantidade() + ") Pressione Enter para ignorar: ");
                        String sQtd = sc.nextLine();
                        if (!sQtd.isBlank()) lp.setQuantidade(Integer.parseInt(sQtd));
                        System.out.println("Nova observação (Observação atual: " + (lp.getObservacoes() != null ? lp.getObservacoes() : "") + ") Pressione Enter para ignorar: ");
                        String obs = sc.nextLine();
                        if (!obs.isBlank()) lp.setObservacoes(obs);
                        if (arquivoListaProduto.update(lp)) System.out.println("Associação atualizada!");
                        else System.out.println("Falha ao atualizar.");
                        break;
                    case "R":
                        if (arquivoListaProduto.delete(listaProduto.getID()))  {
                            System.out.println("Associação removida!");
                            runningSelecionado = false;
                        }
                        else System.out.println("Falha ao remover (pode não existir).");
                        break;
                    case "S":
                        runningSelecionado = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }       
        }
    }

    public void telaProdutoSelecionadoPeloMenu(Produto produto) throws Exception {
        boolean runningSelecionado = true;

        Produto produtoAtual = produto;

        while (runningSelecionado) {

            System.out.println();

            String op;

            if ( produtoAtual.getInativo() == true ) {
                System.out.println("\n> Início > Produtos > " + produtoAtual.getNome());
                System.out.println("\n===============================");
                System.out.println("       PresenteFácil 1.0       ");
                System.out.println("===============================");
                System.out.println(produtoAtual.toString());
                System.out.println("\nESTE PRODUTO ESTÁ INATIVADO.\n");
                System.out.println("(A) Ativar produto");
                System.out.println();
                System.out.println("(S) Sair");
                System.out.println();
                System.out.print("Opção: ");

                op = sc.nextLine().toUpperCase();

                switch (op) {
                    case "A":
                        System.out.println("Ativar produto? (S) para confirmar, (Outra tecla para recusar): ");
                        op = sc.nextLine().trim().toUpperCase();
                        if ( op.equals("S") ) {
                            produtoAtual.setInativo(false);
                            if (arqProduto.update(produtoAtual)) {
                                System.out.println("Produto ativado com sucesso!");
                            } else {
                                System.out.println("Erro ao ativar o produto.");
                                produtoAtual.setInativo(true); // Desfaz a mudança em memória se a gravação falhar
                            }
                        }
                        break;
                    case "S":
                        runningSelecionado = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            } else {
                ListaProduto[] listas = arquivoListaProduto.readPorProduto(produtoAtual.getID());
                ArrayList<Lista> minhasListasList = new ArrayList<>();
                for ( int z = 0; z < listas.length; z++ ) {
                    Lista poss = arquivoLista.read(listas[z].getIdLista());
                    if ( poss != null && poss.getUID() == id ) {
                        minhasListasList.add(poss);
                    }
                }
                Lista[] minhasListas = new Lista[minhasListasList.size()];
                int z = 0;
                for ( Lista lista : minhasListasList ) {
                    minhasListas[z++] = lista;
                }

                System.out.println("\n> Início > Produtos > " + produtoAtual.getNome());
                System.out.println("\n===============================");
                System.out.println("       PresenteFácil 1.0       ");
                System.out.println("===============================");

                System.out.println(produtoAtual.toString());

                if ( minhasListas.length == 0 ) {
                    System.out.println("Não aparece em nenhuma das minhas listas."); 
                } else {
                    System.out.println("Aparece nas minhas listas:");
                    z = 1;
                    for ( Lista lista : minhasListas ) {
                        System.out.println("["+ z +"]" + lista.getNome());
                        z++;
                    } 
                }
                if ( listas.length - minhasListas.length <= 0 ) {
                    System.out.println("Não aparece nas listas de outras pessoas.");
                } else {
                    int valor = listas.length - minhasListas.length;
                    System.out.println("Aparece também em mais " + valor + "istas de outras pessoas.");
                }
                
                System.out.println("(A) Alterar dados do produto");
                System.out.println("(I) Inativar produto");
                System.out.println();
                System.out.println("(S) Sair");
                System.out.println();
                System.out.print("Opção: ");

                op = sc.nextLine().toUpperCase();

                switch (op) {
                    case "A":
                        Produto p = produtoAtual;

                        if (p == null) {
                            System.out.println("Produto não encontrado.");
                            return;
                        }

                        System.out.println("Produto atual:");
                        System.out.println(p);

                       

                        System.out.println("Novo nome (atual: " + p.getNome() + "): ");
                        String novoNome = sc.nextLine();
                        if (!novoNome.trim().isEmpty()) p.setNome(novoNome);

                        System.out.println("Nova descrição (atual: " + p.getDescricao() + "): ");
                        String novaDesc = sc.nextLine();
                        if (!novaDesc.trim().isEmpty()) p.setDescricao(novaDesc);

                        if (arqProduto.update(p))
                            System.out.println("Produto atualizado com sucesso!");
                        else
                            System.out.println("Falha ao atualizar produto.");
                        break;
                    case "I":
                        System.out.println("Inativar produto? (S) para confirmar, (Outra tecla para recusar): ");
                        op = sc.nextLine().trim().toUpperCase();
                        if ( op.equals("S") ) {
                            produtoAtual.setInativo(true);
                            if (arqProduto.update(produtoAtual)) {
                                System.out.println("Produto inativado com sucesso!");
                            } else {
                                System.out.println("Erro ao inativar o produto.");
                                produtoAtual.setInativo(false); // Desfaz a mudança em memória se a gravação falhar
                            }
                        }
                        break;
                    case "S":
                        runningSelecionado = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }
        }
    }

    public void listarProdutosPeloMenu() throws Exception {
        boolean runningListagem = true;

        Produto[] produtos = arqProduto.listProdutosInativados();

        int paginaAtual = 0; // multiplos de 10. Elemento 0 a 9 é da pagina 1 e 10 a 19 da página 2. Assim por diante.

        while (runningListagem) {
            System.out.println("\n> Início > Produtos > Listar Produtos ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();

            Produto[] produtosPagina = new Produto[10];
            for ( int z = 0; z < 10 && z+paginaAtual < produtos.length; z++ ) {
                produtosPagina[z] = produtos[paginaAtual+z];
            }
            int z = 1;
            for ( Produto produto : produtosPagina ) {
                if ( produto != null ) {
                    System.out.print("["+ z +"]" + produto.getNome());
                    if ( produto.getInativo() == true ) {
                        System.out.print(" ( INATIVADO )");
                    }
                    System.out.println();
                    z++;
                }
            }
            System.out.println();

            int paginaAtualTela = paginaAtual/10+1;
            int paginaMaxima = produtos.length/10+1;
            System.out.println("Página atual: (" + paginaAtualTela + "/" + paginaMaxima + ")");
            System.out.println("(A) Página anterior / (P) Próxima página");
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Digite o numero do produto listado ou A/P para trocar de páginas: ");
            String op;

            op = sc.nextLine();
            boolean numero = false;
            boolean numeroInvalido = false;
            try {
                int resultado = Integer.parseInt(op);
                if ( resultado >= 1 && resultado <= 10 ) {
                    numero = true;
                } else {
                    numeroInvalido = true;
                }
            } catch (NumberFormatException e) {
                //numero nao pode ser convertido
            }
            
            if ( numero ) {
                int resultado = Integer.parseInt(op);
                if ( produtosPagina[resultado-1] == null ) {
                    System.out.println("Não há um produto nesta posição.");
                } else {
                    telaProdutoSelecionadoPeloMenu(produtosPagina[resultado-1]);
                }
            } else if (numeroInvalido) {
                System.out.println("Numero entrado invalido.");
            } else {
                switch (op) {
                    case "A":
                        paginaAtual -= 10;
                        if ( paginaAtual < 0 ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Primeira página atingida.");
                        }
                        break;
                    case "P":
                        paginaAtual += 10;
                        if ( paginaAtual > produtos.length ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Última página atingida.");
                        }
                        break;
                    case "S":
                        runningListagem = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }
        }
    }

    public void listarProdutosPelaLista(Lista lista) throws Exception {
        boolean runningListagem = true;

        Produto[] produtos = arqProduto.listProdutos();

        int paginaAtual = 0; // multiplos de 10. Elemento 0 a 9 é da pagina 1 e 10 a 19 da página 2. Assim por diante.

        while (runningListagem) {
            System.out.println("\n> Início > Listas > " + lista.getNome() + " > Gerenciar produtos > Vincular produtos > Listar produtos");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();

            Produto[] produtosPagina = new Produto[10];
            for ( int z = 0; z < 10 && z+paginaAtual < produtos.length; z++ ) {
                produtosPagina[z] = produtos[paginaAtual+z];
            }
            int z = 1;
            for ( Produto produto : produtosPagina ) {
                if ( produto != null ) {
                    System.out.print("["+ z +"]" + produto.getNome());
                    if ( produto.getInativo() == true ) {
                        System.out.print(" ( INATIVADO )");
                    }
                    System.out.println();
                    z++;
                }
            }
            System.out.println();

            int paginaAtualTela = paginaAtual/10+1;
            int paginaMaxima = produtos.length/10+1;
            System.out.println("Página atual: (" + paginaAtualTela + "/" + paginaMaxima + ")");
            System.out.println("(A) Página anterior / (P) Próxima página");
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Digite o numero do produto listado ou A/P para trocar de páginas: ");
            String op;

            op = sc.nextLine();
            boolean numero = false;
            boolean numeroInvalido = false;
            try {
                int resultado = Integer.parseInt(op);
                if ( resultado >= 1 && resultado <= 10 ) {
                    numero = true;
                } else {
                    numeroInvalido = true;
                }
            } catch (NumberFormatException e) {
                //numero nao pode ser convertido
            }
            
            if ( numero ) {
                int resultado = Integer.parseInt(op);
                if ( produtosPagina[resultado-1] == null ) {
                    System.out.println("Não há um produto nesta posição.");
                } else {
                    boolean relacaoExiste = false;
                    ListaProduto[] relacoes = arquivoListaProduto.readPorProduto(produtosPagina[resultado-1].getID());
                    ListaProduto relacaoExistente = null;
                    for ( ListaProduto relacao : relacoes ) {
                        if ( relacao.getIdLista() == lista.getID() ) {
                            relacaoExiste = true;
                            relacaoExistente = relacao;
                            break;
                        }
                    }
                    if ( relacaoExiste ) {
                        System.out.println("Linkagem do produto com lista " + lista.getNome() + "já existe. Deseja alterar variáveis da relação entre esse produto e sua lista?\n(A) Para aceitar (Outra tecla) Para sair");
                        op = sc.nextLine().trim().toUpperCase();
                        if ( op.equals("A") ) {
                            ListaProduto lp = arquivoListaProduto.read(relacaoExistente.getID());
                            if (lp == null) {
                                System.out.println("Associação não encontrada.");
                                return;
                            }
                            System.out.print("Nova quantidade (Qnt atual: " + lp.getQuantidade() + ") Pressione Enter para ignorar: ");
                            String sQtd = sc.nextLine();
                            if (!sQtd.isBlank()) lp.setQuantidade(Integer.parseInt(sQtd));
                            System.out.print("Nova observação (Observação atual: " + (lp.getObservacoes() != null ? lp.getObservacoes() : "") + ") Pressione Enter para ignorar: ");
                            String obs = sc.nextLine();
                            if (!obs.isBlank()) lp.setObservacoes(obs);
                            if (arquivoListaProduto.update(lp)) System.out.println("Associação atualizada!");
                            else System.out.println("Falha ao atualizar.");
                        } else {
                            runningListagem = false;
                        }
                    } else {
                        telaProdutoSelecionadoPelaLista(produtosPagina[resultado-1], null, 2, lista);
                    }
                }
            } else if (numeroInvalido) {
                System.out.println("Numero entrado invalido.");
            } else {
                switch (op) {
                    case "A":
                        paginaAtual -= 10;
                        if ( paginaAtual < 0 ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Primeira página atingida.");
                        }
                        break;
                    case "P":
                        paginaAtual += 10;
                        if ( paginaAtual > produtos.length ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Última página atingida.");
                        }
                        break;
                    case "S":
                        runningListagem = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }
        }
    }

    public void procurarPorGtinNaLista(Lista lista) throws Exception {
        boolean runningGtin = true;

        while (runningGtin) {
            System.out.println("\n> Início > Listas > " + lista.getNome() + " Gerenciar produtos > Vincular produtos > Procurar por GTIN-13 ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();
            System.out.println("(Código do Produto) Pesquisar por produto pelo GTIN-13");
            System.out.println("(S) Sair");
            String op;

            op = sc.nextLine().trim().toUpperCase();

            if ( op.equals("S") ) {
                runningGtin = false;
            } else {
                Produto p = arqProduto.readGtin(op);
                if ( p == null ) {
                    System.out.println("Produto não encontrado com esse GTIN-13.");
                    runningGtin = false;
                    break;
                }

                ListaProduto[] relacoes = arquivoListaProduto.readPorProduto(p.getID());
                boolean relacaoEncontrada = false;
                ListaProduto relacaoExistente = null;
                for ( ListaProduto relacao : relacoes ) {
                    if ( relacao.getIdLista() == lista.getID() ) {
                        relacaoEncontrada = true;
                        relacaoExistente = relacao;
                    }
                }

                if ( relacaoEncontrada ) {
                    System.out.println("Linkagem do produto com lista " + lista.getNome() + "já existe. Deseja alterar variáveis da relação entre esse produto e sua lista?\n(A) Para aceitar (Outra tecla) Para sair");
                    op = sc.nextLine().trim().toUpperCase();
                    if ( op.equals("A") ) {
                        ListaProduto lp = arquivoListaProduto.read(relacaoExistente.getID());
                        if (lp == null) {
                            System.out.println("Associação não encontrada.");
                            return;
                        }
                        System.out.print("Nova quantidade (Qnt atual: " + lp.getQuantidade() + ") Pressione Enter para ignorar: ");
                        String sQtd = sc.nextLine();
                        if (!sQtd.isBlank()) lp.setQuantidade(Integer.parseInt(sQtd));
                        System.out.print("Nova observação (Observação atual: " + (lp.getObservacoes() != null ? lp.getObservacoes() : "") + ") Pressione Enter para ignorar: ");
                        String obs = sc.nextLine();
                        if (!obs.isBlank()) lp.setObservacoes(obs);
                        if (arquivoListaProduto.update(lp)) System.out.println("Associação atualizada!");
                        else System.out.println("Falha ao atualizar.");
                        runningGtin = false;
                    } else {
                        runningGtin = false;
                    }
                } else {
                    if (p != null) {
                        telaProdutoSelecionadoPelaLista(p, null, 1, lista);
                        runningGtin = false;
                    }
                    else
                        System.out.println("Produto não encontrado."); 
                }
            }
        }
    }

    public void acrescentarProdutosNaLista(Lista lista) throws Exception {
        boolean runningOperacoes = true;

        while (runningOperacoes) {
            System.out.println("\n> Início > Listas > " + lista.getNome() + " Gerenciar produtos > Vincular produtos ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();
            System.out.println("(G) Acrescentar produto por GTIN-13");
            System.out.println("(L) Acrescentar produto por listagem de produtos por nome");
            System.out.println("(S) Sair");
            String op;

            op = sc.nextLine().trim().toUpperCase();

            switch (op) {
                case "G":
                    procurarPorGtinNaLista(lista);
                    break;
                case "L":
                    listarProdutosPelaLista(lista);
                case "S":
                    runningOperacoes = false;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }

    }

    public void telaOperacoesDentroDeLista(Lista lista) throws Exception { 
        boolean runningOperacoes = true;

        int paginaAtual = 0; // multiplos de 10. Elemento 0 a 9 é da pagina 1 e 10 a 19 da página 2. Assim por diante.

        while (runningOperacoes) {
            ListaProduto[] produtosRelacionados = arquivoListaProduto.readPorLista(lista.getID());
            ArrayList<Produto> produtosList = new ArrayList<>();

            for ( int z = 0; z < produtosRelacionados.length; z++ ) {
                produtosList.add(arqProduto.readId(produtosRelacionados[z].getIdProduto()));
            }        

            Produto[] produtosLista = new Produto[produtosList.size()];
            int z = 0;
            for ( Produto produto : produtosList ) {
                produtosLista[z++] = produto; 
            }

            System.out.println("\n> Início > Listas > " + lista.getNome() + " > Gerenciar produtos > ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();

            Produto[] produtosPagina = new Produto[10];
            for ( z = 0; z < 10 && z+paginaAtual < produtosLista.length; z++ ) {
                produtosPagina[z] = produtosLista[paginaAtual+z];
            }
            
            for ( int i = 0; i < 10 && i+paginaAtual < produtosLista.length; i++ ) {
                Produto produto = produtosPagina[i];
                if ( produto != null ) {
                    System.out.print("[" + (i+1) + "] " + produto.getNome());
                    if ( produto.getInativo() == true ) {
                        System.out.print(" ( INATIVADO )");
                    }
                    // associação correspondente
                    if ( produtosRelacionados != null && paginaAtual + i < produtosRelacionados.length ) {
                        ListaProduto assoc = produtosRelacionados[paginaAtual + i];
                        if ( assoc != null ) {
                            System.out.print(" | Qtd: " + assoc.getQuantidade());
                            if ( assoc.getObservacoes() != null && !assoc.getObservacoes().isBlank() ) {
                                System.out.print(" | Obs: " + assoc.getObservacoes());
                            }
                        }
                    }
                    System.out.println();
                }
            }
            System.out.println();

            System.out.println("(V) Vincular produto a lista selecionada");

            System.out.println();
            int paginaAtualTela = paginaAtual/10+1;
            int paginaMaxima = produtosLista.length/10+1;
            System.out.println("Página atual: (" + paginaAtualTela + "/" + paginaMaxima + ")");
            System.out.println("(A) Página anterior / (P) Próxima página");
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Digite o numero do produto listado ou operação para ser executada: ");
            String op;

            op = sc.nextLine().trim().toUpperCase();
            boolean numero = false;
            boolean numeroInvalido = false;
            try {
                int resultado = Integer.parseInt(op);
                if ( resultado >= 1 && resultado <= 10 ) {
                    numero = true;
                } else {
                    numeroInvalido = true;
                }
            } catch (NumberFormatException e) {
                //numero nao pode ser convertido
            }
            
            if ( numero ) {
                int resultado = Integer.parseInt(op);
                if ( produtosPagina[resultado-1] == null ) {
                    System.out.println("Não há um produto nesta posição.");
                } else {
                    Produto produto = produtosPagina[resultado-1];
                    ListaProduto[] listaProdutos = arquivoListaProduto.readPorProduto(produto.getID());
                    ListaProduto listaProduto = new ListaProduto();
                    boolean relacaoEncontrada = false;
                    for ( ListaProduto index : listaProdutos ) {
                        if (index.getIdLista() == lista.getID()) {
                            listaProduto = index;
                            relacaoEncontrada = true;
                        }
                    }
                    if ( ! relacaoEncontrada ) {
                        System.err.println("Erro no sistema ao encontrar relacao do produto " + produto.getNome() + " com lista " + lista.getNome());
                    } else {
                        telaProdutoSelecionadoPelaLista(produto, listaProduto, 0, lista);
                    }
                }
            } else if (numeroInvalido) {
                System.out.println("Numero entrado invalido.");
            } else {
                switch (op) {
                    case "V":
                        acrescentarProdutosNaLista(lista);
                        break;
                    case "A":
                        paginaAtual -= 10;
                        if ( paginaAtual < 0 ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Primeira página atingida.");
                        }
                        break;
                    case "P":
                        paginaAtual += 10;
                        if ( paginaAtual > produtosLista.length ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Última página atingida.");
                        }
                        break;
                    case "S":
                        runningOperacoes = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }
        }
    }

    public void telaMenuListaSelecionada(Lista lista) throws Exception {
        boolean runningSelecionada = true;

        Lista listaAtual = lista;

        while (runningSelecionada) {

            System.out.println();

            String op;

            System.out.println("\n> Início > Listas > " + lista.getNome() + " > ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println(lista.toString());
            System.out.println("(P) Gerenciar produtos da lista");
            System.out.println("(U) Alterar dados da lista");
            System.out.println("(D) Apagar lista");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            op = sc.nextLine().toUpperCase();

            switch (op) {
                case "P":
                    telaOperacoesDentroDeLista(listaAtual);
                    break;
                case "U":
                    try {
                        Lista listaAntiga = listaAtual;

                        Lista listaNova = lista_opcao_c();

                        listaAntiga.setNome(listaNova.getNome());
                        listaAntiga.setDescricao(listaNova.getDescricao());
                        listaAntiga.setLimite(listaNova.getLimite());

                        if (arquivoLista.update(listaAntiga)) {
                            System.out.println("Lista alterada com sucesso");
                        } else {
                            System.out.println("Erro ao alterar a lista");

                        }

                    } catch (Exception e) {
                        System.err.println("\nErro no sistema ao atualizar lista.");
                    }
                    break;
                case "D":
                    lista_delete(listaAtual);
                    runningSelecionada = false;
                    break;
                case "S":
                    runningSelecionada = false;
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    public void telaMenuLista() throws Exception {
        boolean runningMenuLista = true;

        ArquivoLista arqLista;
        try {
            arqLista = new ArquivoLista();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (runningMenuLista) {

            /*
            short i = 0;
            while(usuarioListas[i].getID() != -1){
                System.out.println("["+i+"] "+usuarioListas[i].getNome());
                i++;
            }*/
            usuarioListas = arquivoLista.carregaListas(id);

            System.out.println();

            String op;

            // Cabeçalho
            System.out.println("\n> Início > Listas");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");

            // Listagem das listas do usuário 
            System.out.println();
            System.out.println("Listas:");
            System.out.println("-------------------------------");
            int y = 0;
            int n = 0;
            while (y < usuarioListas.length) {
                if (usuarioListas[y] != null) {
                    int index = n++ + 1;
                    System.out.println("[" + index + "] " + usuarioListas[y].getNome());
                }
                y++;
            }
            System.out.println("-------------------------------");
            System.out.println("(L) Selecionar lista para gerenciar");
            System.out.println("(C) Criar lista");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            op = sc.nextLine();

            switch (op.toUpperCase()) {
                case "L":
                    if (usuarioListas == null || usuarioListas.length == 0) {
                        System.out.println("Nenhuma lista encontrada.");
                        break;
                    }
                    System.out.print("Escolha (por posição) uma lista para consultar: ");
                    int pos = -1;
                    String input = sc.nextLine();
                    try {
                        pos = Integer.parseInt(input);
                    } catch (Exception e) {
                        System.out.println("Entrada não corresponde a um número");
                    }

                    Lista listaConsulta = usuarioListas[pos-1];
                    if ( listaConsulta == null ) {
                        System.out.println("Lista não existente na determinada posição");
                        break;
                    }
                    telaMenuListaSelecionada(listaConsulta);
                    break;
                case "C":
                    try {
                        Lista novaLista = lista_opcao_c();
                        arqLista.create(novaLista, id);
                        System.out.println("\nLista criada com sucesso!");
                        System.out.println(novaLista.toString());
                    } catch (Exception e) {
                        System.err.println("\nErro no sistema ao criar lista");
                    }
                    break;
                case "S":
                    runningMenuLista = false;
                    break;

                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    public void telaPaginaDeProdutosNome(Produto[] produtos) throws Exception {
        boolean runningListagem = true;

        int paginaAtual = 0; // multiplos de 10. Elemento 0 a 9 é da pagina 1 e 10 a 19 da página 2. Assim por diante.

        while (runningListagem) {
            System.out.println("\n> Início > Produtos > Encontrar por nome ");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println();

                System.out.println();

                Produto[] produtosPagina = new Produto[10];
            for ( int z = 0; z < 10 && z+paginaAtual < produtos.length; z++ ) {
                produtosPagina[z] = produtos[paginaAtual+z];
            }
            int z = 1;
            for ( Produto produto : produtosPagina ) {
                if ( produto != null ) {
                    System.out.print("["+ z +"]" + produto.getNome());
                    if ( produto.getInativo() == true ) {
                        System.out.print(" ( INATIVADO )");
                    }
                    System.out.println();
                    z++;
                }
            }
            System.out.println();
            System.out.println("-------------------------------");


            int paginaAtualTela = paginaAtual/10+1;
            int paginaMaxima = produtos.length/10+1;
            System.out.println("Página atual: (" + paginaAtualTela + "/" + paginaMaxima + ")");
            System.out.println("(A) Página anterior / (P) Próxima página");
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Digite o numero do produto listado ou A/P para trocar de páginas: ");
            String op;

            op = sc.nextLine();
            boolean numero = false;
            boolean numeroInvalido = false;
            try {
                int resultado = Integer.parseInt(op);
                if ( resultado >= 1 && resultado <= 10 ) {
                    numero = true;
                } else {
                    numeroInvalido = true;
                }
            } catch (NumberFormatException e) {
                //numero nao pode ser convertido
            }
            
            if ( numero ) {
                int resultado = Integer.parseInt(op);
                if ( produtosPagina[resultado-1] == null ) {
                    System.out.println("Não há um produto nesta posição.");
                } else {
                    telaProdutoSelecionadoPeloMenu(produtosPagina[resultado-1]); 
                }
            } else if (numeroInvalido) {
                System.out.println("Numero entrado invalido.");
            } else {
                switch (op) {
                    case "A":
                        paginaAtual -= 10;
                        if ( paginaAtual < 0 ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Primeira página atingida.");
                        }
                        break;
                    case "P":
                        paginaAtual += 10;
                        if ( paginaAtual > produtos.length ) {
                            paginaAtual = 0;
                            System.out.println("Operação inválida. Última página atingida.");
                        }
                        break;
                    case "S":
                        runningListagem = false;
                        break;
                    default:
                        System.out.println("Opção Inválida!");
                        break;
                }
            }
        }
    }

    public void telaMenuProduto() throws Exception {
        boolean runningMenuProduto = true;

        while ( runningMenuProduto ) {
            String op;
        
            System.out.println("\n> Início > Produtos");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println("(C) Cadastrar produto");
            System.out.println("(L) Listar produtos");
            //System.out.println("(3) Buscar produto por ID");
            System.out.println("(G) Buscar produto por GTIN-13");
            System.out.println("(N) Buscar produtos por nome");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            op = sc.nextLine();

            switch (op) {
                case "C": {
                    System.out.println("Nome: ");
                    String nome = sc.nextLine();
                    System.out.println("Descrição: ");
                    String descricao = sc.nextLine();
                    String gtin = gerarGtin13();
                    System.out.println("GTIN-13 gerado: " + gtin);
                   

                    Produto p = new Produto(gtin, nome, descricao, false);
                    int id = arqProduto.create(p);
                    System.out.println("Produto cadastrado com sucesso!"); }
                    break;
                case "L": {
                    listarProdutosPeloMenu(); }
                    break;
                    /*
                case "3": {
                    System.out.print("ID do produto: ");
                    int idProduto = Integer.parseInt(sc.nextLine());
                    Produto p = arqProduto.read(idProduto);
                    if (p != null)
                        System.out.println(p.toString());
                    else
                        System.out.println("Produto não encontrado."); }
                    break; 
                */
                case "G": { 
                    System.out.print("GTIN-13: ");
                    String gtin = sc.nextLine();
                    Produto p = arqProduto.readGtin(gtin);
                    if (p != null)
                        telaProdutoSelecionadoPeloMenu(p);
                    else
                        System.out.println("Produto não encontrado."); }
                    break;
                case "N": {
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        Produto[] produtos = arqProduto.readNome(nome);
                        if (produtos.length > 0) {
                            telaPaginaDeProdutosNome(produtos);
                        } else {
                            System.out.println("Nenhum produto encontrado com esse nome.");
                        } }  
                    break;
                case "S":
                    runningMenuProduto = false;
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    public void telaMenuDados() throws Exception {
        boolean runningMenuDados = true;
    
        String op;

        while (runningMenuDados) {
            Usuario info = arquivoUsuario.readId(id);

            System.out.println("\n> Início > Meus Dados");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println(info.toString());
            System.out.println("(A) Atualizar dados");
            System.out.println("(D) Deletar conta");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            op = sc.nextLine();

            switch (op.toUpperCase()) {
                case "A":
                    Usuario usuarioAtual = arquivoUsuario.readId(id);

                    if (usuarioAtual != null && usuarioAtual.getAtivo()) {
                        System.out.print("Novo nome (enter para manter): ");
                        String novoNome = sc.nextLine().trim();
                        if (!novoNome.isEmpty()) {
                            usuarioAtual.setNome(novoNome);
                        }

                        System.out.print("Nova senha (enter para manter): ");
                        String novaSenha = sc.nextLine().trim();
                        if (!novaSenha.isEmpty()) {
                            usuarioAtual.setHashSenha(HashUtil.gerarHash(novaSenha));
                        }

                        System.out.print("Nova pergunta secreta (enter para manter): ");
                        String novaPergunta = sc.nextLine().trim();
                        if (!novaPergunta.isEmpty()) {
                            usuarioAtual.setPerguntaSecreta(novaPergunta);
                        }

                        System.out.print("Nova resposta secreta (enter para manter): ");
                        String novaResposta = sc.nextLine().trim();
                        if (!novaResposta.isEmpty()) {
                            usuarioAtual.setRespostaSecreta(novaResposta);
                        }

                        if (arquivoUsuario.update(usuarioAtual)) {
                            System.out.println("\nUsuário atualizado com sucesso!");
                        } else {
                            System.out.println("\nFalha ao atualizar usuário!");
                        }
                    } else {
                        System.out.println("Usuário não encontrado ou inativo.");
                    }
                    break;
                case "D":
                    System.out.print("Deseja excluir permanentemente sua conta? Essa ação é irreversível. Digite (S) para aceitar e qualquer outra tecla para recusar: ");
                    String comando = (sc.nextLine().trim());

                    if (comando.toUpperCase().equals("S")) {
                        Usuario usuarioExcluir = arquivoUsuario.readId(id);

                        if (usuarioExcluir != null && usuarioExcluir.getAtivo()) {
                            if (arquivoUsuario.delete(usuarioExcluir.getID())) {
                                System.out.println("\nUsuário excluído com sucesso!");
                                runningMenuDados = false;
                            } else {
                                System.out.println("\nFalha ao excluir usuário!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou já inativo.");
                        }
                    } else {
                        System.out.println("\nOperação cancelada.");
                    }

                    break;
                case "S":
                    runningMenuDados = false;
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }

    public void telaMenuUsuario() throws Exception {
        boolean runningMenuUsuario = true;
    
        String op;

        while (runningMenuUsuario) {
            if (arquivoUsuario.readId(id) == null) {
                return;
            } // testa se conta foi excluida


            System.out.println("\n> Início");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println("(D) Meus dados");
            System.out.println("(L) Minhas listas");
            System.out.println("(P) Produtos");
            System.out.println("(B) Buscar lista de outro usuário");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            op = sc.nextLine();

            switch (op.toUpperCase()) {
                case "D":
                    telaMenuDados();
                    Usuario dados = arquivoUsuario.read(id);
                    System.out.println(dados.toString() + "\n");
                    break;
                case "L":
                    telaMenuLista();
                    break;
                case "P":
                    telaMenuProduto();
                    break;
                case "B":
                    System.out.print("Entre com o código da lista para consultar: ");
                    String codigo;
                    codigo = sc.nextLine().trim();
                    do {
                        System.out.println("ERRO! Código compartilhável deve ter exatamente 10 caracteres. ");
                        System.out.print("\nEntre com o código da lista para consultar: ");
                        codigo = sc.nextLine().trim();
                    } while ( codigo.length() != 10 );
                    
                    try {
                        Lista lista = lista_opcao_b(codigo);
                        if ( lista != null ) {
                            System.out.println("Lista encontrada com sucesso: ");
                            
                            Usuario dono = arquivoUsuario.readId(lista.getUID());
                            if ( dono == null ) {
                                System.out.println("Erro do sistema ao encontrar usuário dono da lista ou usuário deletado. ");
                            } else {
                                System.out.println("Dono da lista: " + dono.getNome());
                            }
                            System.out.println(lista.toString());

                        } else {
                            System.out.println("Lista não encontrada.");
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao encontrar lista compartilhada. ");
                    }
                    break;
                case "S":
                    runningMenuUsuario = false;
                    break;
                default:
                    System.out.println("Opção Inválida!");
                    break;
            }
        }
    }
}
