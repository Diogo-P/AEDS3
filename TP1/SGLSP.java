import java.util.Scanner;
public class SGLSP{

    public static void telaMenu(){
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
    }

    public static void loadLists(User u){
        for(int i=0;i<u){

        }
    }

    public static void start(User u){
        System.out.println("PresenteFácil 1.0\r\n" + //
                        "-----------------\r\n" + //
                        "> Início > Minhas listas \r\n" + //
                        "");
        System.out.println("LISTAS");
        loadLists(u);
    }

    public static void login(){
        Scanner sc = new Scanner(System.in);
        String login;
        String password;
        System.out.println("Login: ");
        login =  sc.nextLine();
        User u = search(login);
        if(u.getId()>-1){
            System.out.println("Senha: ");
            password =  sc.nextLine();
            if(hashSenha(password)==u.getHashSenha()){
                start(u);
            }
        }
    }


     public static int hashSenha(String password)
    { 

        

    }





    public static User signup()    // Ainda tá faltando algumas funcionalidades
    { 
        Scanner sc = new Scanner(System.in);

        User u =new User();
        

        System.out.println("Digite o seu Nome ");
        String nome=sc.nextLine();
        u.setNome(nome);
      
        System.out.println("Digite o seu E-mail ");
        String email=sc.nextLine();
        u.setEmail(email);

        System.out.println("Digite a sua Senha ");
        String senha=sc.nextLine();
       

        System.out.println("Confirme a sua Senha ");
        String senha2=sc.nextLine();
        

       System.out.println("Escolha uma Pergunta Secreta ");
       System.out.println("(1) Qual é o nome do seu primeiro animal de estimação?");
       System.out.println("(2) Qual é a cidade onde você nasceu?");
       System.out.println("(3) Qual é o nome da sua mãe?");
       System.out.println("(4) Qual é a sua comida favorita?");
       System.out.println("(5) Qual foi o nome da sua primeira escola?");

       int IdPergunta = sc.nextInt();

       switch (IdPergunta) {
        case 1:
         u.setPerguntaSecreta("Qual e o nome do seu primeiro animal de estimacao?");
        break;
        case 2:
         u.setPerguntaSecreta("Qual e a cidade onde voce nasceu?");
        break;
        case 3:
         u.setPerguntaSecreta("Qual e o nome da sua mae?");
        break;
        case 4:
         u.setPerguntaSecreta("Qual e a sua comida favorita?");
        break;
        case 5:
         u.setPerguntaSecreta("Qual foi o nome da sua primeira escola?");
        break;
        default:
         System.out.println("Opção inválida! Escolha entre 1 e 5.");
        } 
        
        System.out.println("Digite a sua Resposta ");
        String resposta=sc.nextLine(); 
        u.setRespostaSecreta(resposta);

        checkSignup(u, senha, senha2); // Para checar se os Dados digitados são válidos

    }

    public static User checkSignup(User u,String senha1,String senha2)  // Incompleto 
    { 
        if(senha1.compareTo(senha2)==0)
        { 
             u.setHashSenha(hashSenha(senha1));   

        }


        
    }

    public static void menu(char opt){
        if(opt=='1') login();
        else if(opt=='2') signup();
        else if(opt=='S') exit();
        else System.err.println("Opção inválida!");
    }
    public static void main(String[] args){
//Lê-se uma opção em forma de caracter
        char opt;
        Scanner sc = new Scanner(System.in);
        telaMenu();
        opt = sc.next().charAt(0);
//O menu procede de acordo com a opção
        menu(opt);
    }
}