package br.com.appdreams.estreladocabula.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.utils.Validacoes;

public class RecuperarSenhaActivity extends BaseActivity
{
    private View rootView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private TextInputLayout tilEmail;
    private EditText txtEmail;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        firebaseAuth();

        setUpToolbar("Recuperar senha");
        setHomeAsUp();

        bindActivity();

        addListenerOnButtonEnviar();
    }

    /* Get Firebase Auth Instance */
    protected void firebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
    }

    /* Bind Objetos*/
    private void bindActivity()
    {
        rootView            =   (View) findViewById(R.id.recuperar_senha);
        tilEmail            =   (TextInputLayout) findViewById(R.id.tilEmail);
        txtEmail            =   (EditText) findViewById(R.id.txtEmail);
        btnEnviar           =   (Button) findViewById(R.id.btnEnviar);
    }

    /* Eventos Botão Login */
    private void addListenerOnButtonEnviar()
    {
        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(validaEmail())
                {
                    if (Validacoes.haveNetworkConnection(getContext(), rootView))
                    {
                        loadingShow("", "Validando email...");

                        String email = txtEmail.getText().toString().trim();

                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(RecuperarSenhaActivity.this, new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        loadingHide();

                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(RecuperarSenhaActivity.this, "Enviámos-lhe instruções para redefinir sua senha!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(RecuperarSenhaActivity.this, "O Email informado não foi localizado!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    /* Valida Email */
    public boolean validaEmail()
    {
        Boolean resposta = false;

        if(txtEmail.getText().toString().isEmpty())
        {
            tilEmail.setError("Email é obrigatório!");
            tilEmail.setErrorEnabled(true);
            txtEmail.requestFocus();
            resposta = false;
        }
        else if(!Validacoes.validaEmail(txtEmail.getText().toString()))
        {
            tilEmail.setError("Email Inválido!");
            tilEmail.setErrorEnabled(true);
            txtEmail.requestFocus();
            resposta = false;
        }
        else
        {
            tilEmail.setErrorEnabled(false);
            resposta = true;
        }

        return resposta;
    }
}
