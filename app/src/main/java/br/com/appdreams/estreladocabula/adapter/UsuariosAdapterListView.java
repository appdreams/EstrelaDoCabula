package br.com.appdreams.estreladocabula.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Paulo on 30/03/2017.
 */

public class UsuariosAdapterListView extends RecyclerView.Adapter<UsuariosAdapterListView.MyViewHolder>
{

    private List<Usuario> usuariosList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtNome, txtBlocoAp, txtOnline, txtOffline, txtAcesso;
        public CircleImageView imgFoto;

        public MyViewHolder(View view)
        {
            super(view);
            txtNome         = (TextView) view.findViewById(R.id.txtNome);
            txtBlocoAp      = (TextView) view.findViewById(R.id.txtBlocoAp);
            txtOnline       = (TextView) view.findViewById(R.id.txtOnline);
            txtOffline      = (TextView) view.findViewById(R.id.txtOffline);
            txtAcesso       = (TextView) view.findViewById(R.id.txtAcesso);
            imgFoto         = (CircleImageView) view.findViewById(R.id.imgFoto);
        }
    }

    public UsuariosAdapterListView(List<Usuario> usuariosList)
    {
        this.usuariosList = usuariosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_usuarios, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Usuario usuario = usuariosList.get(position);
        holder.txtNome.setText(usuario.getNome());
        holder.txtBlocoAp.setText("BLOCO "+usuario.getBloco()+" / AP "+usuario.getApartamento());

        if(usuario.getOnline() == "S")
        {
            holder.txtOnline.setVisibility(View.VISIBLE);
            holder.txtOffline.setVisibility(View.GONE);
        }
        else
        {
            holder.txtOnline.setVisibility(View.GONE);
            holder.txtOffline.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(usuario.getFoto()))
        {
            Picasso.with(mContext).load(usuario.getFoto()).placeholder(R.drawable.sem_foto).into(holder.imgFoto);
        }

        holder.txtAcesso.setText(usuario.getAcesso());
    }

    @Override
    public int getItemCount()
    {
        return usuariosList.size();
    }
}