package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Volunteer extends AppCompatActivity {

    TextView name;
    Button logoutBtn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    DatabaseReference myRef;
    FirebaseDatabase database;

    EditText textForSearch;
    Button btnForSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        //logoutBtn=findViewById(R.id.button2);
        name=findViewById(R.id.welcome_tv);
        btnForSearch=findViewById(R.id.search_btn);
        textForSearch=findViewById(R.id.editText);

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
//        start working on sxc check here
        database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference().child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap value = (HashMap) snapshot.getValue();

//                inefficient method, change this later
                int flag=0;
                for(Object s: value.keySet()){
                    HashMap switchMap = (HashMap) value.get(s);
                    if(switchMap.containsValue(account.getEmail())){
                        if ((Long)switchMap.get("admin") == 1){
                            name.setText("Welcome "+switchMap.get("name"));
                            flag=1;
                        }
                    }
                }
                if (flag==0){
                    Toast.makeText(getApplicationContext(), "not a valid sxc acc", Toast.LENGTH_SHORT).show();
                    logOut();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("database error", "Failed to read value.", error.toException());
            }
        });
//        firebase assistant code for reading ends here

//        end working on sxc check here
//        logoutBtn.setOnClickListener(view -> {
//            logOut();
//        });

        btnForSearch.setOnClickListener(view -> {
//            finish();
            Intent i=new Intent(getApplicationContext(),ResultPage.class);
            i.putExtra("uid",textForSearch.getText()+"");
            startActivity(i);
        });


    }

    //menu start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //menu end
    private void logOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}