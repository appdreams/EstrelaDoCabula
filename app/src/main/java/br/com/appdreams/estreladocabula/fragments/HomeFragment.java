package br.com.appdreams.estreladocabula.fragments;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.annotations.Nullable;
import com.firebase.ui.database.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.activity.LoginActivity;
import br.com.appdreams.estreladocabula.adapter.UsuariosAdapter;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.utils.DividerItemDecoration;
import br.com.appdreams.estreladocabula.utils.RecyclerTouchListener;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends android.support.v4.app.Fragment
{

    private List<Usuario> usuariosList = new ArrayList<>();
    private RecyclerView rvListaDeUsuarios;
    private UsuariosAdapter mAdapter;
    private Firebase objetoRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        rvListaDeUsuarios = (RecyclerView) getView().findViewById(R.id.rvListaDeUsuarios);

        mAdapter = new UsuariosAdapter(usuariosList);

        rvListaDeUsuarios.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvListaDeUsuarios.setLayoutManager(mLayoutManager);
        rvListaDeUsuarios.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        rvListaDeUsuarios.setItemAnimator(new DefaultItemAnimator());
        rvListaDeUsuarios.setAdapter(mAdapter);

        rvListaDeUsuarios.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rvListaDeUsuarios,
                        new RecyclerTouchListener.OnTouchActionListener()
                        {
                            @Override
                            public void onClick(View view, int position)
                            {
                                Usuario usuario = usuariosList.get(position);
                                Toast.makeText(getApplicationContext(), usuario.getNome(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onLongClick(View view, int position)
                            {
                                Usuario usuario = usuariosList.get(position);
                                Toast.makeText(getApplicationContext(), usuario.getNome(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onRightSwipe(View view, int position)
                            {
                                Toast.makeText(getContext(), "onRightSwipe",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onLeftSwipe(View view, int position)
                            {
                                Toast.makeText(getContext(), "onLeftSwipe",Toast.LENGTH_SHORT).show();
                            }
                        }));

        prepareMovieData();

        //FIREBASE
        carregaDados();
        //FIREBASE


    }

    private void prepareMovieData()
    {
        Usuario usuario = new Usuario("01", "Paulo Vinicius Senna Figueiredo", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "29/03/2017 10:54:39", "S", "","D","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("02", "Abenilde Pires dos Santos", "abenilde@bol.com.br", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "15/03/2017 00:25:12", "S", "","A","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("03", "Bernardina Fernandes Vaz", "bernardina@uol.com.br", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "30/03/2017 04:36:17", "S", "","B","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("04", "Cecília Castelo Viegas", "celia.castelo@yahoo.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "30/03/2017 12:57:13", "N", "","D","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("05", "Danilsa Cunha de Almeida", "danilsa@bol.com.br", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "29/03/2017 17:02:28", "S", "","E","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("06", "Elga dos Santos de Sousa", "elga@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "28/03/2017 21:51:59", "S", "","F","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("07", "Feliciano Martins Pinheiro", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "28/03/2017 21:51:13", "N", "","D","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("08", "Genisvaldo do Nascimento", "genisvaldo@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "29/03/2017 17:01:25", "N", "","A","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("09", "Honesto Lima Baguide", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "28/03/2017 22:32:55", "S", "","B","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("10", "Joaquim Ramos Pinto", "joaquim@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "29/03/2017 10:55:03", "S", "","D","203", "");
        usuariosList.add(usuario);

        mAdapter.notifyDataSetChanged();
    }

    private void carregaDados()
    {

        Firebase.setAndroidContext(getContext());

        objetoRef = new Firebase("https://estrela-do-cabula.firebaseio.com/");
        Firebase novaRef = objetoRef.child("Usuarios");

        FirebaseListAdapter<String> adaptador = new FirebaseListAdapter<String>(getActivity(), String.class, android.R.layout.simple_list_item_1, novaRef) {
            @Override
            protected void populateView(View v, String model, int position) {

            }
        };
    }
}