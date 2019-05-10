package com.example.baymax.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Controll extends AppCompatActivity {
    Button btnOnLight,btnOffLight,btnOnFan,btnOffFan;
    ImageView imgFan,imgLight;
    DatabaseReference myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        addControlls();
        addEvents();
    }

    private void addEvents() {
        myData= FirebaseDatabase.getInstance().getReference();
        myData.child("light").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model temp=dataSnapshot.getValue(Model.class);
                if(temp.value.contains("on")){
                    imgLight.setImageResource(R.drawable.on);
                }else{
                    imgLight.setImageResource(R.drawable.off);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myData.child("fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model temp=dataSnapshot.getValue(Model.class);
                if(temp.value.contains("on")){
                    imgFan.setImageResource(R.drawable.on);
                }else{
                    imgFan.setImageResource(R.drawable.off);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnOnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model lightOn=new Model("on");
                myData.child("light").setValue(lightOn, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            Toast.makeText(Controll.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnOffLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model lightOff=new Model("off");
                myData.child("light").setValue(lightOff, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            Toast.makeText(Controll.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        btnOnFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model lightOn=new Model("on");
                myData.child("fan").setValue(lightOn, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            Toast.makeText(Controll.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnOffFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model lightOff=new Model("off");
                myData.child("fan").setValue(lightOff, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            Toast.makeText(Controll.this,"Thanh cong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void addControlls() {
        btnOnLight=findViewById(R.id.btnOnLight);
        btnOffLight=findViewById(R.id.btnOffLight);
        btnOnFan=findViewById(R.id.btnOnFan);
        btnOffFan=findViewById(R.id.btnOffFan);
        imgFan=findViewById(R.id.staFan);
        imgLight=findViewById(R.id.staLight);
    }
}
