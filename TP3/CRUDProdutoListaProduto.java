import java.util.Scanner;
import model.*;

public class CRUDProdutoListaProduto {

    private static ArquivoProduto arqProduto;
    private static ArquivoListaProduto arqListaProduto;
    private static Scanner sc = new Scanner(System.in);

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

    public static void main(String[] args) {
        try {
            arqProduto = new ArquivoProduto();
            arqListaProduto = new ArquivoListaProduto();

            int opcao;
            do {
                System.out.println("\n===== CRUD Produto & ListaProduto =====");
                System.out.println("1. Criar produto");
                System.out.println("2. Listar produtos");
                System.out.println("3. Adicionar produto a uma lista");
                System.out.println("4. Listar produtos de uma lista");
                System.out.println("5. Listar listas que contêm um produto");
                System.out.println("6. Atualizar associação (quantidade/observações)");
                System.out.println("7. Remover associação");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                opcao = Integer.parseInt(sc.nextLine());

                switch (opcao) {
                    case 1 -> criarProduto();
                    case 2 -> listarProdutos();
                    case 3 -> adicionarProdutoNaLista();
                    case 4 -> listarProdutosDeUmaLista();
                    case 5 -> listarListasDeUmProduto();
                    case 6 -> atualizarListaProduto();
                    case 7 -> removerAssociacao();
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida!");
                }

            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    // ---------- CRIAR PRODUTO ----------
    private static void criarProduto() throws Exception {
        System.out.println("\n=== Criar Produto ===");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String desc = sc.nextLine();
        String gtin = gerarGtin13();
        System.out.println("GTIN-13 gerado: " + gtin);

        Produto p = new Produto(gtin, nome, desc, false);
        int id = arqProduto.create(p);
        System.out.println("Produto criado com sucesso! ID = " + id);
    }

    // ---------- LISTAR PRODUTOS ----------
    private static void listarProdutos() throws Exception {
    System.out.println("\n=== Lista de Produtos ===");

    int id = 1;
    while (true) {
        Produto p = arqProduto.read(id);
        if (p == null) break; // chega no fim ou registro inexistente
        System.out.printf("[%d] %s | GTIN=%s | %s%n",
                p.getID(), p.getNome(), p.getGtin13(), p.getDescricao());
        id++;
    }
}


    // ---------- ADICIONAR PRODUTO À LISTA ----------
    private static void adicionarProdutoNaLista() throws Exception {
        System.out.print("ID da lista: ");
        int idLista = Integer.parseInt(sc.nextLine());
        System.out.print("ID do produto: ");
        int idProduto = Integer.parseInt(sc.nextLine());
        System.out.print("Quantidade: ");
        int qtd = Integer.parseInt(sc.nextLine());
        System.out.print("Observações (opcional): ");
        String obs = sc.nextLine();

        ListaProduto lp = new ListaProduto(idLista, idProduto, qtd, obs);
        int id = arqListaProduto.create(lp);
        System.out.println("Produto adicionado à lista! Associação ID = " + id);
    }

    // ---------- LISTAR PRODUTOS DE UMA LISTA ----------
    private static void listarProdutosDeUmaLista() throws Exception {
        System.out.print("ID da lista: ");
        int idLista = Integer.parseInt(sc.nextLine());
        ListaProduto[] relacoes = arqListaProduto.readPorLista(idLista);
        if (relacoes == null || relacoes.length == 0) {
            System.out.println("Nenhum produto nesta lista.");
            return;
        }
        for (ListaProduto lp : relacoes) {
            Produto p = arqProduto.read(lp.getIdProduto());
            System.out.printf("[%d] %s | qtd=%d | obs=%s%n",
                    lp.getID(),
                    (p != null ? p.getNome() : "(produto removido)"),
                    lp.getQuantidade(),
                    lp.getObservacoes() != null ? lp.getObservacoes() : "(nenhuma)");
        }
    }

    // ---------- LISTAR LISTAS DE UM PRODUTO ----------
    private static void listarListasDeUmProduto() throws Exception {
        System.out.print("ID do produto: ");
        int idProduto = Integer.parseInt(sc.nextLine());
        ListaProduto[] relacoes = arqListaProduto.readPorProduto(idProduto);
        if (relacoes == null || relacoes.length == 0) {
            System.out.println("Produto não está em nenhuma lista.");
            return;
        }
        for (ListaProduto lp : relacoes) {
            System.out.printf("Associação ID=%d | Lista ID=%d | qtd=%d | obs=%s%n",
                    lp.getID(),
                    lp.getIdLista(),
                    lp.getQuantidade(),
                    lp.getObservacoes() != null ? lp.getObservacoes() : "(nenhuma)");
        }
    }

    // ---------- ATUALIZAR ASSOCIAÇÃO ----------
    private static void atualizarListaProduto() throws Exception {
        System.out.print("ID da associação (ListaProduto): ");
        int id = Integer.parseInt(sc.nextLine());
        ListaProduto lp = arqListaProduto.read(id);
        if (lp == null) {
            System.out.println("Associação não encontrada.");
            return;
        }
        System.out.print("Nova quantidade (" + lp.getQuantidade() + "): ");
        String sQtd = sc.nextLine();
        if (!sQtd.isBlank()) lp.setQuantidade(Integer.parseInt(sQtd));
        System.out.print("Nova observação (" + (lp.getObservacoes() != null ? lp.getObservacoes() : "") + "): ");
        String obs = sc.nextLine();
        if (!obs.isBlank()) lp.setObservacoes(obs);
        if (arqListaProduto.update(lp)) System.out.println("Associação atualizada!");
        else System.out.println("Falha ao atualizar.");
    }

    // ---------- REMOVER ASSOCIAÇÃO ----------
    private static void removerAssociacao() throws Exception {
        System.out.print("ID da associação (ListaProduto): ");
        int id = Integer.parseInt(sc.nextLine());
        if (arqListaProduto.delete(id)) System.out.println("Associação removida!");
        else System.out.println("Falha ao remover (pode não existir).");
    }
}
