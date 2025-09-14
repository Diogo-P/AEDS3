class Usuario{
    protected String nome;
    protected String email;
    protected int hashSenha;
    protected String perguntaSecreta;
    protected String respostaSecreta;
    protected Lista[] listas;

    public Usuario(){
        this.nome="";
        this.email="";
        this.hashSenha=0;
        this.perguntaSecreta="";
        this.respostaSecreta="";
    }

    public Usuario(String nome, String email, int hashSenha, String perguntaSecreta, String respostaSecreta){
        this.nome=nome;
        this.email=email;
        this.hashSenha=hashSenha;
        this.perguntaSecreta=perguntaSecreta;
        this.respostaSecreta=respostaSecreta;
    }
}