Relatório:

Integrantes do grupo:
Fabiano Queiroz Guimarães Almeida
Mateus Evangelista do Nascimento
Fernando Theodoro Porto de Paula Dias
Diogo Patrick Cesário de Oliveira


O nosso programa funciona da seguinte forma:
A classe Principal é responsável pelas operações do menu principal, de um usuário deslogado. O metódo main inicializa um objeto ArquivoUsuario arqUsuarios para lidar com operações de CRUD de usuário. 
A partir disso é declarado um boolean running que indica a execução das operações de menu de um usuário logado ou da tela principal (login e CRUD de usuário). Após isso temos um switch case que, dependendo
do input do usuário, chama um desses metódos: (telaLogin telaCadastro telaAtualizar telaExcluir telaRecuperar) para realizar uma das operações de CRUD de usuário, recuperar senha ou sair do programa. 
Essas operações de usuário chamam a classe ArquivoUsuario que realiza o crud chamando a classe pai Arquivo e manipulando corretamente todos os índices diretos e indiretos dos arquivos de HashExtensível
e ArvoreBMais.

Suponhamos que um usuário criou com sucesso sua conta e logou corretamente com seus dados. A partir daí o programa irá chamar o metódo telaMenuUsuario do MenuUsuario para realizar prints de tela de menu
de usuário logado. Essa tela imprimirá todas as listas do usuário e apresenta opções previstas no código. Para criar uma lista basta fornecer os dados exigidos e a lista será criada. Para consultar,
atualizar ou apagar uma lista basta selecionar a operação e depois selecionar o index da lista correspondente a sua ordem alfabética dentre todas as listas. 

Voltando nas operações de menu de usuário, essas operações que correspondem ao CRUD de listas chamam a classe ArquivoUsuario que realiza o crud chamando a classe pai Arquivo e manipulando 
corretamente todos os índices diretos e indiretos dos arquivos de HashExtensível e ArvoreBMais. As listas são criadas setando sua chave estrangeira id que é mandanda como parâmetro quando o MenuUsuario
é chamado dentro da Principal. Elas também são criadas setando seu código compartilhável de 10 caracteres a partir da classe NanoId dentro do package nanoid, gerando automaticamente o código imutável
de uma lista.


Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente? 
Sim
Há um CRUD de listas (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade) que funciona corretamente?
Sim
As listas de presentes estão vinculadas aos usuários usando o idUsuario como chave estrangeira?
Sim
Há uma árvore B+ que registre o relacionamento 1:N entre usuários e listas?
Sim
Há um CRUD de usuários (que estende a classe ArquivoIndexado, acrescentando Tabelas Hash Extensíveis e Árvores B+ como índices diretos e indiretos conforme necessidade)?
Sim
Há uma visualização das listas de outras pessoas por meio de um código NanoID?
Sim
O trabalho compila corretamente?
Sim
O trabalho está completo e funcionando sem erros de execução?
Sim
O trabalho é original e não a cópia de um trabalho de outro grupo?
Sim
