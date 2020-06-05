package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Regionpokemon extends AppCompatActivity implements recyclerViewClick {
int num;
String mName;
SharedPref sharedpref;
RecyclerView mRecyclerView;
recyclerAdapter mAdapter;
ArrayList<pokemon> mList = new ArrayList<>();
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
            Intent intent = new Intent(Regionpokemon.this,MainActivity.class);
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

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Retrive();

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new recyclerAdapter(this,this,1);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(pokeAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        pokeAPI api = retrofit.create(pokeAPI.class);
        Call<pokemon_species> call =api.getGen("generation/"+num+"/");
        call.enqueue(new Callback<pokemon_species>() {
            @Override
            public void onResponse(Call<pokemon_species> call, Response<pokemon_species> response) {
                if(response.isSuccessful())
                {
                    pokemon_species pokemonSpecies = response.body();
                    ArrayList<pokemon> pokemons = pokemonSpecies.getPokemon_species();
                    for(int i=0;i<pokemons.size();++i)
                    {
                        String s = pokemons.get(i).getName();
                        pokemons.get(i).name=s.substring(0,1).toUpperCase()+s.substring(1);
                    }
                    mList=pokemons;
                    mAdapter.pokemonAdapter(pokemons);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }

            @Override
            public void onFailure(Call<pokemon_species> call, Throwable t) {

            }
        });

    }
    public void Retrive()
    {
        Intent intent = getIntent();
        num=intent.getIntExtra("num",0);
        mName=intent.getStringExtra("name");
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(Regionpokemon.this,singlepokemon.class);
        intent.putExtra("name",mList.get(position).getName());
        intent.putExtra("number",mList.get(position).getNumber());
        intent.putExtra("url",mList.get(position).getUrl());
        finish();
        startActivity(intent);

    }

    @Override
    public void onLongerClick(int position) {

    }
}
