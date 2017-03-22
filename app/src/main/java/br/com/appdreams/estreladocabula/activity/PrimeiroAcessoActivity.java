package br.com.appdreams.estreladocabula.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.model.Usuario;
import br.com.appdreams.estreladocabula.utils.Validacoes;
import de.hdodenhof.circleimageview.CircleImageView;

public class PrimeiroAcessoActivity extends BaseActivity
{
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private CircleImageView imgFoto;
    private CircleProgressView circleProgressView;
    private TextInputLayout tilNome;
    private TextInputLayout tilEmail;
    private TextInputLayout tilSenha;
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private TextView txtEsqueceuSenha;
    private Button btnSalvar;

    //
    private Bitmap bitmap;
    private Uri filePath;
    private String selectedPath;
    private int PICK_IMAGE_REQUEST  = 1;
    private String internetUrl      = "";
    private String nomeFoto         = "";
    //

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeiro_acesso);

        firebaseAuth();
        firebaseDatabase();

        setUpToolbar("Criar conta");
        setHomeAsUp();

        bindActivity();

        addListenerOnImgFoto();
        addListenerOnButtonSalvar();
        addListenerOnButtonRecuperarSenha();
    }

    /* Get Firebase Auth Instance */
    protected void firebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
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

    /* Bind Objetos*/
    private void bindActivity()
    {
        imgFoto             =   (CircleImageView) findViewById(R.id.imgFoto);
        circleProgressView  =   (CircleProgressView) findViewById(R.id.circleProgressView);
        tilNome             =   (TextInputLayout) findViewById(R.id.tilNome);
        tilEmail            =   (TextInputLayout) findViewById(R.id.tilEmail);
        tilSenha            =   (TextInputLayout) findViewById(R.id.tilSenha);
        txtNome             =   (EditText) findViewById(R.id.txtNome);
        txtEmail            =   (EditText) findViewById(R.id.txtEmail);
        txtSenha            =   (EditText) findViewById(R.id.txtSenha);
        txtEsqueceuSenha    =   (TextView) findViewById(R.id.txtEsqueceuSenha);
        btnSalvar           =   (Button) findViewById(R.id.btnSalvar);
    }

    /* Eventos Botão Salvar */
    private void addListenerOnButtonSalvar()
    {
        btnSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if(validaFoto() && validaNomeCompleto() && validaEmail() && validaSenha())
                {
                    loadingShow("","Cadastrando usuário...");

                    String email        =   txtEmail.getText().toString().trim();
                    String senha        =   txtSenha.getText().toString().trim();

                    //Create User
                    mAuth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(PrimeiroAcessoActivity.this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    loadingHide();

                                    if (!task.isSuccessful())
                                    {
                                        Toast.makeText(PrimeiroAcessoActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        salvarNovoUsuario();

                                        Toast.makeText(PrimeiroAcessoActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(PrimeiroAcessoActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
                else
                {
                    alert("ATENÇÃO","Verifique os campos em vermelho!");
                }
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
                Intent intent = new Intent(PrimeiroAcessoActivity.this, RecuperarSenhaActivity.class);
                startActivity(intent);
            }
        });
    }/**/

    /* Eventos Botão Foto */
    private void addListenerOnImgFoto()
    {
        imgFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //showFileChooser();
                checkPermissionExternalStorage();
            }
        });
    }

    /* Seleciona Foto */
    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione a Foto"), PICK_IMAGE_REQUEST);
    }

    /* Get Path */
    public String getPath(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //Paulo


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            selectedPath = getPath(filePath);
            //
            File file = new File(selectedPath);
            nomeFoto = file.getName();

            uploadImage();
        }
    }

    /* Upload da Foto */
    private void uploadImage()
    {
        //if there is a file to upload
        if (selectedPath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            //progressDialog.setTitle("Uploading");
            //progressDialog.show();
            circleProgressView.setValueAnimated(0);
            circleProgressView.setVisibility(View.VISIBLE);

            FirebaseStorage storageReference = FirebaseStorage.getInstance();
            StorageReference storageRef = storageReference.getReferenceFromUrl("gs://estrela-do-cabula.appspot.com");
            StorageReference riversRef = storageRef.child("fotos/"+System.currentTimeMillis() + ".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            //progressDialog.dismiss();
                            circleProgressView.setVisibility(View.GONE);
                            circleProgressView.setValueAnimated(0);

                            Picasso.with(PrimeiroAcessoActivity.this).load(taskSnapshot.getDownloadUrl()).placeholder(R.drawable.sem_foto).into(imgFoto);

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Foto enviada com sucesso!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            //progressDialog.dismiss();
                            circleProgressView.setVisibility(View.GONE);

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            //progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            circleProgressView.setValueAnimated(((int) progress));
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    /* Valida Foto */
    private Boolean validaFoto()
    {
        Boolean resposta = false;

        if(nomeFoto.isEmpty())
        {
            resposta = false;
            imgFoto.setBorderColor(getResources().getColor(R.color.red));
        }
        else
        {
            resposta = true;
            imgFoto.setBorderColor(getResources().getColor(R.color.colorAccent));
        }

        return resposta;
    }

    /* Valida Nome */
    private Boolean validaNomeCompleto()
    {
        Boolean resposta = false;

        if(txtNome.getText().toString().isEmpty())
        {
            tilNome.setError("Nome Completo é obrigatório!");
            tilNome.setErrorEnabled(true);
            txtNome.requestFocus();
            resposta = false;
        }
        else if(!txtNome.getText().toString().contains(" "))
        {
            tilNome.setError("Nome Completo deve conter o sobrenome!");
            tilNome.setErrorEnabled(true);
            txtNome.requestFocus();
            resposta = false;
        }
        else
        {
            tilNome.setErrorEnabled(false);
            resposta = true;
        }

        return resposta;
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

    /* Salvar Usuário */
    private void salvarNovoUsuario()
    {
        String nome         =   txtNome.getText().toString();
        String email        =   txtEmail.getText().toString().trim();
        String senha        =   txtSenha.getText().toString().trim();

        Usuario usuario = new Usuario();
        usuario.setID(getUsuarioLogado().getUid());
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setFoto("");
        usuario.setTipo("M");
        usuario.setAcesso(getDataHora());
        usuario.setOrigem("LOGIN");
        usuario.setOnline("S");
        usuario.setStatus("1");

        DatabaseReference tbUsuarios = mDatabase.getReference("usuarios");
        tbUsuarios.child(usuario.getID()).setValue(usuario);
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

    private void checkPermissionExternalStorage()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else
        {
            showFileChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    showFileChooser();
                }
                break;

            default:
                break;
        }
    }

}
