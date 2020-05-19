package com.lamatias.pratosdaanabela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.Food;
import com.lamatias.pratosdaanabela.logic.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

public class UsersActivity extends AppCompatActivity {

    private App app;
    private static final String FILE = "data.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        app = (App) getIntent().getSerializableExtra("app");

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        aa.add("");
        Iterator<User> it = app.getUsers();
        while (it.hasNext())
            aa.add(it.next().getName());
        spinner.setAdapter(aa);
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("app", (Serializable) app);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        File file = new File(getApplicationContext().getFilesDir(), FILE);
        try {
            ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath()));
            oout.writeObject(app);
            oout.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("app", (Serializable) app);
        startActivity(intent);
    }

    private void initializeAdapter(){
        RecyclerView recycler_view = findViewById(R.id.recyclerview_foods);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        TextView noFood = findViewById(R.id.noFoods);
        Iterator<Food> it = app.getFoodsIterator();
        if(!it.hasNext()){
            noFood.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else{
            noFood.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new FoodsAdapter (this, it);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

}
