package com.lamatias.pratosdaanabela;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamatias.pratosdaanabela.logic.App;
import com.lamatias.pratosdaanabela.logic.Food;
import com.lamatias.pratosdaanabela.logic.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

public class UsersActivity extends AppCompatActivity implements FoodsAdapter.ItemClickListener,
        FoodsDialog.DialogClickListener, Serializable {

    private App app;
    private Food food;
    private FoodsAdapter adapter;
    private Spinner spinner;
    private static final String FILE = "data.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        app = (App) getIntent().getSerializableExtra("app");

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        aa.add("Selecione um utilizador");
        Iterator<User> it = app.getUsers();
        while (it.hasNext())
            aa.add(it.next().getName());
        spinner.setAdapter(aa);

        initializeAdapter();
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
        RecyclerView recycler_view = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                layoutManager.getOrientation());
        recycler_view.addItemDecoration(dividerItemDecoration);
        TextView noFood = findViewById(R.id.noFoods2);
        Iterator<Food> it = app.getFoodsIterator();
        if(!it.hasNext()){
            spinner.setVisibility(View.VISIBLE);
            noFood.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        } else{
            spinner.setVisibility(View.VISIBLE);
            noFood.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
        }
        adapter = new FoodsAdapter (this, it);
        adapter.setClickListener(this);
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        food = adapter.getItem(position);
        DialogFragment dialog = new UsersDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDeleteFoodClick(View view, int position) {
        food = adapter.getItem(position);
        DialogFragment dialog = new FoodsDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void deleteFood(View view) {
        app.deleteFood(food);
        initializeAdapter();
    }
}
