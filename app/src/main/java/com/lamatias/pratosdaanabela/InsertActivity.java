package com.lamatias.pratosdaanabela;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lamatias.pratosdaanabela.exceptions.EmptyFieldsException;
import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;
import com.lamatias.pratosdaanabela.exceptions.NoUsersSelected;
import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.AppClass;
import com.lamatias.pratosdaanabela.logic.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class InsertActivity extends AppCompatActivity implements Serializable {

    private App app;
    private static final String FILE = "data.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        app= (App) getIntent ().getSerializableExtra ("app");
    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screenOn;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        } else {
            screenOn = pm.isScreenOn();
        }
        if (!screenOn) {    //Screen off by lock or power
            Intent intent = new Intent (this, MainActivity.class);
            intent.putExtra ("app", (Serializable) app);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity (intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        File file = new File (getApplicationContext ( ).getFilesDir ( ), FILE);
        try {
            ObjectOutputStream oout = new ObjectOutputStream (new FileOutputStream(file.getAbsolutePath ( )));
            oout.writeObject (app);
            oout.close ( );
        }catch(IOException e){
            Toast.makeText (getApplicationContext ( ), e.getMessage ( ), Toast.LENGTH_LONG).show ( );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ( );
        Intent intent = new Intent (this, MainActivity.class);
        intent.putExtra ("app", (Serializable) app);
        startActivity (intent);
    }

    public void insertNewFood (View view){
        try{
            CheckBox B = findViewById(R.id.checkBoxIB);
            CheckBox G = findViewById(R.id.checkBoxIG);
            CheckBox M = findViewById(R.id.checkBoxIM);
            CheckBox PB = findViewById(R.id.checkBoxIPB);
            CheckBox PG = findViewById(R.id.checkBoxIPG);
            TextView foodTV = findViewById(R.id.newFoodText);
            String food = foodTV.getText().toString();

            if(food.equals(""))
                throw new EmptyFieldsException();

            if(!B.isChecked() && !G.isChecked() && !M.isChecked() && !PB.isChecked() && !PG.isChecked())
                throw new NoUsersSelected();

            List<User> temp = new ArrayList<>();
            if(B.isChecked())
                temp.add(app.getUser(AppClass.B));
            if(G.isChecked())
                temp.add(app.getUser(AppClass.G));
            if(M.isChecked())
                temp.add(app.getUser(AppClass.M));
            if(PB.isChecked())
                temp.add(app.getUser(AppClass.PB));
            if(PG.isChecked())
                temp.add(app.getUser(AppClass.PG));

            app.insertFood(food,temp.iterator());
            foodTV.setText("");
            Toast.makeText (getApplicationContext ( ), "Prato inserido com sucesso! :)", Toast.LENGTH_LONG).show ( );

        }catch (NoUsersSelected|EmptyFieldsException|FoodAlreadyExists e){
            Toast.makeText (getApplicationContext ( ), e.getMessage ( ), Toast.LENGTH_LONG).show ( );
        }
    }
}
