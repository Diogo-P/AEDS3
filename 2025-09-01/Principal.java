
import java.io.File;
import java.util.Scanner;
import model.*;

public class Principal {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Apaga arquivos antigos (opcional)
        
        (new File("./dados/usuario.email.d.db")).delete();
        (new File("./dados/usuario.email.c.db")).delete();
        (new File("./dados/usuario.nome.db")).delete();
        (new File("./dados/usuario.db")).delete();
        (new File("./dados/usuario.c.db")).delete();
        (new File("./dados/usuario.d.db")).delete();
        

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
            System.out.println("(S) Sair");
            System.out.print("\nOpção: ");

            String opcao = sc.nextLine().trim();

            switch (opcao.toUpperCase()) {

                case "1": // Login
                    try {
                        System.out.print("Email: ");
                        String email = sc.nextLine().trim();
                        System.out.print("Senha (número): ");
                        int senha = Integer.parseInt(sc.nextLine().trim());

                        Usuario usuario = arqUsuarios.readEmail(email);
                        if (usuario != null && usuario.getHashSenha() == senha && usuario.getAtivo()) {
                            System.out.println("\nLogin realizado com sucesso!");
                            System.out.println(usuario);
                        } else {
                            System.out.println("\nEmail ou senha incorretos ou usuário inativo!");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro durante login: " + e.getMessage());
                    }
                    break;

                case "2": // Novo usuário
                    try {
                        System.out.print("Nome: ");
                        String nome = sc.nextLine().trim();
                        System.out.print("Email: ");
                        String email = sc.nextLine().trim();
                        System.out.print("Senha (número): ");
                        int senha = Integer.parseInt(sc.nextLine().trim());
                        System.out.print("Pergunta secreta: ");
                        String pergunta = sc.nextLine().trim();
                        System.out.print("Resposta secreta: ");
                        String resposta = sc.nextLine().trim();

                        Usuario novoUsuario = new Usuario(nome, email, senha, pergunta, resposta, true);
                        arqUsuarios.create(novoUsuario);

                        System.out.println("\nUsuário criado com sucesso!");
                    } catch (Exception e) {
                        System.out.println("Erro ao criar usuário: " + e.getMessage());
                    }
                    break;

                case "3": // Atualizar usuário
                    try {
                        System.out.print("Email do usuário a atualizar: ");
                        String email = sc.nextLine().trim();
                        Usuario usuario = arqUsuarios.readEmail(email);

                        if (usuario != null && usuario.getAtivo()) {
                            System.out.print("Novo nome (enter para manter): ");
                            String novoNome = sc.nextLine().trim();
                            if (!novoNome.isEmpty()) {
                                usuario.setNome(novoNome);
                            }

                            System.out.print("Nova senha (enter para manter): ");
                            String novaSenhaStr = sc.nextLine().trim();
                            if (!novaSenhaStr.isEmpty()) {
                                usuario.setHashSenha(Integer.parseInt(novaSenhaStr));
                            }

                            System.out.print("Nova pergunta secreta (enter para manter): ");
                            String novaPergunta = sc.nextLine().trim();
                            if (!novaPergunta.isEmpty()) {
                                usuario.setPerguntaSecreta(novaPergunta);
                            }

                            System.out.print("Nova resposta secreta (enter para manter): ");
                            String novaResposta = sc.nextLine().trim();
                            if (!novaResposta.isEmpty()) {
                                usuario.setRespostaSecreta(novaResposta);
                            }

                            if (arqUsuarios.update(usuario)) {
                                System.out.println("\nUsuário atualizado com sucesso!");
                            } else {
                                System.out.println("\nFalha ao atualizar usuário!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou inativo.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao atualizar usuário: " + e.getMessage());
                    }
                    break;

                case "4": // Excluir usuário
                    try {
                        System.out.print("Email do usuário a excluir: ");
                        String email = sc.nextLine().trim();
                        Usuario usuario = arqUsuarios.readEmail(email);

                        if (usuario != null && usuario.getAtivo()) {
                            if (arqUsuarios.delete(usuario.getID())) {
                                System.out.println("\nUsuário excluído com sucesso!");
                            } else {
                                System.out.println("\nFalha ao excluir usuário!");
                            }
                        } else {
                            System.out.println("Usuário não encontrado ou já inativo.");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao excluir usuário: " + e.getMessage());
                    }
                    break;

                case "5": // Listar usuários
                    System.out.println("\n(1) Buscar por email");
                    System.out.println("(2) Buscar por nome");
                    System.out.print("Opção: ");
                    String listOp = sc.nextLine().trim();

                    try {
                        if (listOp.equals("1")) {
                            System.out.print("Digite o email: ");
                            String emailBusca = sc.nextLine().trim();
                            Usuario usuario = arqUsuarios.readEmail(emailBusca);
                            if (usuario != null && usuario.getAtivo()) {
                                System.out.println("\nUsuário encontrado:");
                                System.out.println(usuario);
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
                    } catch (Exception e) {
                        System.out.println("Erro ao buscar usuários: " + e.getMessage());
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
        }

        try {
            arqUsuarios.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}
