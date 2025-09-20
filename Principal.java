<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
import java.util.Scanner;
import model.*;

public class Principal {

<<<<<<< HEAD
    private final ArquivoUsuario arq;

    
        public Principal()throws Exception{
            this.arq = new ArquivoUsuario();
        }

    public static Usuario telaLogin(ArquivoUsuario arqUsuarios)throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.print("Email: ");
        String emailLogin = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senhaLogin = sc.nextLine().trim();

        Usuario usuarioLogin = arqUsuarios.readEmail(emailLogin);
        if (usuarioLogin != null && usuarioLogin.getAtivo()) {
            String hashDigitado = HashUtil.gerarHash(senhaLogin);
            if (usuarioLogin.getHashSenha().equals(hashDigitado)) {
                System.out.println("\nLogin realizado com sucesso!");
                //System.out.println(usuarioLogin);
            } else {
                System.out.println("\nEmail ou senha incorretos!");
            }
        } else {
                System.out.println("\nUsuário não encontrado ou inativo!");
        }
        sc.close();
        return usuarioLogin;
    }
    
    public static Usuario telaCadastro(){
        String nome;
        String email;
        String senha;
        String pergunta;
        String resposta;

        Scanner sc = new Scanner(System.in);

        System.out.print("Nome: ");
        nome = sc.nextLine().trim();
        System.out.print("Email: ");
        email = sc.nextLine().trim();
        System.out.print("Senha: ");
        senha = sc.nextLine().trim();
        System.out.print("Pergunta secreta: ");
        pergunta = sc.nextLine().trim();
        System.out.print("Resposta secreta: ");
        resposta = sc.nextLine().trim();

        Usuario novoUsuario = new Usuario(nome, email, senha, pergunta, resposta, true);
        sc.close();
        return novoUsuario;
    }

    public static void telaAtualizar(ArquivoUsuario arqUsuarios)throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.print("Email do usuário a atualizar: ");
        String emailAtualizar = sc.nextLine().trim();
        Usuario usuarioAtual = arqUsuarios.readEmail(emailAtualizar);

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

            if (arqUsuarios.update(usuarioAtual)) {
                System.out.println("\nUsuário atualizado com sucesso!");
            } else {
                System.out.println("\nFalha ao atualizar usuário!");
            }
        } else {
            System.out.println("Usuário não encontrado ou inativo.");
        }               
    }

    public static void telaExcluir(ArquivoUsuario arqUsuarios)throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.print("Email do usuário a excluir: ");
        String emailExcluir = sc.nextLine().trim();
        Usuario usuarioExcluir = arqUsuarios.readEmail(emailExcluir);

        if (usuarioExcluir != null && usuarioExcluir.getAtivo()) {
            if (arqUsuarios.delete(usuarioExcluir.getID())) {
                System.out.println("\nUsuário excluído com sucesso!");
            } else {
                System.out.println("\nFalha ao excluir usuário!");
            }
        } else {
            System.out.println("Usuário não encontrado ou já inativo.");
        }
    }

    public static void telaListar(ArquivoUsuario arqUsuarios)throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.println("\n(1) Buscar por email");
        System.out.println("(2) Buscar por nome");
        System.out.print("Opção: ");
        String listOp = sc.nextLine().trim();

        if (listOp.equals("1")) {
            System.out.print("Digite o email: ");
            String emailBusca = sc.nextLine().trim();
            Usuario usuarioBusca = arqUsuarios.readEmail(emailBusca);
            if (usuarioBusca != null && usuarioBusca.getAtivo()) {
                System.out.println("\nUsuário encontrado:");
                System.out.println(usuarioBusca);
            } else {
                System.out.println("\nUsuário não encontrado ou inativo.");
            }
        } else if (listOp.equals("2")) {
            System.out.print("Digite o nome (ou parte dele): ");
            String nomeBusca = sc.nextLine().trim();
            Usuario[] usuarios = arqUsuarios.readNome(nomeBusca);
            if (usuarios.length > 0) {
                System.out.println("\nUsuários encontrados:");
                for (Usuario u : usuarios) {
                    if (u.getAtivo()) {
                        System.out.println(u);
                        System.out.println("-------------------------");
                    }
                }
            } else {
                System.out.println("\nNenhum usuário encontrado com esse nome.");
            }
        } else {
            System.out.println("\nOpção inválida!");
        }
    }

    public static void telaRecuperar(ArquivoUsuario arqUsuarios)throws Exception{
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o email: ");
        String emailRecuperar = sc.nextLine().trim();
        Usuario usuarioRec = arqUsuarios.readEmail(emailRecuperar);

        if (usuarioRec != null && usuarioRec.getAtivo()) {
            System.out.println("Pergunta secreta: " + usuarioRec.getPerguntaSecreta());
            System.out.print("Resposta: ");
            String respostaDigitada = sc.nextLine().trim();

            if (usuarioRec.getRespostaSecreta().equalsIgnoreCase(respostaDigitada)) {
                System.out.print("Digite a nova senha: ");
                String novaSenhaRec = sc.nextLine().trim();
                usuarioRec.setHashSenha(HashUtil.gerarHash(novaSenhaRec));

                if (arqUsuarios.update(usuarioRec)) {
                    System.out.println("\nSenha redefinida com sucesso!");
                } else {
                    System.out.println("\nErro ao atualizar a senha.");
                }
            } else {
                System.out.println("Resposta incorreta!");
            }
        } else {
            System.out.println("Usuário não encontrado ou inativo.");
        }
    }

   


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArquivoUsuario arqUsuarios;


       
        try {
=======
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
    ListaArquivo listaArq;
    ArquivoUsuario arqUsuarios;
    Usuario usuarioLogado = null; 
        try {
            listaArq = new ListaArquivo();
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
            arqUsuarios = new ArquivoUsuario();
        } catch (Exception e) {
            e.printStackTrace();
            sc.close();
            return;
        }
<<<<<<< HEAD

=======
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
//Menu principal
        boolean running = true;
        while (running) {
            System.out.println("\n===============================");
            System.out.println("       PresenteFácil 1.0       ");
            System.out.println("===============================");
            System.out.println("(1) Login");
            System.out.println("(2) Novo usuário");
            System.out.println("(3) Atualizar usuário");
            System.out.println("(4) Excluir usuário");
            System.out.println("(5) Listar usuários");
            System.out.println("(6) Recuperar senha");
<<<<<<< HEAD
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

            String opcao = sc.nextLine().trim();

            try {
                switch (opcao.toUpperCase()) {

//Login
                    case "1": // Login
                        Usuario usuario = telaLogin(arqUsuarios);
                        if(usuario!=null){
                            MenuUsuario menu = new MenuUsuario(usuario.getID());
                            menu.telaMenuUsuario();
                        }
                        // System.out.print("Email: ");
                        // String emailLogin = sc.nextLine().trim();
                        // System.out.print("Senha: ");
                        // String senhaLogin = sc.nextLine().trim();

                        // Usuario usuarioLogin = arqUsuarios.readEmail(emailLogin);
                        // if (usuarioLogin != null && usuarioLogin.getAtivo()) {
                        //     String hashDigitado = HashUtil.gerarHash(senhaLogin);
                        //     if (usuarioLogin.getHashSenha().equals(hashDigitado)) {
                        //         System.out.println("\nLogin realizado com sucesso!");
                        //         System.out.println(usuarioLogin);
                        //     } else {
                        //         System.out.println("\nEmail ou senha incorretos!");
                        //     }
                        // } else {
                        //     System.out.println("\nUsuário não encontrado ou inativo!");
                        // }
                        break;

//Cadastro
                    case "2": // Novo usuário
                        Usuario novoUsuario  = telaCadastro();
                        // System.out.print("Nome: ");
                        // String nome = sc.nextLine().trim();
                        // System.out.print("Email: ");
                        // String email = sc.nextLine().trim();
                        // System.out.print("Senha: ");
                        // String senha = sc.nextLine().trim();
                        // System.out.print("Pergunta secreta: ");
                        // String pergunta = sc.nextLine().trim();
                        // System.out.print("Resposta secreta: ");
                        // String resposta = sc.nextLine().trim();

                        // Usuario novoUsuario = new Usuario(nome, email, senha, pergunta, resposta, true);
                        arqUsuarios.create(novoUsuario);
                        System.out.println("\nUsuário criado com sucesso!");
                        break;
//Atualizar
                    case "3": // Atualizar usuário
                        telaAtualizar(arqUsuarios);
                        // System.out.print("Email do usuário a atualizar: ");
                        // String emailAtualizar = sc.nextLine().trim();
                        // Usuario usuarioAtual = arqUsuarios.readEmail(emailAtualizar);

                        // if (usuarioAtual != null && usuarioAtual.getAtivo()) {
                        //     System.out.print("Novo nome (enter para manter): ");
                        //     String novoNome = sc.nextLine().trim();
                        //     if (!novoNome.isEmpty()) {
                        //         usuarioAtual.setNome(novoNome);
                        //     }

                        //     System.out.print("Nova senha (enter para manter): ");
                        //     String novaSenha = sc.nextLine().trim();
                        //     if (!novaSenha.isEmpty()) {
                        //         usuarioAtual.setHashSenha(HashUtil.gerarHash(novaSenha));
                        //     }

                        //     System.out.print("Nova pergunta secreta (enter para manter): ");
                        //     String novaPergunta = sc.nextLine().trim();
                        //     if (!novaPergunta.isEmpty()) {
                        //         usuarioAtual.setPerguntaSecreta(novaPergunta);
                        //     }

                        //     System.out.print("Nova resposta secreta (enter para manter): ");
                        //     String novaResposta = sc.nextLine().trim();
                        //     if (!novaResposta.isEmpty()) {
                        //         usuarioAtual.setRespostaSecreta(novaResposta);
                        //     }

                        //     if (arqUsuarios.update(usuarioAtual)) {
                        //         System.out.println("\nUsuário atualizado com sucesso!");
                        //     } else {
                        //         System.out.println("\nFalha ao atualizar usuário!");
                        //     }
                        // } else {
                        //     System.out.println("Usuário não encontrado ou inativo.");
                        // }
                        break;

//Excluir usuário                        
                    case "4": // Excluir usuário
                        telaExcluir(arqUsuarios);
                        // System.out.print("Email do usuário a excluir: ");
                        // String emailExcluir = sc.nextLine().trim();
                        // Usuario usuarioExcluir = arqUsuarios.readEmail(emailExcluir);

                        // if (usuarioExcluir != null && usuarioExcluir.getAtivo()) {
                        //     if (arqUsuarios.delete(usuarioExcluir.getID())) {
                        //         System.out.println("\nUsuário excluído com sucesso!");
                        //     } else {
                        //         System.out.println("\nFalha ao excluir usuário!");
                        //     }
                        // } else {
                        //     System.out.println("Usuário não encontrado ou já inativo.");
                        // }
                        break;


//Listar usuários
                    case "5": // Listar usuários
                        telaListar(arqUsuarios);
                        // System.out.println("\n(1) Buscar por email");
                        // System.out.println("(2) Buscar por nome");
                        // System.out.print("Opção: ");
                        // String listOp = sc.nextLine().trim();

                        // if (listOp.equals("1")) {
                        //     System.out.print("Digite o email: ");
                        //     String emailBusca = sc.nextLine().trim();
                        //     Usuario usuarioBusca = arqUsuarios.readEmail(emailBusca);
                        //     if (usuarioBusca != null && usuarioBusca.getAtivo()) {
                        //         System.out.println("\nUsuário encontrado:");
                        //         System.out.println(usuarioBusca);
                        //     } else {
                        //         System.out.println("\nUsuário não encontrado ou inativo.");
                        //     }
                        // } else if (listOp.equals("2")) {
                        //     System.out.print("Digite o nome (ou parte dele): ");
                        //     String nomeBusca = sc.nextLine().trim();
                        //     Usuario[] usuarios = arqUsuarios.readNome(nomeBusca);
                        //     if (usuarios.length > 0) {
                        //         System.out.println("\nUsuários encontrados:");
                        //         for (Usuario u : usuarios) {
                        //             if (u.getAtivo()) {
                        //                 System.out.println(u);
                        //                 System.out.println("-------------------------");
                        //             }
                        //         }
                        //     } else {
                        //         System.out.println("\nNenhum usuário encontrado com esse nome.");
                        //     }
                        // } else {
                        //     System.out.println("\nOpção inválida!");
                        // }
                        break;
//Recuperar senha
                    case "6": // Recuperar senha
                        telaRecuperar(arqUsuarios);
                        // System.out.print("Digite o email: ");
                        // String emailRecuperar = sc.nextLine().trim();
                        // Usuario usuarioRec = arqUsuarios.readEmail(emailRecuperar);

                        // if (usuarioRec != null && usuarioRec.getAtivo()) {
                        //     System.out.println("Pergunta secreta: " + usuarioRec.getPerguntaSecreta());
                        //     System.out.print("Resposta: ");
                        //     String respostaDigitada = sc.nextLine().trim();

                        //     if (usuarioRec.getRespostaSecreta().equalsIgnoreCase(respostaDigitada)) {
                        //         System.out.print("Digite a nova senha: ");
                        //         String novaSenhaRec = sc.nextLine().trim();
                        //         usuarioRec.setHashSenha(HashUtil.gerarHash(novaSenhaRec));

                        //         if (arqUsuarios.update(usuarioRec)) {
                        //             System.out.println("\nSenha redefinida com sucesso!");
                        //         } else {
                        //             System.out.println("\nErro ao atualizar a senha.");
                        //         }
                        //     } else {
                        //         System.out.println("Resposta incorreta!");
                        //     }
                        // } else {
                        //     System.out.println("Usuário não encontrado ou inativo.");
                        // }
                        break;

//Sair                        
=======
            // System.out.println("(N) Nova lista a partir de ID de usuário");
            // System.out.println("(M) Mostrar listas de um ID de usuário");
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

//Lista as listas que o própio usuário possui
                                // try {
                                //     int uid=0;
                                //     Lista[] queryListas = listaArq.getAllLists(uid);
                                //     if (queryListas != null) {
                                //         System.out.println("AQ");
                                //         System.out.println("\nListas encontradas:\n");
                                //         for ( Lista lista : queryListas ) {
                                //             System.out.println(lista.toString());
                                //         }
                                //     } else {
                                //         System.out.println("\nLista não encontrada.");
                                //     }
                                // } catch (Exception e) {
                                //     System.err.println("\nErro no sistema.");
                                // }


            String opcao = sc.nextLine().trim();
            String opcao_usuario;
            try {
                switch (opcao.toUpperCase()) {

                    case "1": // Login
                        System.out.print("Email: ");
                        String emailLogin = sc.nextLine().trim();
                        System.out.print("Senha: ");
                        String senhaLogin = sc.nextLine().trim();

//Acessa usuário a partir do email de login
                        Usuario usuarioLogin = arqUsuarios.readEmail(emailLogin);
//Precisa estar ativo e não ser nulo
                        if (usuarioLogin != null && usuarioLogin.getAtivo()) {
//Confere se a hash da senha tentada é a mesma da referente ao login tentado
                            String hashDigitado = HashUtil.gerarHash(senhaLogin);
//Menu do usuário
                            if (usuarioLogin.getHashSenha().equals(hashDigitado)) {
                                // marca usuário como logado e entra no menu de listas até logout
                                usuarioLogado = usuarioLogin;
                                System.out.println("\nLogin realizado com sucesso! Entrando no menu de listas...");
                                while (usuarioLogado != null && !Thread.currentThread().isInterrupted()) {
                                    System.out.println("\n===============================");
                                    System.out.println("> Início > Minhas listas ");
                                    System.out.println("Usuário: " + usuarioLogado.getNome());
                                    System.out.println();
                                    System.out.println("(C) Criar lista");
                                    System.out.println("(R) Ler e exibir listas");
                                    System.out.println("(U) Atualizar lista");
                                    System.out.println("(D) Deletar lista");
                                    System.out.println("(L) Listar minhas listas");
                                    System.out.println("(E) Fazer logout");
                                    System.out.println("(S) Sair");
                                    System.out.print("\nOpção: ");

                                    String escolha = sc.nextLine().trim().toUpperCase();
                                    switch (escolha) {
                                        case "C":
                                            try {
                                                String nomeLista;
                                                do {
                                                    System.out.println("\nNome da lista: ");
                                                    nomeLista = sc.nextLine().trim();
                                                    if (nomeLista.compareTo("") == 0 || nomeLista.length() < 4) {
                                                        System.err.println("Lista não pode ter nome vazio ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos");
                                                    }
                                                } while (nomeLista.compareTo("") == 0 || nomeLista.length() < 4);

                                                String descricao;
                                                do {
                                                    System.out.println("\nDescrição da lista: ");
                                                    descricao = sc.nextLine().trim();
                                                    if (descricao.compareTo("") == 0 || descricao.length() < 4) {
                                                        System.err.println("Lista não pode ter descrição vazia ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos.");
                                                    }
                                                } while (descricao.compareTo("") == 0 || descricao.length() < 4);

                                                LocalDate criacao = LocalDate.now();
                                                boolean dataValida = false;
                                                LocalDate limite = LocalDate.now();
                                                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                                do {
                                                    try {
                                                        System.out.print("\nData limite da lista: (FORMATO DD/MM/AAAA): ");
                                                        limite = LocalDate.parse(sc.nextLine().trim(), formatador);
                                                        dataValida = true;
                                                    } catch (Exception e) {
                                                        System.err.println("Data limite da lista deve ser no formato DD/MM/AAAA.");
                                                    }
                                                } while (!dataValida);

                                                Lista novaLista = new Lista(-1, nomeLista, descricao, criacao, limite, "", usuarioLogado.getID());
                                                novaLista.setCodigo();
                                                listaArq.create(novaLista, usuarioLogado.getID());
                                                System.out.println("\nSucesso na criação da lista!");
                                                System.out.println(novaLista.toString());
                                            } catch (Exception e) {
                                                System.err.println("Erro na criação da lista: " + e.getMessage());
                                            }
                                            System.out.println("\nPressione ENTER para continuar...");
                                            sc.nextLine();
                                            break;


                                //             do {
                                //                 System.out.println("Entre com nome de Usuário: ");
                                //                 nomeU =sc.nextLine();
                                // //sc.nextLine();
                                //                 if ( arqUsuarios.readNome(nomeU) != null ) {
                                //                     usuarioEncontrado = true;
                                //                 } else {
                                //                      System.out.println("ID de usuário inexistente ou inativado.");
                                //                 }
                                //             } while ( !usuarioEncontrado );

//Solicita o nome da lista até receber um nome válido como entrada

                                        case "R":
                                            try {
                                                System.out.print("\nDigite codigo da lista (10 caracteres apenas): ");
                                                String codigo = sc.nextLine().trim();
                                                if (codigo.length() != 10) {
                                                    System.err.println("Lista não pode ter código com menos de 10 caracteres efetivos");
                                                    break;
                                                }
                                                Lista queryLista = listaArq.read(codigo);
                                                if (queryLista != null) {
                                                    System.out.println("\nLista encontrada!\n");
                                                    System.out.println(queryLista.toString());
                                                } else {
                                                    System.out.println("\nLista não encontrada.");
                                                }
                                            } catch (Exception e) {
                                                System.err.println("Erro ao ler lista: " + e.getMessage());
                                            }
                                            System.out.println("\nPressione ENTER para continuar...");
                                            sc.nextLine();
                                            break;

                                        case "U":
                                            try {
                                                System.out.print("\nDigite o código da lista que deseja alterar: ");
                                                String codigo = sc.nextLine().trim();
                                                Lista antiga = listaArq.read(codigo);
                                                if (antiga != null && antiga.getUID() == usuarioLogado.getID()) {
                                                    System.out.println("Atributos da lista a ser mudada:");
                                                    System.out.println(antiga.toString());
                                                    System.out.print("\nNovo nome da lista (Enter para manter): ");
                                                    String novoNome = sc.nextLine().trim();
                                                    if (novoNome.isEmpty()) novoNome = antiga.getNome();
                                                    System.out.print("\nNova descrição da lista (Enter para manter): ");
                                                    String descricao = sc.nextLine().trim();
                                                    if (descricao.isEmpty()) descricao = antiga.getDescricao();
                                                    System.out.print("\nNova data limite (dd/MM/yyyy) (Enter para manter): ");
                                                    String novaData = sc.nextLine().trim();
                                                    LocalDate limite = antiga.getLimite();
                                                    if (!novaData.isEmpty()) {
                                                        try {
                                                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                                            limite = LocalDate.parse(novaData, fmt);
                                                        } catch (Exception ex) {
                                                            System.err.println("Data inválida, mantendo a antiga.");
                                                        }
                                                    }
                                                    Lista novaLista = new Lista(antiga.getID(), novoNome, descricao, antiga.getCriacao(), limite, antiga.getCodigo(), antiga.getUID());
                                                    if (listaArq.update(novaLista)) {
                                                        System.out.println("\nSucesso na alteração da lista!");
                                                        System.out.println(novaLista.toString());
                                                    } else {
                                                        System.err.println("Erro ao atualizar a lista!");
                                                    }
                                                } else {
                                                    System.out.println("\nLista não encontrada ou você não tem permissão para alterá-la.");
                                                }
                                            } catch (Exception e) {
                                                System.err.println("Erro na atualização: " + e.getMessage());
                                            }
                                            System.out.println("\nPressione ENTER para continuar...");
                                            sc.nextLine();
                                            break;

                                        case "D":
                                            try {
                                                System.out.print("\nDigite o código da lista que deseja deletar: ");
                                                String codigo = sc.nextLine().trim();
                                                Lista listaDeletar = listaArq.read(codigo);
                                                if (listaDeletar != null && listaDeletar.getUID() == usuarioLogado.getID()) {
                                                    System.out.println(listaDeletar.toString());
                                                    System.out.print("\nConfirmar exclusão? (S/N): ");
                                                    if (sc.nextLine().trim().toUpperCase().equals("S")) {
                                                        if (listaArq.delete(listaDeletar)) {
                                                            System.out.println("\nLista deletada com sucesso!");
                                                        } else {
                                                            System.err.println("Erro ao deletar lista.");
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("\nLista não encontrada ou você não tem permissão para deletá-la.");
                                                }
                                            } catch (Exception e) {
                                                System.err.println("Erro ao deletar: " + e.getMessage());
                                            }
                                            System.out.println("\nPressione ENTER para continuar...");
                                            sc.nextLine();
                                            break;

                                        case "L":
                                            try {
                                                Lista[] minhasListas = listaArq.getAllLists(usuarioLogado.getID());
                                                if (minhasListas != null && minhasListas.length > 0) {
                                                    System.out.println("\nSuas listas:\n");
                                                    for (Lista l : minhasListas) {
                                                        System.out.println(l.toString());
                                                        System.out.println("-------------------------");
                                                    }
                                                } else {
                                                    System.out.println("\nVocê ainda não tem nenhuma lista.");
                                                }
                                            } catch (Exception e) {
                                                System.err.println("Erro ao listar: " + e.getMessage());
                                            }
                                            System.out.println("\nPressione ENTER para continuar...");
                                            sc.nextLine();
                                            break;

                                        case "E":
                                            // logout
                                            usuarioLogado = null;
                                            System.out.println("\nLogout realizado. Voltando ao menu principal.");
                                            break;

                                        case "S":
                                            // sair do programa
                                            System.out.println("\nSaindo do PresenteFácil 1.0...");
                                            System.exit(0);
                                            break;

                                        default:
                                            System.out.println("\nOpção inválida!");
                                            break;

                                            /* Case V:
                                             * // try {
                                        //     int uid=0;
                                        //     boolean usuarioEncontrado = false;
                                        //     String nomeLista;
                                        //     do {
                                        //         System.out.println("Entre com nome de Lista: ");
                                        //         nomeLista = sc.nextLine();
                                        //         sc.nextLine();
                                        //         if ( arqUsuarios.readId(uid) != null ) {
                                        //             usuarioEncontrado = true;
                                        //         } else {
                                        //             System.out.println("Nome de usuário inexistente ou inativado.");
                                        //         }
                                        //     } while ( !usuarioEncontrado );
                            
                                        //     // try {
                                        //     //     int uid=0;
                                        //     //     Lista[] queryListas = listaArq.getAllLists(uid);
                                        //     //     if (queryListas != null) {
                                        //     //         System.out.println("\nListas encontradas:\n");
                                        //     //         for ( Lista lista : queryListas ) {
                                        //     //             System.out.println(lista.toString());
                                        //     //         }
                                        //     //     } else {
                                        //     //         System.out.println("\nLista não encontrada.");
                                        //     //     }
                                        //     // } catch (Exception e) {
                                        //     //     System.err.println("\nErro no sistema.");
                                        //     // }
                                        // } catch (Exception e) {
                                        //     System.err.println("\nErro durante coleta de informações de lista ou criação da lista: " + e.getMessage());
                                        // }
                                             */
                                    }
                                }
                            } else {
                                System.out.println("\nEmail ou senha incorretos!");
                            }
                        } else {
                            System.out.println("\nUsuário não encontrado ou inativo!");
                        }
                        break;

                    case "2": // Novo usuário
                        System.out.print("Nome: ");
                        String nome = sc.nextLine().trim();
                        System.out.print("Email: ");
                        String email = sc.nextLine().trim();
                        System.out.print("Senha: ");
                        String senha = sc.nextLine().trim();
                        System.out.print("Pergunta secreta: ");
                        String pergunta = sc.nextLine().trim();
                        System.out.print("Resposta secreta: ");
                        String resposta = sc.nextLine().trim();

//Cria o usuário a ser armazenado                        
                        Usuario novoUsuario = new Usuario(nome, email, senha, pergunta, resposta, true);
//Cria o registro 
                        arqUsuarios.create(novoUsuario);
                        System.out.println("\nUsuário criado com sucesso!");
                        break;

                    case "3": // Atualizar usuário
                        System.out.print("Email do usuário a atualizar: ");
                        String emailAtualizar = sc.nextLine().trim();
//Acessa e armazena dados de registro de usuário na RAM 
                        Usuario usuarioAtual = arqUsuarios.readEmail(emailAtualizar);
//Opções de atualização de dados
                        if (usuarioAtual != null && usuarioAtual.getAtivo()) {
                            System.out.print("Novo nome (enter para manter): ");
                            String novoNome = sc.nextLine().trim();
                            if (!novoNome.isEmpty()) {
                                usuarioAtual.setNome(novoNome);
                            }
                            System.out.print("Nova senha (enter para manter): ");
                            String novaSenha = sc.nextLine().trim();
                            if (!novaSenha.isEmpty()) {
//Atualiza senha a partir da hash
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
//Após setar o novo faz o update partir dos dados coletados
                            if (arqUsuarios.update(usuarioAtual)) {
                                System.out.println("\nUsuário atualizado com sucesso!");
                            } else {
                                System.out.println("\nFalha ao atualizar usuário!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou inativo.");
                        }
                        break;

                   case "4": // Excluir usuário
                        System.out.print("Email do usuário a excluir: ");
                        String emailExcluir = sc.nextLine().trim();
//Capta usuário a ser excluído
                        Usuario usuarioExcluir = arqUsuarios.readEmail(emailExcluir);
                        if (usuarioExcluir != null && usuarioExcluir.getAtivo()) {
//Usa função delete do CRUD da classe Arquivo
                            if (arqUsuarios.delete(usuarioExcluir.getID())) {
                                System.out.println("\nUsuário excluído com sucesso!");
                            } else {
                                System.out.println("\nFalha ao excluir usuário!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou já inativo.");
                        }
                        break;

                    case "5": // Listar usuários
                        System.out.println("\n(1) Buscar por email");
                        System.out.println("(2) Buscar por nome");
                        System.out.print("Opção: ");
                        String listOp = sc.nextLine().trim();
//Lista todos os usuários com o email igual ao buscado
                        if (listOp.equals("1")) {
                            System.out.print("Digite o email: ");
                            String emailBusca = sc.nextLine().trim();
//Traz registros dos emails buscados por email para a memória de trabalho
                            Usuario usuarioBusca = arqUsuarios.readEmail(emailBusca);
                            if (usuarioBusca != null && usuarioBusca.getAtivo()) {
                                System.out.println("\nUsuário encontrado:");
                                System.out.println(usuarioBusca);
                            } else {
                                System.out.println("\nUsuário não encontrado ou inativo.");
                            }
//Lista todos os usuários com o nome igual ao buscado
                        } else if (listOp.equals("2")) {
                            System.out.print("Digite o nome (ou parte dele): ");
                            String nomeBusca = sc.nextLine().trim();
//Traz registros dos emails buscados por email para a memória de trabalho
                            Usuario[] usuarios = arqUsuarios.readNome(nomeBusca);
//Se a lista não estiver vazia ela será exibida
                            if (usuarios.length > 0) {
                                System.out.println("\nUsuários encontrados:");
                                for (Usuario u : usuarios) {
                                    if (u.getAtivo()) {
                                        System.out.println(u);
                                        System.out.println("-------------------------");
                                    }
                                }
                            } else {
                                System.out.println("\nNenhum usuário encontrado com esse nome.");
                            }
                        } else {
                            System.out.println("\nOpção inválida!");
                        }
                        break;

                    case "6": // Recuperar senha
                        System.out.print("Digite o email: ");
                        String emailRecuperar = sc.nextLine().trim();
                        Usuario usuarioRec = arqUsuarios.readEmail(emailRecuperar);

                        if (usuarioRec != null && usuarioRec.getAtivo()) {
                            System.out.println("Pergunta secreta: " + usuarioRec.getPerguntaSecreta());
                            System.out.print("Resposta: ");
                            String respostaDigitada = sc.nextLine().trim();

                            if (usuarioRec.getRespostaSecreta().equalsIgnoreCase(respostaDigitada)) {
                                System.out.print("Digite a nova senha: ");
                                String novaSenhaRec = sc.nextLine().trim();
                                usuarioRec.setHashSenha(HashUtil.gerarHash(novaSenhaRec));

                                if (arqUsuarios.update(usuarioRec)) {
                                    System.out.println("\nSenha redefinida com sucesso!");
                                } else {
                                    System.out.println("\nErro ao atualizar a senha.");
                                }
                            } else {
                                System.out.println("Resposta incorreta!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou inativo.");
                        }
                        break;

                     case "N":  // ESSA OPERAÇÃO NÃO PODE EXISTIR NA VERSÃO FINAL, LISTA É LINKADA AUTOMATICAMENTE COM USUARIO LOGADO
                        try {
                            boolean usuarioEncontrado = false;
                            String nomeU;
                            int uid=0;
                            do {
                                System.out.println("Entre com nome de Usuário: ");
                                nomeU =sc.nextLine();
                                //sc.nextLine();
                                if ( arqUsuarios.readNome(nomeU) != null ) {
                                    usuarioEncontrado = true;
                                } else {
                                    System.out.println("ID de usuário inexistente ou inativado.");
                                }
                            } while ( !usuarioEncontrado );

                            do { 
                                System.out.println("\nNome da lista: ");
                                nome = sc.nextLine().trim();
                                if (nome.compareTo("") == 0 || nome.length() < 4) {
                                    System.err.println("Lista não pode ter nome vazio ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos");
                                }
                            } while (nome.compareTo("") == 0 || nome.length() < 4);

                            String descricao;
                            do { 
                                System.out.println("\nDescrição da lista: ");
                                descricao = sc.nextLine().trim();
                                if (descricao.compareTo("") == 0 || descricao.length() < 4) {
                                    System.err.println("Lista não pode ter descrição vazia ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos.");
                                }
                            } while (descricao.compareTo("") == 0 || descricao.length() < 4);

                            LocalDate criacao = LocalDate.now();

                            boolean dataValida = false;
                            LocalDate limite = LocalDate.now();
                            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            do { 
                                try {
                                    System.out.print("\nData limite da lista: (FORMATO DD/MM/AAAA, números sem décimos devem ter 0x): ");
                                    limite = LocalDate.parse(sc.nextLine().trim(),formatador);
                                    dataValida = true;
                                } catch (Exception e) {
                                    System.err.println("Data limite da lista deve ser no formato DD/MM/AAAA.");
                                }
                            } while (!dataValida);
                            
                            try {
                                Lista novaLista = new Lista(-1,nome,descricao,criacao,limite,"",-1);
                                novaLista.setCodigo();
                                listaArq.create(novaLista, uid);
                                System.out.println("\nSucesso na criação da lista do usuario de nome " + nomeU + "!");
                                System.out.println(novaLista.toString());
                            } catch (Exception e) {
                                System.err.println("\nErro na criação da lista!");
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            System.err.println("Erro durante coleta de informações de usuário, lista ou criação da lista: " + e.getMessage());
                        }
                        break;

>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
                    case "S": // Sair
                        running = false;
                        System.out.println("\nSaindo do PresenteFácil 1.0...");
                        break;

                    default:
                        System.out.println("\nOpção inválida!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
            }
<<<<<<< HEAD
=======

             // System.out.println();
                                // System.out.println("LISTAS");
                                
                               // System.out.println("\nLogin realizado com sucesso!");
                               // System.out.println(usuarioLogin);
>>>>>>> f339579cdf2e06d7c0cfe1c0f18cec5118dfb798
        }

        try {
            arqUsuarios.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
