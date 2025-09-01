import java.util.Scanner;

class Acesso {
    private static final Scanner sc = new Scanner(System.in);

    public static void telaMenu() {
        System.out.println("PresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("(1) Login");
        System.out.println("(2) Novo usuário");
        System.out.println();
        System.out.println("(S) Sair");
        System.out.println();
        System.out.print("Opção: _");
    }

    public static User search(String login){
        //Aqui começa a parte de busca na memória secundária
        return null; // MUDAR,TÁ AQUI SÓ PRA N FICAR DANDO ERRO
    }

    public static void loadLists(User u){
        //for(int i=0;i<u){ }
    }

    public static void start(User u) {
        System.out.println("PresenteFácil 1.0\r\n" + //
                "-----------------\r\n" + //
                "> Início > Minhas listas \r\n" + //
                "");
        System.out.println("LISTAS");
        loadLists(u);
    }

    public static void login() {
        System.out.print("Login: ");
        String login = sc.nextLine();
        User u = search(login);
        if (u.getId() > -1) {
            System.out.print("Senha: ");
            String password = sc.nextLine();
            if (hashSenha(password) == u.getHashSenha()) {
                start(u);
            } else {
                System.out.println("Senha incorreta.");
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    public static int hashSenha(String password) {
        return password.hashCode();
    }

    public static User signup() {
        User u = new User();

        System.out.print("Digite o seu Nome: ");
        u.setNome(sc.nextLine());

        System.out.print("Digite o seu E-mail: ");
        u.setEmail(sc.nextLine());

        System.out.print("Digite a sua Senha: ");
        String senha = sc.nextLine();

        System.out.print("Confirme a sua Senha: ");
        String senha2 = sc.nextLine();

        System.out.println("Escolha uma Pergunta Secreta:");
        System.out.println("(1) Qual é o nome do seu primeiro animal de estimação?");
        System.out.println("(2) Qual é a cidade onde você nasceu?");
        System.out.println("(3) Qual é o nome da sua mãe?");
        System.out.println("(4) Qual é a sua comida favorita?");
        System.out.println("(5) Qual foi o nome da sua primeira escola?");
        int IdPergunta = Integer.parseInt(sc.nextLine());

        switch (IdPergunta) {
            case 1:
                u.setPerguntaSecreta("Qual é o nome do seu primeiro animal de estimação?");
                break;
            case 2:
                u.setPerguntaSecreta("Qual é a cidade onde você nasceu?");
                break;
            case 3:
                u.setPerguntaSecreta("Qual é o nome da sua mãe?");
                break;
            case 4:
                u.setPerguntaSecreta("Qual é a sua comida favorita?");
                break;
            case 5:
                u.setPerguntaSecreta("Qual foi o nome da sua primeira escola?");
                break;
            default:
                System.out.println("Opção inválida! Escolha entre 1 e 5.");
                return null;
        }

        System.out.print("Digite a sua Resposta: ");
        u.setRespostaSecreta(sc.nextLine());

        User validUser = checkSignup(u, senha, senha2);
        if (validUser != null) {
            System.out.println("Usuário cadastrado com sucesso!");
        }
        return validUser;
    }

    public static User checkSignup(User u, String senha1, String senha2) {
        while (!senha1.equals(senha2)) {
            System.out.println("As Senhas não batem, digite elas novamente.");
            System.out.print("Digite a sua Senha: ");
            senha1 = sc.nextLine();
            System.out.print("Confirme a sua Senha: ");
            senha2 = sc.nextLine();
        }
        u.setHashSenha(hashSenha(senha1));
        return u;
    }

    public static void menu(char opt) {
        if (opt == '1') login();
        else if (opt == '2') signup();
        else if (opt == 'S' || opt == 's') exit();
        else System.err.println("Opção inválida!");
    }

    public static void exit() {
      
    }

    public static void main(String[] args) {
        char opt;
        telaMenu();
        opt = sc.next().charAt(0);
        menu(opt);
    }
}
