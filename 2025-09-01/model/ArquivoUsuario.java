package model;

import java.util.ArrayList;
import aeds3.*;

public class ArquivoUsuario extends Arquivo<Usuario> {

    HashExtensivel<ParEmailID> indiceIndiretoEmail;
    ArvoreBMais<ParNomeId> indiceIndiretoNome;

    public ArquivoUsuario() throws Exception {
        super("usuario", Usuario.class.getConstructor());
        indiceIndiretoEmail = new HashExtensivel<>(
            ParEmailID.class.getConstructor(), 
            3, 
            "./dados/usuario.email.d.db", 
            "./dados/usuario.email.c.db"
        );
        indiceIndiretoNome = new ArvoreBMais<>(
            ParNomeId.class.getConstructor(), 
            5, 
            "./dados/usuario.nome.db"
        );
    }

    public int create(Usuario usuario) throws Exception {
        int id = super.create(usuario);
        indiceIndiretoEmail.create(new ParEmailID(usuario.getEmail(), id));
        indiceIndiretoNome.create(new ParNomeId(usuario.getNome(), id));
        return id;
    }

    public Usuario readEmail(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei == null)
            return null;
        int id = pei.getID();
        return super.read(id);
    }

    public Usuario[] readNome(String nome) throws Exception {
        ArrayList<ParNomeId> pnis = indiceIndiretoNome.read(new ParNomeId(nome, -1));
        Usuario[] usuarios = new Usuario[pnis.size()];
        int i = 0;
        for (ParNomeId pni : pnis) {
            usuarios[i++] = super.read(pni.getId());
        }
        return usuarios;
    }

    public boolean update(Usuario novoUsuario) throws Exception {
        Usuario usuarioAntigo = super.read(novoUsuario.getID());
        String emailAnterior = usuarioAntigo.getEmail();
        String nomeAnterior = usuarioAntigo.getNome();

        if (super.update(novoUsuario)) {
            if (!novoUsuario.getEmail().equals(emailAnterior)) {
                indiceIndiretoEmail.delete(ParEmailID.hash(emailAnterior));
                indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getID()));
            }
            if (!novoUsuario.getNome().equals(nomeAnterior)) {
                indiceIndiretoNome.delete(new ParNomeId(nomeAnterior, novoUsuario.getID()));
                indiceIndiretoNome.create(new ParNomeId(novoUsuario.getNome(), novoUsuario.getID()));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario usuario = super.read(id);
        String email = usuario.getEmail();
        String nome = usuario.getNome();
        if (super.delete(id)) {
            if (indiceIndiretoEmail.delete(ParEmailID.hash(email))
                    && indiceIndiretoNome.delete(new ParNomeId(nome, id)))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
}
