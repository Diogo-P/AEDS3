class User{
    private int id;
    private String nome;
    private String email;
    private int hashSenha;
    private String perguntaSecreta;
    private String respostaSecreta;
    public

    int getId(){
        return this.id;
    }

    String getNome(){
        return this.nome;
    }void setNome(String nome){
        this.nome=nome;
    }

    
    String getEmail(){
        return this.email;
    }void setEmail(String email){
        this.email=email;
    }

    int getHashSenha(){
        return this.hashSenha;
    }void setHashSenha(int hashSenha){
        this.hashSenha=hashSenha;
    }

    String getPerguntaSecreta(){
        return this.perguntaSecreta;
    }void setPerguntaSecreta(String perguntaSecreta){
        this.perguntaSecreta=perguntaSecreta;
    }

     String getRespostaSecreta(){
        return this.respostaSecreta;
    }void setRespostaSecreta(String respostaSecreta){
        this.respostaSecreta=respostaSecreta;
    }

    public User(){
        this.id=0;
        this.nome="";
        this.email="";
        this.hashSenha=0;
        this.perguntaSecreta="";
        this.respostaSecreta="";
    }

}