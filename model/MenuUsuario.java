import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.ArquivoLista;
import model.ArquivoUsuario;
import model.Lista;

public class MenuUsuario{
    private final int id;
    private final ArquivoUsuario arquivoUsuario;
    private final ArquivoLista arquivoLista;

    public MenuUsuario(int id) throws Exception{
        this.id=id;
        arquivoUsuario = new ArquivoUsuario();
        arquivoLista = new ArquivoLista();
        
        //System.err.println("Erro de arquivoUsuario");
        
    }

    public static Lista opcao_c()throws Exception{
        Scanner sc = new Scanner(System.in);
        
//Apenas esses 3 dados estarão no input, os outros são automáticos
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        System.out.print("Data Limite: ");
        String limite = sc.nextLine();
//Manipulando datas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataCriacao = LocalDate.now();
        String tmp = dataCriacao.format(formatter);
        dataCriacao = LocalDate.parse(tmp,formatter);
        LocalDate dataLimite = LocalDate.parse(limite,formatter);
//Cria o objeto e dá sequência ao fluxo
        Lista lista =  new Lista(-1,nome,descricao,dataCriacao,dataLimite,"",-1,true);
        return lista;
    }

//     public static void opcao_r()throws Exception{
//         try{
//             short tam = 117;
//             long regs = arquivoLista.length/tam;
//             for(long i = 0; i<regs;i++){
//                 arquivoLista.read(id);
//             }
//         }catch(Exception e){
//             e.printStackTrace();
//         }
//         long regs = arquivoLista.length/117;
//         Scanner sc = new Scanner(System.in);
        
// //Apenas esses 3 dados estarão no input, os outros são automáticos
//         System.out.print("Nome: ");
//         String nome = sc.nextLine();
//         System.out.print("Descrição: ");
//         String descricao = sc.nextLine();
//         System.out.print("Data Limite: ");
//         String limite = sc.nextLine();
// //Manipulando datas
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//         LocalDate dataCriacao = LocalDate.now();
//         String tmp = dataCriacao.format(formatter);
//         dataCriacao = LocalDate.parse(tmp,formatter);
//         LocalDate dataLimite = LocalDate.parse(limite,formatter);
// //Cria o objeto e dá sequência ao fluxo
//         Lista lista =  new Lista(-1,nome,descricao,dataCriacao,dataLimite,"",-1,true);
//         return lista;
//     }




    // public static Lista opcao_u(){
    //     Lista lista=null;
    //     Scanner sc = new Scanner(System.in);
    //     System.out.print("Escolha a lista a ser atualizada: ");
    //     long pos = sc.nextLong();

    //     //Lista lista = readPos(pos);
    //     System.out.print("Novo nome da lista (aperte enter para manter): ");
    //     String nome = sc.nextLine();
    //     System.out.print("Nova descrição da lista (aperte enter para manter): ");
    //     String descricao = sc.nextLine();
    //     System.out.print("Nova data Limite: ");
    //     String limite = sc.nextLine();

    //     sc.close();

    //     return lista;
    // }

    public static void telaMenuUsuario()throws Exception{
        ArquivoLista arqLista;
        try{
            arqLista = new ArquivoLista();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        Scanner sc = new Scanner(System.in);
        char op;
        System.out.println("(C) Inserir lista");
        System.out.println("(R) Buscar lista");
        System.out.println("(U) Atualizar lista");
        System.out.println("(D) Apagar lista");
        System.out.println();
        System.out.println("(B) Voltar");
        System.out.println("(E) Sair");
        System.out.println();
        System.out.print("Opção: ");
        
        while (((op=sc.nextLine().charAt(0))!='B')&&((op=sc.nextLine().charAt(0))!='E')) {
            
        }
        switch (op) {
            case 'C':
                
                // System.out.print("Nome: ");
                // String nome = sc.nextLine();
                // System.out.print("Descrição: ");
                // String descricao = sc.nextLine();
                // System.out.print("Data Limite: ");
                // String limite = sc.nextLine();
                // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                // LocalDate date = LocalDate.parse(limite,formatter);
                Lista novaLista =  opcao_c();
                arqLista.create(novaLista);
                break;
            case 'R':
                //opcao_r();
                break;
            case 'U':
                //opcao_u();
                break;
            case 'D':
                //opcao_d();
                break;
        
            default: 
                System.out.println("Opção Inválida!");
                break;
        }
    }
}