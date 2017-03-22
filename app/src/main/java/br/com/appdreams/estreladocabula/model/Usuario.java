package br.com.appdreams.estreladocabula.model;

/**
 * Created by Paulo on 17/03/2017.
 */

public class Usuario
{
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;
    private String foto;
    private String acesso;
    private String online;
    private String origem;
    private String status;

    public Usuario()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
    }

    public Usuario(String id, String nome, String email, String senha, String tipo, String foto, String acesso, String online, String origem, String status)
    {

        this.id     = id;
        this.nome   = nome;
        this.email  = email;
        this.senha  = senha;
        this.tipo   = tipo;
        this.foto   = foto;
        this.acesso = acesso;
        this.online = online;
        this.origem = origem;
        this.status = status;
    }

    public String getID()
    {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getFoto()
    {
        return foto;
    }

    public void setFoto(String foto)
    {
        this.foto = foto;
    }

    public String getAcesso()
    {
        return acesso;
    }

    public void setAcesso(String acesso)
    {
        this.acesso = acesso;
    }

    public String getOnline()
    {
        return online;
    }

    public void setOnline(String online)
    {
        this.online = online;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
