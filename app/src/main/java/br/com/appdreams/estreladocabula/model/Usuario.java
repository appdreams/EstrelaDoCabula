package br.com.appdreams.estreladocabula.model;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by Paulo on 17/03/2017.
 */
@Parcel
public class Usuario implements Parcelable
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
    public String bloco;
    public String apartamento;
    public String status;
    public String token;

    public Usuario()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Usuario.class)
    }

    public Usuario(String id, String nome, String email, String senha, String tipo, String foto, String acesso, String online, String origem, String bloco, String apartamento, String token, String status)
    {

        this.id             = id;
        this.nome           = nome;
        this.email          = email;
        this.senha          = senha;
        this.tipo           = tipo;
        this.foto           = foto;
        this.acesso         = acesso;
        this.online         = online;
        this.origem         = origem;
        this.bloco          = bloco;
        this.apartamento    = apartamento;
        this.status         = status;
        this.setToken(token);
    }/**/

    protected Usuario(android.os.Parcel in) {
        id = in.readString();
        nome = in.readString();
        email = in.readString();
        senha = in.readString();
        tipo = in.readString();
        foto = in.readString();
        acesso = in.readString();
        online = in.readString();
        origem = in.readString();
        bloco = in.readString();
        apartamento = in.readString();
        status = in.readString();
        token = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(android.os.Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        if(token == "")
        {
            this.token = "";
        }
        else
        {
            this.token = token;
        }
    }

    public String getPrimeiroNome()
    {
        String[] nomeArray = nome.split(" ");
        return nomeArray[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(senha);
        dest.writeString(tipo);
        dest.writeString(foto);
        dest.writeString(acesso);
        dest.writeString(online);
        dest.writeString(origem);
        dest.writeString(bloco);
        dest.writeString(apartamento);
        dest.writeString(status);
        dest.writeString(token);
    }
}
