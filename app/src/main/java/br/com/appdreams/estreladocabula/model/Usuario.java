package br.com.appdreams.estreladocabula.model;

/**
 * Created by Paulo on 17/03/2017.
 */

public class Usuario
{
    public  String id;
    public  String nome;
    public  String email;
    public  String senha;
    public  String tipo;
    public  String foto;
    public  String acesso;
    public  String online;
    public  String origem;
    private String bloco;
    private String apartamento;
    public  String status;

    public Usuario()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
    }

    public Usuario(String id, String nome, String email, String senha, String tipo, String foto, String acesso, String online, String origem, String bloco, String apartamento, String status)
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
        this.bloco = bloco;
        this.apartamento = apartamento;
        this.status = status;
    }/**/

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
        if(nome == "")
        {
            this.nome = "";
        }
        else
        {
            this.nome = nome;
        }
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        if(email == "")
        {
            this.email = "";
        }
        else
        {
            this.email = email;
        }
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        if(senha == "")
        {
            this.senha = "";
        }
        else
        {
            this.senha = senha;
        }
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        if(tipo == "")
        {
            this.tipo = "";
        }
        else
        {
            this.tipo = tipo;
        }
    }

    public String getFoto()
    {
        return foto;
    }

    public void setFoto(String foto)
    {
        if(foto == "")
        {
            this.foto = "";
        }
        else
        {
            this.foto = foto;
        }
    }

    public String getAcesso()
    {
        return acesso;
    }

    public void setAcesso(String acesso)
    {
        if(acesso == "")
        {
            this.acesso = "";
        }
        else
        {
            this.acesso = acesso;
        }
    }

    public String getOnline()
    {
        return online;
    }

    public void setOnline(String online)
    {
        if(online == "")
        {
            this.online = "";
        }
        else
        {
            this.online = online;
        }
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        if(status == "")
        {
            this.status = "";
        }
        else
        {
            this.status = status;
        }
    }

    public String getOrigem()
    {
        return origem;
    }

    public void setOrigem(String origem)
    {
        if(origem == "")
        {
            this.origem = "";
        }
        else
        {
            this.origem = origem;
        }
    }

    public String getBloco()
    {
        return bloco;
    }

    public void setBloco(String bloco)
    {
        if(bloco == "")
        {
            this.bloco = "";
        }
        else
        {
            this.bloco = bloco;
        }
    }

    public String getApartamento()
    {
        return apartamento;
    }

    public void setApartamento(String apartamento)
    {
        if(apartamento == "")
        {
            this.apartamento = "";
        }
        else
        {
            this.apartamento = apartamento;
        }
    }
}
