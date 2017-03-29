package br.com.appdreams.estreladocabula.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
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
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseException;
import com.firebase.ui.auth.core.FirebaseLoginError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.utils.Validacoes;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    //Firebase
    private Firebase mFirebase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    //Firebase

    //Geral
    private View rootView;
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
    //Geral

    //Flasg
    private String origem;
    private Boolean respsotaAchouUsuario = false;
    //Flasg

    //FaceBook
    private CallbackManager mFacebookCallbackManager;
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;
    //FACEBOOK

    //GOOGLE
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN = 9001;
    //GOOGLE

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext()); // for facebook log in

        bindActivity();

        firebaseAuth();
        firebaseDatabase();

        addListenerOnButtonLogin();
        addListenerOnButtonGoogle();
        addListenerOnButtonFacebook();
        addListenerOnButtonNovaConta();
        addListenerOnButtonRecuperarSenha();

        // FACEBOOK
        initFBFacebookLogIn();
        // FACEBOOK

        //GOOGLE
        initFBGoogleSignIn();
        //GOOGLE


    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    /* Bind Objetos*/
    private void bindActivity()
    {
        rootView            =   (View) findViewById(R.id.login);
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
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (mFirebaseAuth.getCurrentUser() != null)
        {
            if(Validacoes.haveNetworkConnection(getContext(), rootView))
            {
                Toast.makeText(LoginActivity.this, "Usuário já logado no sistema!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }/**/
    }

    /* Get Firebase Database instance */
    protected void firebaseDatabase()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    /* Get Usuário Logado*/
    private FirebaseUser getUsuarioLogado()
    {
        FirebaseUser usuarioLogado   =   mFirebaseAuth.getCurrentUser();
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
                        if(Validacoes.haveNetworkConnection(getContext(), rootView))
                        {
                            loadingShow("", "Validando usuário...");

                            String email = txtEmail.getText().toString().trim();
                            String senha = txtSenha.getText().toString().trim();

                            //authenticate user
                            mFirebaseAuth.signInWithEmailAndPassword(email, senha)
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            loadingHide();

                                            if (!task.isSuccessful())
                                            {
                                                //Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                Toast.makeText(LoginActivity.this, "Autenticação falhou, verifique seu e-mail e senha!", Toast.LENGTH_SHORT).show();
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
                if (Validacoes.haveNetworkConnection(getContext(), rootView))
                {
                    //alert("ATENÇÃO","Google!");
                    origem = "GOOGLE";
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                }

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
                if (Validacoes.haveNetworkConnection(getContext(), rootView))
                {
                    origem = "FACEBOOK";
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                }
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
        DatabaseReference tbUsuarios = mFirebaseDatabase.getReference("Usuarios");
        tbUsuarios.child(getUsuarioLogado().getUid()).child("acesso").setValue(getDataHora());
        tbUsuarios.child(getUsuarioLogado().getUid()).child("online").setValue("S");
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
    private void initFBFacebookLogIn() {
        mFacebookCallbackManager  = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //alert("onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
                // success
            }

            @Override
            public void onCancel() {
                // cancel
            }

            @Override
            public void onError(FacebookException error) {
                // error
            }

        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful())
                        {
                            alert("ATENÇÃO","Já existe uma conta com o mesmo endereço de e-mail, mas com diferentes credenciais de login. Faça login usando o provedor associado a este endereço de e-mail.");
                            Toast.makeText(LoginActivity.this, "A autenticação falhou.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String uid = task.getResult().getUser().getUid();
                            String nome = task.getResult().getUser().getDisplayName();
                            String email = task.getResult().getUser().getEmail();
                            Uri foto = task.getResult().getUser().getPhotoUrl();
                            //alert(uid+"\n"+nome+"\n"+email+"\n"+foto.toString());
                            //alert(uid);

                            buscaUsuario(uid, nome, email, foto.toString());

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }
    //FaceBook

    //GOOGLE
    private void initFBGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.google_web_id))
                .requestEmail()
                .build();

        Context context = getContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        alert(connectionResult.getErrorMessage());
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount token) {
        AuthCredential credential = GoogleAuthProvider.getCredential(token.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful())
                        {
                            //alert("ATENÇÃO","Erro Google: "+ task.getException().getMessage());
                            alert("ATENÇÃO","Já existe uma conta com o mesmo endereço de e-mail, mas com diferentes credenciais de login. Faça login usando o provedor associado a este endereço de e-mail.");
                            Toast.makeText(LoginActivity.this, "A autenticação falhou.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String uid = task.getResult().getUser().getUid();
                            String nome = task.getResult().getUser().getDisplayName();
                            String email = task.getResult().getUser().getEmail();
                            Uri foto = task.getResult().getUser().getPhotoUrl();
                            //alert(uid+"\n"+nome+"\n"+email+"\n"+foto.toString());

                            buscaUsuario(uid, nome, email, foto.toString());

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // fail
            }
        });
    }
    //GOOGLE

    //GOOGLE E FACEBOOK
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                // fail
            }
        }

        if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE)
        {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }/**/
    }
    //GOOGLE E FACEBOOK

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

        DatabaseReference tbUsuarios = mFirebaseDatabase.getReference("Usuarios");
        tbUsuarios.child(usuario.getID()).setValue(usuario);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    public void buscaUsuario(final String uid, final String nome, final String email, final String foto)
    {
        //alert(uid);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("Usuarios");

        DatabaseReference raiz = FirebaseDatabase.getInstance().getReference();
        //Query query = raiz.child("Usuarios").orderByChild("email").equalTo(email).limitToFirst(1);
        Query query = raiz.child("Usuarios").child(uid);
        //Query query = raiz.child("Usuarios").orderByChild("nome");
        //Query query = raiz.child("Usuarios").child("001");
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Passar os dados para a interface grafica
                if (dataSnapshot.exists())
                {
                    //respsotaAchouUsuario = true;
                    //alert("Achou!");
                    //alert("Achou!");
                    //alert(dataSnapshot.toString());

                    //
                    //Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    //alert(usuario.getEmail());

                    /*for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                    {
                        alert(childSnapshot.getKey()+" = "+childSnapshot.getValue());
                    }*/

                    /*for (DataSnapshot noteSnapshot: dataSnapshot.getChildren())
                    {
                        Usuario usuario1 = noteSnapshot.getValue(Usuario.class);
                        alert(usuario1.getEmail());
                    }*/
                    salvarUltimoAcesso();
                }
                else
                {
                    //alert("Não Achou!");
                    salvarNovoUsuario(uid, nome, email, foto.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //Se ocorrer um erro
                alert(databaseError.getMessage().toString());
            }
        });
    }
}