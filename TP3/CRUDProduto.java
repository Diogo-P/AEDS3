import java.util.Scanner;
import model.*;

public class CRUDProduto {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArquivoProduto arqProduto = null;

        try {
            arqProduto = new ArquivoProduto();
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo de produtos: " + e.getMessage());
            return;
        }

        int opcao = 0;

        do {
            System.out.println("\n====== CRUD DE PRODUTOS ======");
            System.out.println("1. Cadastrar produto");
            System.out.println("2. Buscar produto por ID");
            System.out.println("3. Buscar produto por GTIN-13");
            System.out.println("4. Buscar produtos por nome");
            System.out.println("5. Atualizar produto");
            System.out.println("6. Excluir produto");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(sc.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        criarProduto(sc, arqProduto);
                        break;
                    case 2:
                        lerProdutoPorID(sc, arqProduto);
                        break;
                    case 3:
                        lerProdutoPorGtin(arqProduto);
                        break;
                    case 4:
                        lerProdutoPorNome(sc, arqProduto);
                        break;
                    case 5:
                        atualizarProduto(sc, arqProduto);
                        break;
                    case 6:
                        deletarProduto(sc, arqProduto);
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (opcao != 0);

        sc.close();
    }

    // ======== CRUD MÉTODOS ========

    private static String gerarGtin13() {
        StringBuilder gtin = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            gtin.append((int)(Math.random() * 10));
        }
        
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = gtin.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        gtin.append(checkDigit);
        
        return gtin.toString();
    }

    private static void criarProduto(Scanner sc, ArquivoProduto arqProduto) throws Exception {
        String gtin = gerarGtin13();
        System.out.println("GTIN-13 gerado: " + gtin);
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();

        Produto p = new Produto(gtin, nome, descricao, false);
        int id = arqProduto.create(p);
        System.out.println("✅ Produto cadastrado com sucesso! ID: " + id);
    }

    private static void lerProdutoPorID(Scanner sc, ArquivoProduto arqProduto) throws Exception {
        System.out.print("ID do produto: ");
        int id = Integer.parseInt(sc.nextLine());
        Produto p = arqProduto.read(id);
        if (p != null)
            System.out.println(p);
        else
            System.out.println("❌ Produto não encontrado.");
    }

    private static void lerProdutoPorGtin(ArquivoProduto arqProduto) throws Exception {
        String gtin = gerarGtin13();
        
        System.out.println("Código GTIN-13 gerado: " + gtin);
        Produto p = arqProduto.readGtin(gtin);
        if (p != null)
            System.out.println(p);
        else
            System.out.println("❌ Produto não encontrado.");
    }

    private static void lerProdutoPorNome(Scanner sc, ArquivoProduto arqProduto) throws Exception {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        Produto[] produtos = arqProduto.readNome(nome);
        if (produtos.length > 0) {
            for (Produto p : produtos) {
                System.out.println(p);
                System.out.println("------------------------");
            }
        } else {
            System.out.println("❌ Nenhum produto encontrado com esse nome.");
        }
    }

    private static void atualizarProduto(Scanner sc, ArquivoProduto arqProduto) throws Exception {
        System.out.print("ID do produto a atualizar: ");
        int id = Integer.parseInt(sc.nextLine());
        Produto p = arqProduto.read(id);

        if (p == null) {
            System.out.println("❌ Produto não encontrado.");
            return;
        }

        System.out.println("Produto atual:");
        System.out.println(p);

        

        System.out.print("Novo nome (atual: " + p.getNome() + "): ");
        String novoNome = sc.nextLine();
        if (!novoNome.trim().isEmpty()) p.setNome(novoNome);

        System.out.print("Nova descrição (atual: " + p.getDescricao() + "): ");
        String novaDesc = sc.nextLine();
        if (!novaDesc.trim().isEmpty()) p.setDescricao(novaDesc);

        if (arqProduto.update(p))
            System.out.println("✅ Produto atualizado com sucesso!");
        else
            System.out.println("❌ Falha ao atualizar produto.");
    }

    private static void deletarProduto(Scanner sc, ArquivoProduto arqProduto) throws Exception {
        System.out.print("ID do produto a excluir: ");
        int id = Integer.parseInt(sc.nextLine());
        if (arqProduto.delete(id))
            System.out.println("✅ Produto excluído com sucesso!");
        else
            System.out.println("❌ Falha ao excluir produto (ou produto não encontrado).");
    }
}
