package com.lamatias.pratosdaanabela;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.AdapterView;
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

public class FoodsActivity extends AppCompatActivity implements FoodsAdapter.ItemClickListener,
        UsersDialog.DialogClickListener, FoodsDialog.DialogClickListener, Serializable {

    private App app;
    private Food food;
    private FoodsAdapter adapter;
    private static final String FILE = "data.dat";
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        app = (App) getIntent().getSerializableExtra("app");

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.spinner_item);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_item);
        aa.add("Todos os pratos");
        Iterator<User> it = app.getUsers();
        while (it.hasNext())
            aa.add(it.next().getName());
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String user = (String) parentView.getItemAtPosition(position);
                fromSpinner(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
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

    private void initializeAdapter(String user) {
        RecyclerView recycler_view = findViewById(R.id.recycler_view);
        TextView noFood = findViewById(R.id.noFoods2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                layoutManager.getOrientation());
        recycler_view.addItemDecoration(dividerItemDecoration);
        Iterator<Food> it;
        if (user.equals("Todos os pratos")) {
            noFood.setText(R.string.noFood);
            it = app.getFoodsIterator();
        } else {
            noFood.setText(R.string.noFood2);
            it = app.getUsersFood(user);
        }
        if (!it.hasNext()) {
            noFood.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        } else {
            noFood.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
        }
        adapter = new FoodsAdapter(this, it);
        adapter.setClickListener(this);
        recycler_view.setAdapter(adapter);
    }


    public void fromSpinner(String user) {
        selectedUser = user;
        initializeAdapter(user);
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
        initializeAdapter(selectedUser);
    }

    @Override
    public void getUsers(View view) {
        TextView text = view.findViewById(R.id.infoUser);
        StringBuilder sb = new StringBuilder();
        Iterator<User> it = food.getUsers();
        while (it.hasNext())
            sb.append(it.next().getName()).append("\n");
        text.setText(sb);
    }
}
