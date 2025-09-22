package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuUsuario {

    private static int id;
    private final ArquivoUsuario arquivoUsuario;
    private final ArquivoLista arquivoLista;
    Lista[] usuarioListas;

    public MenuUsuario(int id) throws Exception {
        this.id = id;
        arquivoUsuario = new ArquivoUsuario();
        arquivoLista = new ArquivoLista();

        //System.err.println("Erro de arquivoUsuario");
        usuarioListas = arquivoLista.carregaListas(id);
    }

    //Carregar listas do usuário para memória principal        
    public Lista opcao_c() throws Exception {

        Scanner sc = new Scanner(System.in);

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

    public void opcao_r(Lista lista) throws Exception {

        try {
            System.out.println(lista.toString());
            //MenuUsuario menu = new MenuUsuario();
//O tamanho do registro é 117 bytes
//             short tam = 117;
// //É definida a quantidade de registros do arquivo
//             long regs = arquivoLista.length()/tam;
// //Lê o nome da lista a ser buscada
//             Scanner sc = new Scanner(System.in);
//             String nome;
//             nome=sc.nextLine();
// //Printa a(s) lista(s) com nome igual ao buscado
//             for(long i=0;i<regs;i++){
//                 if(nome.equals())
//                 Lista[] listas  = arquivoLista.read();
//                 i=i*tam;
//             }

//             // short tam = 117;
//             // long regs = arquivoLista.length/tam;
//             // for(long i = 0; i<regs;i++){
//             //     arquivoLista.read(id);
//             // }
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

    // public static Lista opcao_u(){
    //     Lista lista=null;
    //     Scanner sc = new Scanner(System.in);
    //     System.out.print("Escolha a lista a ser atualizada: ");
    //     long pos = sc.nextLong();
    //     //Lista lista = readPos(pos);
    //     System.out.print("Novo nome da lista (aperte enter para manter): ");
    //     String nome = sc.nextLine();
    //     System.out.print("Nova descrição da lista (aperte enter para manter): ");
    //     String descricao = sc.nextLine();
    //     System.out.print("Nova data Limite: ");
    //     String limite = sc.nextLine();
    //     sc.close();
    //     return lista;
    // }
    private static void mostrarListas(Lista[] listas) {
        System.out.println("\n=== Listas cadastradas ===");
        for (int i = 0; i < listas.length; i++) {
            System.out.println((i + 1) + " - " + listas[i].getNome() + " (ID: " + listas[i].getID() + ")");
        }
    }

    public boolean update(Lista lista) throws Exception {
        return arquivoLista.update(lista);
    }

    public static void Delete(int idUsuario) throws Exception {
        ArquivoLista arqLista = new ArquivoLista();

        Lista[] listas = arqLista.carregaListas(idUsuario);

        if (listas == null || listas.length == 0) {
            System.out.println("Nenhuma lista encontrada.");
            return;
        }

        //mostrarListas(listas);
        Scanner sc = new Scanner(System.in);
        int escolha = -1;

        while (true) {
            System.out.print("\nDigite o número da lista que deseja excluir: ");
            if (sc.hasNextInt()) {
                escolha = sc.nextInt();
                sc.nextLine();

                if (escolha >= 1 && escolha <= listas.length) {
                    break;
                } else {
                    System.out.println("Opção fora do intervalo. Tente novamente.");
                }
            } else {
                System.out.println("Entrada inválida. Digite apenas números.");
                sc.nextLine();
            }
        }

        // Pega lista a partir do array
        Lista lista = listas[escolha - 1];

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

    public void telaMenuUsuario() throws Exception {
        boolean runningMenuUsuario = true;

        ArquivoLista arqLista;
        try {
            arqLista = new ArquivoLista();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (runningMenuUsuario) {

            /*
            short i = 0;
            while(usuarioListas[i].getID() != -1){
                System.out.println("["+i+"] "+usuarioListas[i].getNome());
                i++;
            }*/
            usuarioListas = arquivoLista.carregaListas(id);

            System.out.println();

            Scanner sc = new Scanner(System.in);
            String op;
            // System.out.println("[1] Meus dados"); //read
            //System.out.println("[2] Minhas listas");
            //System.out.println("[3] Produtos");
            //System.out.println("[4] Buscar lista");

            //[1]
            //[2]
            //[3]
            int y = 0;
            int n = 0;
            while (y < usuarioListas.length) {
                if (usuarioListas[y] != null) {
                    int index = n++ + 1;
                    System.out.println("[" + index + "] " + usuarioListas[y].getNome());
                }
                y++;
            }

            System.out.println("\n> Início");
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println("(C) Criar lista");
            System.out.println("(R) Consultar lista");
            System.out.println("(U) Atualizar lista");
            System.out.println("(D) Apagar lista");
            System.out.println();
            System.out.println("(S) Sair");
            System.out.println();
            System.out.print("Opção: ");

            /*while (((op=sc.nextLine().charAt(0))!='B')&&((op=sc.nextLine().charAt(0))!='E')) {
                
            } */
            op = sc.nextLine();

            switch (op.toUpperCase()) {
                case "C":
                    // System.out.print("Nome: ");
                    // String nome = sc.nextLine();
                    // System.out.print("Descrição: ");
                    // String descricao = sc.nextLine();
                    // System.out.print("Data Limite: ");
                    // String limite = sc.nextLine();
                    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    // LocalDate date = LocalDate.parse(limite,formatter);
                    try {
                        Lista novaLista = opcao_c();
                        arqLista.create(novaLista, id);
                        System.out.println("\nLista criada com sucesso!");
                        System.out.println(novaLista.toString());
                    } catch (Exception e) {
                        System.err.println("\nErro no sistema ao criar lista");
                    }
                    break;
                case "R":
                    System.out.print("Escolha (por posição) uma lista para consultar: ");
                    int pos;

                    pos = sc.nextInt();
                    pos = pos - 1;

                    opcao_r(usuarioListas[pos]);
                    break;
                case "U":

                    try {

                        System.out.print("Escolha (por posição) uma lista para consultar: ");
                        int posicao = 0;
                        posicao = sc.nextInt();
                        posicao = posicao - 1;

                        Lista listaAntiga = usuarioListas[posicao];

                        Lista listaNova = opcao_c();

                        listaAntiga.setNome(listaNova.getNome());
                        listaAntiga.setDescricao(listaNova.getDescricao());
                        listaAntiga.setLimite(listaNova.getLimite());

                        if (arquivoLista.update(listaAntiga)) {
                            System.out.println("Lista alterada com sucesso");
                        } else {
                            System.out.println("Erro ao alterar a lista");

                        }

                    } catch (Exception e) {
                    }

                    break;
                case "D":
                    Delete(id);
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
