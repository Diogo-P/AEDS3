
import java.util.Scanner;
import model.*;

public class Principal {

    private final ArquivoUsuario arq;

    public Principal() throws Exception {
        this.arq = new ArquivoUsuario();
    }

    public static Usuario telaLogin(ArquivoUsuario arqUsuarios) throws Exception {
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
                return null;
            }
        } else {
            System.out.println("\nUsuário não encontrado ou inativo!");
            return null;
        }

        return usuarioLogin;
    }

    public static Usuario telaCadastro() {
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

        return novoUsuario;
    }

    public static void telaAtualizar(ArquivoUsuario arqUsuarios) throws Exception {
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

    public static void telaExcluir(ArquivoUsuario arqUsuarios) throws Exception {
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

    public static void telaRecuperar(ArquivoUsuario arqUsuarios) throws Exception {
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
            // System.out.println("(5) Listar usuários");
            System.out.println("(5) Recuperar senha");
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

            String opcao = sc.nextLine().trim();

            try {
                switch (opcao.toUpperCase()) {

// Login
                    case "1": // Login
                        Usuario usuario = telaLogin(arqUsuarios);
                        if (usuario != null) {
                            MenuUsuario menu = new MenuUsuario(usuario.getID());
                            menu.telaMenuUsuario();
                        }
                        break;


                    /* 
                        System.out.print("Email: ");
                        String emailLogin = sc.nextLine().trim();
                        System.out.print("Senha: ");
                        String senhaLogin = sc.nextLine().trim();

                         Usuario usuarioLogin = arqUsuarios.readEmail(emailLogin);
                        if (usuarioLogin != null && usuarioLogin.getAtivo()) {
                             String hashDigitado = HashUtil.gerarHash(senhaLogin);
                             if (usuarioLogin.getHashSenha().equals(hashDigitado)) {
                              System.out.println("\nLogin realizado com sucesso!");
                               System.out.println(usuarioLogin);
                           } else {
                                System.out.println("\nEmail ou senha incorretos!");
                            }
                        } else {
                            System.out.println("\nUsuário não encontrado ou inativo!");
                         }
                        break;
                     */
// Cadastro
                    case "2": // Cadastro / Novo Usuário
                        Usuario novoUsuario = telaCadastro();
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

//Recuperar senha
                    case "5": // Recuperar senha
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
                    case "S": // Sair
                        running = false;
                        System.out.println("\nSaindo do PresenteFácil 1.0...");
                        break;

                    default:
                        System.out.println("\nOpção inválida!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro dentro do menu de operações: " + e.getMessage());
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
