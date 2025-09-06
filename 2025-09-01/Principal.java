import java.util.Scanner;
import model.*;

public class Principal {

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
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

            String opcao = sc.nextLine().trim();

            try {
                switch (opcao.toUpperCase()) {

                    case "1": // Login
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

                        Usuario novoUsuario = new Usuario(nome, email, senha, pergunta, resposta, true);
                        arqUsuarios.create(novoUsuario);
                        System.out.println("\nUsuário criado com sucesso!");
                        break;

                    case "3": // Atualizar usuário
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
                        break;

                    case "4": // Excluir usuário
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
                        break;

                    case "5": // Listar usuários
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
