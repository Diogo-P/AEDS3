import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String emailLogin = (sc.nextLine().trim());
        System.out.print("Senha: ");
        String senhaLogin = (sc.nextLine().trim());

        Usuario usuarioLogin = arqUsuarios.readEmail(emailLogin);
        if (usuarioLogin != null && usuarioLogin.getAtivo()) {
            String hashDigitado = HashUtil.gerarHash(senhaLogin);
            if (usuarioLogin.getHashSenha().equals(hashDigitado)) {
                System.out.println("\nLogin realizado com sucesso!");
            } else {
                System.out.println("\nEmail ou senha incorretos!");
                return null;
            }
        } else {
            System.out.println("\nUsuario nao encontrado ou inativo!");
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
            System.out.println("Usuario nao encontrado ou inativo.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArquivoUsuario arqUsuarios;
        Path caminhoDados = Paths.get("dados");

        if (!Files.exists(caminhoDados)) {
            try {
                Files.createDirectories(caminhoDados);
            } catch (Exception e) {
                e.printStackTrace();
                sc.close();
                return;
            }
        }

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
            System.out.println("       PresenteFacil 1.0       ");
            System.out.println("===============================");
            System.out.println("(1) Login");
            System.out.println("(2) Novo usuario");
            System.out.println("(3) Recuperar senha");
            System.out.println("(S) Sair");
            System.out.print("\nOpcao: ");

            String opcao = sc.nextLine().trim();

            try {
                switch (opcao.toUpperCase()) {

                    case "1": // Login
                        Usuario usuario = telaLogin(arqUsuarios);
                        if (usuario != null) {
                            MenuUsuario menu = new MenuUsuario(usuario.getID());
                            menu.telaMenuUsuario();
                        }
                        break;

                    case "2": // Cadastro / Novo Usuario
                        Usuario novoUsuario = telaCadastro();
                        arqUsuarios.create(novoUsuario);
                        System.out.println("\nUsuario criado com sucesso!");
                        break;

                    case "3": // Recuperar senha
                        telaRecuperar(arqUsuarios);
                        break;

                    case "S": // Sair
                        running = false;
                        System.out.println("\nSaindo do PresenteFacil 1.0...");
                        break;

                    default:
                        System.out.println("\nOpcao invalida!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro dentro do menu de operacoes: " + e.getMessage());
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
