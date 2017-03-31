package br.com.appdreams.estreladocabula.adapter;

/**
 * Created by Paulo on 31/03/2017.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matteo on 24/08/2015.
 */
public class UsuarioAdapterRecycleview extends FirebaseRecyclerAdapter<UsuarioAdapterRecycleview.ViewHolder, Usuario> {

    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNome;
        public TextView txtBlocoAp;
        public TextView txtOnline;
        public TextView txtOffline;
        public TextView txtAcesso;
        public CircleImageView imgFoto;

        public ViewHolder(View view) {
            super(view);
            txtNome         = (TextView) view.findViewById(R.id.txtNome);
            txtBlocoAp      = (TextView) view.findViewById(R.id.txtBlocoAp);
            txtOnline       = (TextView) view.findViewById(R.id.txtOnline);
            txtOffline      = (TextView) view.findViewById(R.id.txtOffline);
            txtAcesso       = (TextView) view.findViewById(R.id.txtAcesso);
            imgFoto         = (CircleImageView) view.findViewById(R.id.imgFoto);
        }
    }

    public UsuarioAdapterRecycleview(Query query, Class<Usuario> usuarioClass, @Nullable ArrayList<Usuario> items, @Nullable ArrayList<String> keys)
    {
        super(query, items, keys);
    }

    @Override public UsuarioAdapterRecycleview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lista_usuarios, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(UsuarioAdapterRecycleview.ViewHolder holder, int position) {
        Usuario usuario = getItem(position);
        holder.txtNome.setText(usuario.getNome());

        if((usuario.getBloco() == null)||(usuario.getApartamento() == null))
        {
            holder.txtBlocoAp.setText("Apartamento não informado.");
        }
        else
        {
            holder.txtBlocoAp.setText("Bloco "+usuario.getBloco()+" / AP "+usuario.getApartamento());
        }

        if(usuario.getOnline().equals("S"))
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

    @Override protected void itemAdded(Usuario item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(Usuario oldItem, Usuario newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(Usuario item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(Usuario item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}