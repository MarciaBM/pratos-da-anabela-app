package com.lamatias.pratosdaanabela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lamatias.pratosdaanabela.exceptions.FoodAlreadyExists;
import com.lamatias.pratosdaanabela.logic.App;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private App app;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);
        starting();
    }

    private void starting() {
        try {
            app = (App) getIntent().getSerializableExtra("app");

            if (app == null)
                app = db.load();
        } catch (FoodAlreadyExists foodAlreadyExists) {
            //cannot happen
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        starting();
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            onStop();
        } else {
            Toast.makeText(getBaseContext(), "Clique outra vez para sair.", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    public void insertButton(View view) {
        Intent intent = new Intent(this, InsertActivity.class);
        intent.putExtra("app", (Serializable) app);
        finish();
        startActivity(intent);
    }

    public void usersButton(View view) {
        Intent intent = new Intent(this, FoodsActivity.class);
        intent.putExtra("app", (Serializable) app);
        finish();
        startActivity(intent);
    }

    public void generatorsButton(View view) {
        Intent intent = new Intent(this, GeneratorsActivity.class);
        intent.putExtra("app", (Serializable) app);
        finish();
        startActivity(intent);
    }
}
