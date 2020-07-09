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

import com.lamatias.pratosdaanabela.exceptions.NoUsersSelected;
import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.AppClass;
import com.lamatias.pratosdaanabela.logic.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GeneratorsActivity extends AppCompatActivity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generators);
        app = (App) getIntent().getSerializableExtra("app");
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
    public void onBackPressed() {
        super.onBackPressed ( );
        Intent intent = new Intent (this, MainActivity.class);
        intent.putExtra ("app", (Serializable) app);
        finish();
        startActivity (intent);
    }

    public void getIdea(View view) {
        try {
            CheckBox B = findViewById(R.id.checkBoxIB2);
            CheckBox G = findViewById(R.id.checkBoxIG2);
            CheckBox M = findViewById(R.id.checkBoxIM2);
            CheckBox PB = findViewById(R.id.checkBoxIPB2);
            CheckBox PG = findViewById(R.id.checkBoxIPG2);
            TextView idea = findViewById(R.id.t_idea);

            if (!B.isChecked() && !G.isChecked() && !M.isChecked() && !PB.isChecked() && !PG.isChecked())
                throw new NoUsersSelected();

            List<User> temp = new ArrayList<>();
            if (B.isChecked())
                temp.add(app.getUser(AppClass.B));
            if (G.isChecked())
                temp.add(app.getUser(AppClass.G));
            if (M.isChecked())
                temp.add(app.getUser(AppClass.M));
            if (PB.isChecked())
                temp.add(app.getUser(AppClass.PB));
            if (PG.isChecked())
                temp.add(app.getUser(AppClass.PG));

            idea.setText(app.getFoodByUsers(temp));

        } catch (NoUsersSelected e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
