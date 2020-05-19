package com.lamatias.pratosdaanabela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import com.lamatias.pratosdaanabela.logic.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GeneratorsActivity extends AppCompatActivity {

    private App app;
    private static final String FILE = "data.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generators);
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

}
