package br.com.appdreams.estreladocabula.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;

public class MainActivity extends BaseActivity
{
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth();
        bindActivity();
        addListenerOnButtonSair();


        alert(getUsuario().getNome());
    }


    /* Bind Objetos*/
    private void bindActivity()
    {
        btnSair        =   (Button) findViewById(R.id.btnSair);
    }

    /* Eventos Botão Sair */
    private void addListenerOnButtonSair()
    {
        btnSair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                alert("","LogOut!");
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void firebaseAuth()
    {
        //Get Firebase auth instance
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    private String getUsuarioUID()
    {
        FirebaseUser usuarioLogado = mFirebaseAuth.getCurrentUser();
        return usuarioLogado.getUid();
    }

    /* Get Usuário Logado*/
    private Usuario getUsuario()
    {
        final Usuario usuario = new Usuario();

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();
        Query query = raiz.child("Usuarios").child(getUsuarioUID());
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Passar os dados para a interface grafica
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren())
                    {
                        usuario = noteSnapshot.getValue(Usuario.class);
                        alert(usuario.getEmail());
                        break;
                    }
                }
                else
                {
                    alert("Não Achou!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //Se ocorrer um erro
                alert(databaseError.getMessage().toString());
            }
        });
        return usuario;
    }

}
