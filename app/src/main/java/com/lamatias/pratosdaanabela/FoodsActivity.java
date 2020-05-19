package com.lamatias.pratosdaanabela;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
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

public class FoodsActivity extends AppCompatActivity implements FoodsAdapter.ItemClickListener,
        UsersDialog.DialogClickListener, FoodsDialog.DialogClickListener,Serializable{

    private App app;
    private Food food;
    private static final String FILE = "data.dat";
    private FoodsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        app= (App) getIntent ().getSerializableExtra ("app");
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

    private void initializeAdapter(){
        recyclerView = findViewById(R.id.recyclerview_foods);
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

    @Override
    public void onItemClick(View view, int position) {
        food = adapter.getItem(position);
        DialogFragment dialog=new UsersDialog ();
        dialog.show (getSupportFragmentManager (),"dialog");
    }

    @Override
    public void onDeleteFoodClick(View view, int position) {
        food = adapter.getItem(position);
        DialogFragment dialog=new FoodsDialog ();
        dialog.show (getSupportFragmentManager (),"dialog");
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

    @Override
    public void deleteFood(View view) {
        app.deleteFood(food);
        initializeAdapter();
    }
}
