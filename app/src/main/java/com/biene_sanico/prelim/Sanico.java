package com.biene_sanico.prelim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Sanico extends AppCompatActivity {
    Button playBtn;
    EditText getReel1, getReel2, getReel3, getBetAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanico);
    playBtn =  findViewById(R.id.btnAction);
    getReel1 = findViewById(R.id.betInput1);
    getReel2 = findViewById(R.id.betInput2);
    getReel3 = findViewById(R.id.betInput3);
    getBetAmount = findViewById(R.id.betAmount);
    playBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    });
    }

}