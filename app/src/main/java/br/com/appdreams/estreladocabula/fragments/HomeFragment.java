package br.com.appdreams.estreladocabula.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.adapter.UsuarioAdapterRecycleView;
import br.com.appdreams.estreladocabula.adapter.UsuariosAdapterListView;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.service.SendPushNotificationAsync;
import br.com.appdreams.estreladocabula.utils.DividerItemDecoration;
import br.com.appdreams.estreladocabula.utils.MarginItemDecoration;
import br.com.appdreams.estreladocabula.utils.RecyclerTouchListener;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends android.support.v4.app.Fragment
{

    private Usuario usuarioLogado;
    private RecyclerView rvListaDeUsuarios;
    private ProgressBar pbCarregando;
    private Query mQuery;
    private UsuarioAdapterRecycleView mMyAdapter;
    private ArrayList<Usuario> mAdapterItems;
    private ArrayList<String> mAdapterKeys;

    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private UsuariosAdapterListView mAdapter;
    private List<Usuario> usuariosList = new ArrayList<>();

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

        bindActivity();
        showLoading();
        cargaUsuarioLogado();
        handleInstanceState(savedInstanceState);
        setupFirebase();
        setupRecyclerview();


    }

    /* Bind Objetos*/
    private void bindActivity()
    {
        pbCarregando        = (ProgressBar) getView().findViewById(R.id.pbCarregando);
        rvListaDeUsuarios   = (RecyclerView) getView().findViewById(R.id.rvListaDeUsuarios);
    }

    private void cargaUsuarioLogado()
    {
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            usuarioLogado = bundle.getParcelable("usuario");
            //Toast.makeText(getApplicationContext(), "===> "+usuarioLogado.getPrimeiroNome(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargaDadosTeste()
    {
        Usuario usuario = new Usuario("01", "Paulo Vinicius Senna Figueiredo", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "29/03/2017 10:54:39", "S", "","D","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("02", "Abenilde Pires dos Santos", "abenilde@bol.com.br", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "15/03/2017 00:25:12", "S", "","A","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("03", "Bernardina Fernandes Vaz", "bernardina@uol.com.br", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "30/03/2017 04:36:17", "S", "","B","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("04", "Cecília Castelo Viegas", "celia.castelo@yahoo.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "30/03/2017 12:57:13", "N", "","D","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("05", "Danilsa Cunha de Almeida", "danilsa@bol.com.br", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "29/03/2017 17:02:28", "S", "","E","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("06", "Elga dos Santos de Sousa", "elga@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "28/03/2017 21:51:59", "S", "","F","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("07", "Feliciano Martins Pinheiro", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "28/03/2017 21:51:13", "N", "","D","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("08", "Genisvaldo do Nascimento", "genisvaldo@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "29/03/2017 17:01:25", "N", "","A","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("09", "Honesto Lima Baguide", "pvincius@gmail.com", "", "", "https://s-media-cache-ak0.pinimg.com/originals/72/26/ed/7226edc1b5b9b0de0af1f19ec6032c3a.jpg", "28/03/2017 22:32:55", "S", "","B","","203", "");
        usuariosList.add(usuario);

        usuario = new Usuario("10", "Joaquim Ramos Pinto", "joaquim@gmail.com", "", "", "http://www.dicasenovidades.com.br/wp-content/gallery/angelina-jolie-2014/angelina-jolie-29.jpg", "29/03/2017 10:55:03", "S", "","D","","203", "");
        usuariosList.add(usuario);

        mAdapter.notifyDataSetChanged();
    }

    private void setupFirebase()
    {
        Firebase.setAndroidContext(getContext());
        mQuery  = FirebaseDatabase.getInstance().getReference("Usuarios").orderByChild("nome");
    }

    private void setupRecyclerview()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        rvListaDeUsuarios.setHasFixedSize(true);
        rvListaDeUsuarios.setLayoutManager(mLayoutManager);
        //
        //int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.com_facebook_likeboxcountview_text_padding);
        //rvListaDeUsuarios.addItemDecoration(new MarginItemDecoration(200));
        //
        rvListaDeUsuarios.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        rvListaDeUsuarios.setItemAnimator(new DefaultItemAnimator());

        mMyAdapter = new UsuarioAdapterRecycleView(mQuery, Usuario.class, mAdapterItems, mAdapterKeys);
        rvListaDeUsuarios.setAdapter(mMyAdapter);

        rvListaDeUsuarios.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rvListaDeUsuarios,
                        new RecyclerTouchListener.OnTouchActionListener()
                        {
                            @Override
                            public void onClick(View view, int position)
                            {
                                Usuario usuario = mAdapterItems.get(position);
                                Toast.makeText(getApplicationContext(), "Você cutucou "+usuario.getPrimeiroNome()+"!", Toast.LENGTH_SHORT).show();

                                //
                                try
                                {
                                    new SendPushNotificationAsync().execute(usuario.getToken(), usuarioLogado.getPrimeiroNome()+" cutucou você!");
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                //

                            }
                            @Override
                            public void onLongClick(View view, int position)
                            {
                                Usuario usuario = mAdapterItems.get(position);
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

        hideLoading();
    }

    private void handleInstanceState(Bundle savedInstanceState)
    {
        if ((savedInstanceState != null) && (savedInstanceState.containsKey(SAVED_ADAPTER_ITEMS)) && (savedInstanceState.containsKey(SAVED_ADAPTER_KEYS)))
        {
            mAdapterItems = Parcels.unwrap(savedInstanceState.getParcelable(SAVED_ADAPTER_ITEMS));
            mAdapterKeys = savedInstanceState.getStringArrayList(SAVED_ADAPTER_KEYS);
        }
        else
        {
            mAdapterItems = new ArrayList<Usuario>();
            mAdapterKeys = new ArrayList<String>();
        }
    }

    private void showLoading()
    {
        pbCarregando.setVisibility(View.VISIBLE);
    }

    private void hideLoading()
    {
        pbCarregando.setVisibility(View.GONE);
    }

}