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

    public void login(){
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