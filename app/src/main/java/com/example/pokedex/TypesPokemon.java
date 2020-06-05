package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TypesPokemon extends AppCompatActivity implements recyclerViewClick {
    String mName;
    String mUrl;
    TextView name;
    ImageView mImg;
    SharedPref sharedpref;
    RecyclerView mRecyclerView;
    ArrayList<mainType> List = new ArrayList<>();
    TypesAdapter mAdapter;
    type mType;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            Intent intent = new Intent(TypesPokemon.this,MainActivity.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref = new SharedPref(this);
        if(sharedpref.loadNightModeState()==true) {
            setTheme(R.style.DarkThemeNoActionBar);
        }
        else  setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_pokemon);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Retrive();

       mRecyclerView=findViewById(R.id.recyclerview);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       mAdapter= new TypesAdapter(this,this);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(mUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();

        pokeAPI api =  retrofit.create(pokeAPI.class);
        Call<mainTypeResults> call = api.getPokeType("");
        call.enqueue(new Callback<mainTypeResults>() {
            @Override
            public void onResponse(Call<mainTypeResults> call, Response<mainTypeResults> response) {
                if (response.isSuccessful())
                { mainTypeResults results= response.body();
                    ArrayList<mainType> mainTypes = results.getResults();
                    for(int i=0;i<mainTypes.size();++i)
                    {String b =mainTypes.get(i).getPokemon().getName();
                        mainTypes.get(i).getPokemon().name=b.substring(0,1).toUpperCase()+b.substring(1);
                    }
                    List=mainTypes;
                    mAdapter.pokemonAdapter(mainTypes);
                    mRecyclerView.setAdapter(mAdapter);
                    Log.d("pokedexaa","success");

                }
                else{ Log.d("pokedexaa","failure");}
            }

            @Override
            public void onFailure(Call<mainTypeResults> call, Throwable t) {

            }
        });


    }
    public void Retrive(){
        Intent intent = getIntent();
        mName=intent.getStringExtra("name");
        mUrl=intent.getStringExtra("url");
        Log.d("pokedexaa",mUrl);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(TypesPokemon.this, singlepokemon.class);
        int n = List.get(position).getPokemon().getNumber();
        String na = List.get(position).getPokemon().getName();
        String ur = List.get(position).getPokemon().getUrl();
        intent.putExtra("number", n);
        intent.putExtra("name", na);
        intent.putExtra("url", ur);
        finish();
        startActivity(intent);
    }

    @Override
    public void onLongerClick(int position) {

    }
}

