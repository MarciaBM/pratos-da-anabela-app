package com.lamatias.pratosdaanabela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.AppClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private static final String FILE = "data.dat";
    private File file;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starting();
    }

    private void starting() {
        try {
            app = (App) getIntent().getSerializableExtra("app");

            file = new File(getApplicationContext().getFilesDir(), FILE);
            //file.delete ( );
            if(app != null)
                System.out.println("read from extra");
            else if (!file.exists()) {
                app = new AppClass();
            } else{
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()));
                app = (App) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy ( );
        if (isTaskRoot ( )) {
            try {
                ObjectOutputStream oout = new ObjectOutputStream (new FileOutputStream(file.getAbsolutePath ( )));
                oout.writeObject (app);
                oout.close ( );
            } catch (IOException e) {
                Toast.makeText (getApplicationContext ( ), e.getMessage ( ), Toast.LENGTH_LONG).show ( );
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause ( );
        try {
            ObjectOutputStream oout = new ObjectOutputStream (new FileOutputStream (file.getAbsolutePath ( )));
            oout.writeObject (app);
            oout.close ( );
        } catch (IOException e) {
            Toast.makeText (getApplicationContext ( ), e.getMessage ( ), Toast.LENGTH_LONG).show ( );
        }
    }

    @Override
    protected void onResume() {
        super.onResume ( );
        starting ();
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            onStop ();
        }
        else { Toast.makeText(getBaseContext(), "Double tap to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    public void insertButton(View view){
        Intent intent = new Intent(this, InsertActivity.class);
        intent.putExtra ("app", (Serializable) app);
        finish ( );
        startActivity(intent);
    }

    public void usersButton(View view){
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra ("app", (Serializable) app);
        finish ( );
        startActivity(intent);
    }

    public void foodsButton(View view){
        Intent intent = new Intent(this, FoodsActivity.class);
        intent.putExtra ("app", (Serializable) app);
        finish ( );
        startActivity(intent);
    }

    public void generatorsButton(View view){
        Intent intent = new Intent(this, GeneratorsActivity.class);
        intent.putExtra ("app", (Serializable) app);
        finish ( );
        startActivity(intent);
    }
}
