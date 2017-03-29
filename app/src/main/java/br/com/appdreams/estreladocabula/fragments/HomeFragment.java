package br.com.appdreams.estreladocabula.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.appdreams.estreladocabula.R;
import br.com.appdreams.estreladocabula.activity.LoginActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment
{
    //Firebase
    private Firebase mFirebase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    //Firebase

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        carregaListaDeUsuarios();
    }

    private void carregaListaDeUsuarios()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Firebase ref = new Firebase("https://<myfirebase>.firebaseio.com/users");
        Query queryRef = ref.child("Usuarios");


        DatabaseReference tbUsuarios = mFirebaseDatabase.getReference("Usuarios");
        tbUsuarios.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Toast.makeText(getActivity(), "Added",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Toast.makeText(getActivity(), "Changed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                Toast.makeText(getActivity(), "Removed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void showAdd()
    {
        Toast.makeText(getActivity(), "A autenticação falhou.",Toast.LENGTH_SHORT).show();
    }

}
