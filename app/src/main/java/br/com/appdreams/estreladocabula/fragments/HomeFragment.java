package br.com.appdreams.estreladocabula.fragments;

import android.app.Activity;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.activity.LoginActivity;
import br.com.appdreams.estreladocabula.adapter.UsuariosAdapter;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.utils.DividerItemDecoration;
import br.com.appdreams.estreladocabula.utils.RecyclerTouchListener;

import static com.facebook.FacebookSdk.getApplicationContext;

import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends android.support.v4.app.Fragment
{

    private List<Usuario> usuariosList = new ArrayList<>();
    private RecyclerView rvListaDeUsuarios;
    //private ListView lvListaDeUsuarios;
    private UsuariosAdapter mAdapter;
    private Firebase objetoRef;
    private Activity view = new Activity();

    //
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

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

        //lvListaDeUsuarios = (ListView) getView().findViewById(R.id.lvListaDeUsuarios);
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

        /*Firebase.setAndroidContext(getContext());

        Firebase objetoRef = new Firebase("https://estrela-do-cabula.firebaseio.com/");
        Firebase novaRef = objetoRef.child("Usuarios");

        final Firebase ref = new Firebase("https://test.firebaseio.com/users");
        Query query = ref.orderByChild("username").equalTo("toto");

        FirebaseListAdapter<String> adaptador = new FirebaseListAdapter<String>(getActivity(), String.class, android.R.layout.simple_list_item_1, objetoRef) {
            @Override
            protected void populateView(View v, String model, int position) {

            }
        };

        Firebase mRef = new Firebase("https://estrela-do-cabula.firebaseio.com/Usuarios");
        ListAdapter adapter = new FirebaseListAdapter<Usuario>(this, Usuario.class, android.R.layout.two_line_list_item, mRef)
        {
            protected void populateView(View view, Usuario usuario)
            {
                ((TextView)view.findViewById(android.R.id.text1)).setText(chatMessage.getName());
                ((TextView)view.findViewById(android.R.id.text2)).setText(chatMessage.getMessage());
            }
        };
        rvListaDeUsuarios.setAdapter(adapter);

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        final DatabaseReference myRef = database.getReference("todoItems");

        Query myQuery = myRef.orderByValue().equalTo("");

        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                    firstChild.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/





        /*Firebase rootRef = new Firebase("https://estrela-do-cabula.firebaseio.com/");
        //DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Firebase novaRef = rootRef.child("Usuarios");


        FirebaseListAdapter<String> adaptador = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.row_lista_usuarios, novaRef) {
            @Override
            protected void populateView(View v, String model, int position) {

                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        lvListaDeUsuarios.setAdapter(adaptador);*/

        /*mFirebaseAdapter = new FirebaseRecyclerAdapter<Usuario, UsuariosAdapter>(Usuario.class, R.layout.row_lista_usuarios, UsuariosAdapter.class,
                        mRestaurantReference) {

            @Override
            protected void populateViewHolder(UsuariosAdapter viewHolder, Usuario model, int position)
            {
                viewHolder.bindViewHolder(model);
            }
        };
        rvListaDeUsuarios.setHasFixedSize(true);
        rvListaDeUsuarios.setLayoutManager(new LinearLayoutManager(this));
        rvListaDeUsuarios.setAdapter(mFirebaseAdapter);*/
    }
}