import java.util.Scanner;


class Acesso {

    public boolean buscaUsuario(String email){
        return true;
    }

    public boolean telaLogin(){
        boolean resp = false;

        Scanner sc = new Scanner(System.in);

        System.out.println("Login: ");
        System.out.println("Insira seu email: ");
        String input = sc.nextLine();
        boolean emailEncontrado = buscaUsuario(input);

        while (!emailEncontrado) {
            System.out.println("Email inexistente! Tente denovo ou digite (S) para voltar");
            input = sc.nextLine();
            if (input.compareTo("S") == 0) {
                break;
            }
            emailEncontrado = buscaUsuario(input);
        }

        if ( emailEncontrado ) {
            System.out.println("Insira sua senha: ");
            String senha = sc.nextLine();
            resp = login(input,senha);
            sc.close();
        }
        
        
        return resp;
    }

    public void telaSignup(){
        Scanner sc = new Scanner(System.in);
        Usuario novoUsuario = new Usuario();

        System.out.print("Nome: ");
        String input = sc.nextLine();
        novoUsuario.nome = input;

        System.out.print("Email: ");
        input = sc.nextLine();
        novoUsuario.email = input;

        System.out.print("Criar senha: ");
        input = sc.nextLine();
        System.out.print("Confirmar senha: ");
        String confirmaSenha = sc.nextLine();

        if(input.equals(confirmaSenha)) {
            //novoUsuario.hashSenha = transformarSenha(input);
        }

        sc.close();
    }

    public boolean login(String email, String senha){
        Scanner sc = new Scanner(System.in);
        System.out.println();
        return true; // mudar depois
    }

    public void signup(){

    }
}
