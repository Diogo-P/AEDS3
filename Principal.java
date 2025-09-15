import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.*;
import model.Lista;
import model.Usuario;

public class Principal {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        ListaArquivo listaArq;
        ArquivoUsuario arqUsuarios;
        try {
            listaArq = new ListaArquivo();
            arqUsuarios = new ArquivoUsuario();
        } catch (Exception e) {
            e.printStackTrace();
            sc.close();
            return;
        }
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
            // System.out.println("(N) Nova lista a partir de ID de usuário");
            // System.out.println("(M) Mostrar listas de um ID de usuário");
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

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
                                System.out.println("PresenteFácil 1.0");
                                System.out.println("-----------------");
                                System.out.println("> Início > Minhas listas ");
                                System.out.println();
                                System.out.println("(C) Criar lista");
                                System.out.println("(R) Ler e exibir listas");
                               
                                System.out.println("(U) Atualizar lista");
                                System.out.println("(D) Deletar lista");
                                System.out.println();
                                System.out.println("(B) Volta");
                                System.out.println("(E) Sair");
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
                                opcao_usuario = sc.nextLine().trim();
//Criar nova lista
                                switch(opcao_usuario.toUpperCase()){
                                    case "C":
                                         try {
                                            boolean usuarioEncontrado = false;
                                            String nomeU;
                                            String nomeLista;
                                            int uid=0;

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
                                            do { 
                                                System.out.println("\nNome da lista: ");
                                                nomeLista = sc.nextLine().trim();
                                                if (nomeLista.compareTo("") == 0 || nomeLista.length() < 4) {
                                                    System.err.println("Lista não pode ter nome vazio ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos");
                                                }
                                            } while (nomeLista.compareTo("") == 0 || nomeLista.length() < 4);
//Solicita uma descrição da lista até receber uma válida como entrada
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
//Solicita uma data de lista até receber uma válida como entrada
                                            do { 
                                                try {
                                                    System.out.print("\nData limite da lista: (FORMATO DD/MM/AAAA, números sem décimos devem ter 0x): ");
                                                    limite = LocalDate.parse(sc.nextLine().trim(),formatador);
                                                    dataValida = true;
                                                } catch (Exception e) {
                                                    System.err.println("Data limite da lista deve ser no formato DD/MM/AAAA.");
                                                }
                                            } while (!dataValida);
//Constroi um objeto lista a partir dos dados coletados
                                            try {
                                                Lista novaLista = new Lista(-1,nomeLista,descricao,criacao,limite,"",-1);
                                                novaLista.setCodigo();
                                                listaArq.create(novaLista, uid);
                                                System.out.println("\nSucesso na criação da lista do usuario de nome " + nomeLista + "!");
                                                System.out.println(novaLista.toString());
                                            } catch (Exception e) {
                                                System.err.println("\nErro na criação da lista!");
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e) {
                                            System.err.println("Erro durante coleta de informações de usuário, lista ou criação da lista: " + e.getMessage());
                                        }
                                    break;
//Exibir listas
                                    case "R":
                                        try {
                                            System.out.println("(R1) Ler e exibir minhas listas");
                                            System.out.println("(R2) Acessar lista de outrem");
                                            System.out.println("");
                                            System.out.print("Opcão:");
                                
                                            String codigo;
                                            do { 
                                                System.out.println("\nDigite codigo da lista (10 caracteres apenas): ");
                                                codigo = sc.nextLine().trim();
                                                if (codigo.length() != 10) {
                                                    System.err.println("Lista não pode ter código com menos de 10 caracteres efetivos");
                                                }
                                            } while (codigo.length() != 10);
                                            
                                            try {
                                                Lista queryLista = listaArq.read(codigo);
                                                if (queryLista != null) {
                                                    System.out.println("\nLista encontrada!\n");
                                                    System.out.println(queryLista.toString());
                                                } else {
                                                    System.out.println("\nLista não encontrada.");
                                                }
                                            } catch (Exception e) {
                                                System.err.println("\nErro no sistema.");
                                            }
                                        } catch (Exception e) {
                                            System.err.println("Erro durante coleta de informações de lista ou criação da lista: " + e.getMessage());
                                        }
                                    break;
//Atualizar listas
                                    case "U":
                                         try {
                                            System.out.println("\nEntre com o nome da lista que deseja alterar: ");
                                            String nomeLista = sc.nextLine().trim();

                                            boolean listaEncontrada = false;
                                            Lista antiga = listaArq.read(nomeLista);
                                            //Lista antiga = listaArq.read(codigo);

                                            System.out.println("Atributos da lista a ser mudada: ");
                                            System.out.println(antiga.toString());

                                            do {
                                                if ( antiga != null ) {
                                                    listaEncontrada = true;
                                                    break;
                                                }
                                                System.out.println("\nLista não encontrada.");
                                                System.out.println("\nEntre com o nome da lista que deseja alterar: ");
                                                nomeLista = sc.nextLine().trim();
                                                antiga = listaArq.read(nomeLista);
                                            } while ( !listaEncontrada );
                                            String novoNome;
                                            do { 
                                                System.out.println("\nNovo nome da lista: (Enter para manter)");
                                                novoNome = sc.nextLine().trim();
                                                if (novoNome.isEmpty()) {
                                                    break;
                                                }                            
                                                else if (novoNome.length() < 4) {
                                                    System.err.println("Lista não pode ter nome vazio ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos");
                                                } 
                                            } while (novoNome.length() < 4);

                                            String descricao;
                                            do { 
                                                System.out.println("\nNova descrição da lista: (Enter para manter)");
                                                descricao = sc.nextLine().trim();
                                                if (descricao.isEmpty()) {
                                                    break;
                                                }                            
                                                else if (descricao.length() < 4) {
                                                    System.err.println("Lista não pode ter descrição vazia ou ter apenas caracteres de espaço ou ter menos de 4 caracteres efetivos");
                                                } 
                                            } while (descricao.length() < 4);

                                            boolean dataValida = false;
                                            LocalDate limite = LocalDate.now();
                                            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                            do { 
                                                try {
                                                    System.out.print("\nData limite da nova lista: (FORMATO DD/MM/AAAA, números sem décimos devem ter 0x) (Enter para manter) ");
                                                    String input = sc.nextLine().trim();
                                                    if ( input.isEmpty() ) { break; }
                                                    limite = LocalDate.parse(input,formatador);
                                                    dataValida = true;
                                                } catch (Exception e) {
                                                    System.err.println("Data limite da lista deve ser no formato DD/MM/AAAA.");
                                                }
                                            } while (!dataValida);
                                            
                                            try {
                                                Lista novaLista = new Lista(antiga.getID(),novoNome,descricao,antiga.getCriacao(),limite,antiga.getCodigo(),antiga.getUID());
                                                listaArq.update(novaLista);
                                                System.out.println("\nSucesso na alteração da lista!");
                                                System.out.println(novaLista.toString());
                                            } catch (Exception e) {
                                                System.err.println("\nErro na alteração da lista!");
                                                e.printStackTrace();
                                            }
                                        } catch (Exception e) {
                                            System.err.println("Erro durante coleta de informações de lista ou alteração da lista: " + e.getMessage());
                                        }
                                    break;
//Apagar Lista
                                    case "D":
                                        try {
                                            System.out.println("\nEntre com o nome da lista que deseja deletar: ");
                                            String nomeLista = sc.nextLine().trim();

                                            Lista listaDeletada = listaArq.read(nomeLista);

                                            String resp;
                                            do { 
                                                System.out.println("\nLista encontrada. Deseja prosseguir com remoção? (S/N)");
                                                resp = sc.nextLine().trim().toUpperCase();
                                                if ( resp.compareTo("S") == 0 || resp.compareTo("N") == 0) { break; }
                                                System.out.println("\nOpção incorreta, prosseguir com remoção? (S/N)");
                                            } while ( resp.compareTo("S") != 0 || resp.compareTo("N") != 0 );

                                            if ( resp.compareTo("S") == 0 ) {
                                                if ( listaArq.delete(listaDeletada) ) {
                                                    System.out.println("Lista deletada com sucesso!");
                                                } else {
                                                    System.err.println("Falha no sistema ao deletar lista");
                                                }
                                            }

                                        } catch (Exception e) {
                                            System.err.println("Erro remoção deletar listar: " + e.getMessage());
                                        }
                                    break;
//Voltar para o menu principal
                                    case "V":
                                        // try {
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
                                    break;
//Sair
                                    case "S":
                                        running = false;
                                        System.out.println("\nSaindo do PresenteFácil 1.0...");
                                    break;
                                }
                                
                                // System.out.println();
                                // System.out.println("LISTAS");
                                
                               // System.out.println("\nLogin realizado com sucesso!");
                               // System.out.println(usuarioLogin);
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
        }

        try {
            arqUsuarios.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
