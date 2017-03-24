package br.com.appdreams.estreladocabula.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.utils.Validacoes;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Firebase mFirebase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private ImageView imgLogo;
    private TextView txtEsqueceuSenha;
    private TextInputLayout tilEmail;
    private TextInputLayout tilSenha;
    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnLogin;
    private Button btnGoogle;
    private Button btnFacebook;
    private Button btnNovaConta;

    private String origem;

    //FaceBook
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //FACEBOOK

    //GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    //GOOGLE

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth();
        firebaseDatabase();

        bindActivity();

        addListenerOnButtonLogin();
        addListenerOnButtonGoogle();
        addListenerOnButtonFacebook();
        addListenerOnButtonNovaConta();
        addListenerOnButtonRecuperarSenha();

        // FACEBOOK
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                //alert("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel()
            {
                alert("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error)
            {
                alert("facebook:onError => "+ error);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();
                if (userFirebase != null)
                {
                    // User is signed in
                    //alert("onAuthStateChanged:signed_in:" + user.getUid());
                    alert(userFirebase.getUid()+"\n"+userFirebase.getDisplayName()+"\n"+userFirebase.getEmail()+"\n"+userFirebase.getPhotoUrl()+"\n"+origem+"\n"+userFirebase.getProviderId());
                    salvarNovoUsuario(userFirebase.getUid(),userFirebase.getDisplayName(),userFirebase.getEmail(),userFirebase.getPhotoUrl().toString());

                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //startActivity(intent);
                    //finish();
                }
                else
                {
                    // User is signed out
                    //alert("onAuthStateChanged: signed_out");
                }
            }
        };
        // FACEBOOK

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.google_web_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //GOOGLE


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /* Bind Objetos*/
    private void bindActivity()
    {
        imgLogo             =   (ImageView) findViewById(R.id.imgLogo);
        txtEsqueceuSenha    =   (TextView) findViewById(R.id.txtEsqueceuSenha);
        tilEmail            =   (TextInputLayout) findViewById(R.id.tilEmail);
        tilSenha            =   (TextInputLayout) findViewById(R.id.tilSenha);
        txtEmail            =   (EditText) findViewById(R.id.txtEmail);
        txtSenha            =   (EditText) findViewById(R.id.txtSenha);
        btnLogin            =   (Button) findViewById(R.id.btnLogin);
        btnGoogle           =   (Button) findViewById(R.id.btnGoogle);
        btnFacebook         =   (Button) findViewById(R.id.btnFacebook);
        btnNovaConta        =   (Button) findViewById(R.id.btnNovaConta);
    }

    /* Get Firebase Auth Instance */
    protected void firebaseAuth()
    {
        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
        {
            Toast.makeText(LoginActivity.this, "Usuário já logado no sistema!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }/**/
    }

    /* Get Firebase Database instance */
    protected void firebaseDatabase()
    {
        mDatabase = FirebaseDatabase.getInstance();
    }

    /* Get Usuário Logado*/
    private FirebaseUser getUsuarioLogado()
    {
        FirebaseUser usuarioLogado   =   mAuth.getCurrentUser();
        return usuarioLogado;
    }

    /* Eventos Botão Login */
    private void addListenerOnButtonLogin()
    {
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(validaEmail() && validaSenha())
                {
                    mAuth.removeAuthStateListener(mAuthListener);

                    loadingShow("", "Validando usuário...");

                    String email        =   txtEmail.getText().toString().trim();
                    String senha        =   txtSenha.getText().toString().trim();

                    //authenticate user
                    mAuth.signInWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    loadingHide();

                                    if (!task.isSuccessful())
                                    {
                                        mAuth.addAuthStateListener(mAuthListener);
                                        //Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        Toast.makeText(LoginActivity.this, "Autenticação falhou, verifique seu e-mail e senha!",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        salvarUltimoAcesso();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }
            }
        });
    }

    /* Eventos Botão Google */
    private void addListenerOnButtonGoogle()
    {
        btnGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                //alert("ATENÇÃO","Google!");
                origem = "GOOGLE";
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
    }

    /* Eventos Botão Facebook */
    private void addListenerOnButtonFacebook()
    {
        btnFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                origem = "FACEBOOK";
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });
    }

    /* Eventos Botão Nova Conta */
    private void addListenerOnButtonNovaConta()
    {
        btnNovaConta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(LoginActivity.this, PrimeiroAcessoActivity.class);
                startActivity(intent);
            }
        });
    }

    /* Eventos Botão Recuperar Senha */
    public void addListenerOnButtonRecuperarSenha()
    {
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(intent);

            }
        });
    }/**/

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

    /* Valida Senha */
    public boolean validaSenha()
    {
        Boolean resposta = false;

        if(txtSenha.getText().toString().isEmpty())
        {
            tilSenha.setError("Senha é obrigatória!");
            tilSenha.setErrorEnabled(true);
            txtSenha.requestFocus();
            resposta = false;
        }
        else if(txtSenha.getText().toString().trim().length() < 6)
        {
            tilSenha.setError("Senha deve conter no mínimo 6 caractéres!");
            tilSenha.setErrorEnabled(true);
            txtSenha.requestFocus();
            resposta = false;
        }
        else
        {
            tilSenha.setErrorEnabled(false);
            resposta = true;
        }

        return resposta;
    }

    /* Salva Ultimo Acesso */
    private void salvarUltimoAcesso()
    {
        DatabaseReference tbUsuarios = mDatabase.getReference("usuarios");
        tbUsuarios.child(getUsuarioLogado().getUid()).child("acesso").setValue(getDataHora());
    }

    /* Get Data Hora Atual*/
    private String getDataHora()
    {
        String data = "dd/MM/yyyy";
        String hora = "HH:mm:ss";
        String dataAtual, horaAtual, dataHoraAtual;
        java.util.Date agora = new java.util.Date();
        SimpleDateFormat formata = new SimpleDateFormat(data);
        dataAtual = formata.format(agora);
        formata = new SimpleDateFormat(hora);
        horaAtual = formata.format(agora);
        dataHoraAtual = dataAtual+" "+horaAtual;

        return dataHoraAtual;
    }

    //FaceBook
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                //alert("Foi => "+result.getStatus());
                //alert("Foi => "+result.isSuccess());
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token)
    {
        //alert("handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //alert(""+task.getResult());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Falha na autenticação!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //FaceBook

    //GOOGLE
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //alert("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //alert("signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //alert("signInWithCredential "+ task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    //GOOGLE

    /* Salvar Usuário */
    private void salvarNovoUsuario(String uid, String nome, String email, String foto)
    {
        Usuario usuario = new Usuario();
        usuario.setID(uid);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha("");
        usuario.setFoto(foto);
        usuario.setTipo("M");
        usuario.setAcesso(getDataHora());
        usuario.setOnline("S");
        usuario.setOrigem(origem);
        usuario.setStatus("1");

        DatabaseReference tbUsuarios = mDatabase.getReference("usuarios");
        tbUsuarios.child(usuario.getID()).setValue(usuario);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}