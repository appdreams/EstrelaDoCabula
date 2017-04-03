package br.com.appdreams.estreladocabula.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.fragments.HomeFragment;
import br.com.appdreams.estreladocabula.model.Usuario;

public class MainActivity extends BaseActivity
{
    //Firebase
    private Firebase mFirebase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    //Firebase

    private Usuario usuario = new Usuario();

    private TextView txtNome;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingShow("", "Carregando dados...");

        firebaseAuth();
        firebaseDatabase();

        setUpToolbar("Estrela do Cabula");
        setUpNavDrawer();

        //registrationBroadcastReceiverFirebaseMenssage();

        bindActivity();
        addListenerOnButtonSair();

        carregaDadosUsuario(getUsuarioUID());

        //replaceFragment(R.layout.fragment_home);

        //alert(getUsuario().getNome());

    }

    /* Bind Objetos*/
    private void bindActivity()
    {
        txtNome        =   (TextView) findViewById(R.id.txtNome);
        //btnSair        =   (Button) findViewById(R.id.btnSair);
    }

    /* Get Firebase Auth Instance */
    protected void firebaseAuth()
    {
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    /* Get Firebase Database instance */
    protected void firebaseDatabase()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    /* Eventos Botão Sair */
    private void addListenerOnButtonSair()
    {
        /*btnSair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                logout();
            }
        });*/
    }

    /* Get UID Usuário */
    private String getUsuarioUID()
    {
        FirebaseUser usuarioLogado = mFirebaseAuth.getCurrentUser();
        return usuarioLogado.getUid();
    }

    /* Carrega Dados do Usuário */
    private void carregaDadosUsuario(final String uid)
    {
        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();
        Query query = raiz.child("Usuarios").child(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    setUsuario(dataSnapshot.getValue(Usuario.class));

                    //txtNome.setText(getUsuario().getNome());

                    setNavHeaderValues(navigationView, getUsuario().getNome(), getUsuario().getEmail(), getUsuario().getFoto(), R.drawable.nav_drawer_header2);

                    setMenu(getUsuario().getTipo());

                    replaceFragment(new HomeFragment());

                    loadingHide();
                }
                else
                {
                    //alert("Não Achou!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                alert(databaseError.getMessage().toString());
            }
        });
    }

    /* getUsuario() */
    private Usuario getUsuario()
    {
        return usuario;
    }

    /* setUsuario() */
    private void setUsuario(Usuario usuario)
    {
        this.usuario = usuario;
    }

    private void logout()
    {
        DatabaseReference tbUsuarios = mFirebaseDatabase.getReference("Usuarios");
        tbUsuarios.child(getUsuarioUID()).child("online").setValue("N");

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNavDrawerItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.nav_menu_home_m:
                replaceFragment(new HomeFragment());
                break;
            case R.id.nav_item_carros_todos:
                // Nada aqui pois somente a MainActivity possui menu lateral
                //navigationView = (NavigationView) findViewById(R.id.menu_drawer);
                setNavHeaderValues(navigationView, "Maria Joaquina", "maria@bol.com.br", "http://portal.tododia.uol.com.br/_midias/jpg/2014/10/06/larissa_manoela-268490.jpg", R.drawable.nav_drawer_header1);
                setNavMenuView(navigationView, R.menu.nav_drawer_menu);
                break;
            case R.id.nav_item_carros_classicos:
                /*Intent intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.classicos);
                startActivity(intent);*/
                break;
            case R.id.nav_item_carros_esportivos:
                /*intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.esportivos);
                startActivity(intent);*/
                break;
            case R.id.nav_item_carros_luxo:
                /*intent = new Intent(getContext(), CarrosActivity.class);
                intent.putExtra("tipo", R.string.luxo);
                startActivity(intent);*/
                break;
            case R.id.nav_item_site_livro:
                /*startActivity(new Intent(getContext(), SiteLivroActivity.class));*/
                break;
            case R.id.nav_menu_sair_m:
                logout();
                break;
            case R.id.nav_menu_sair_p:
                logout();
                break;
        }
    }

    private void setMenu(String tipo)
    {
        switch (tipo)
        {
            case "Sindico":
                setNavMenuView(navigationView, R.menu.menu_sindico);
                break;
            case "Morador":
                setNavMenuView(navigationView, R.menu.menu_morador);
                break;
            case "Porteiro":
                setNavMenuView(navigationView, R.menu.menu_porteiro);
                break;
        }
    }

}
