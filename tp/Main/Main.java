import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static char telaInicial(){
        char input = ' ';
        Scanner sc = new Scanner(System.in);

        System.out.println("PresenteFácil 1.0\n-----------------\n");
        System.out.println("(1) Login");
        System.out.println("(2) Novo usuário\n");
        System.out.println("(S) sair\n");
        System.out.print("Opcao: ");

        input = sc.next().charAt(0);
        return input;
    }
    
    public static void limparConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            // Fallback: imprime várias linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    public static void main(String[] args) {
        char input = ' ';
        boolean resp;

        do {
            input = telaInicial();

            limparConsole();
            
            if(input=='1'){
                resp = new Acesso().telaLogin();
            }
            else if(input=='2') new Acesso().telaSignup();
            else System.exit(0);
        } while ( input != 'S' );

        
        /* 
        Usuario u = new Usuario();
        System.out.println(u);
        FileOutputStream arq;
        try{
            arq = new FileOutputStream("dados/user.db");
        }catch(Exception e){
            e.printStackTrace();
        }
        */

    }
}
