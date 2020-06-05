package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements recyclerViewClick {

    SharedPref sharedpref;
   ArrayList<pokemon> List=new ArrayList<>();
    ArrayList<pokemon> TypesList=new ArrayList<>();
    ArrayList<pokemon> RegionList=new ArrayList<>();
    ArrayList<String> Typesname=new ArrayList<>();
    RecyclerView recyclerView;
    recyclerAdapter Adapter;
    int f=1;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationViewv;



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.darkmode);
        checkable.setChecked(sharedpref.loadNightModeState());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.toolbar,menu);
        //getSupportActionBar().setLogo(R.drawable.logopoke);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("PokéDex");
        MenuItem menuItem =menu.findItem(R.id.search);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              Adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.darkmode)
        {
            sharedpref.setNightModeState(!item.isChecked());
            item.setChecked(sharedpref.loadNightModeState());
            if(item.isChecked())
            { setTheme(R.style.DarkThemeNoActionBar);
            Log.d("darkmode",Boolean.toString(sharedpref.loadNightModeState()));
            restartAct();
            }
            else{
                setTheme(R.style.AppThemeNoActionBar);
                restartAct();
        }
        }
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
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
        setContentView(R.layout.activity_mains);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationViewv=(NavigationView) findViewById(R.id.nav_view);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedpreferences = getSharedPreferences("MyP", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor =sharedpreferences.edit();
        mNavigationViewv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.types)
                {
                  f=2;
                    mNavigationViewv.setCheckedItem(R.id.types);
                    editor.putInt("f",f);
                    editor.commit();
                    restartAct();
                }
               else if(item.getItemId()==R.id.Home)
                {
                   f=1;
                    mNavigationViewv.setCheckedItem(R.id.Home);
                    editor.putInt("f",f);
                    editor.commit();
                    restartAct();

                }
                else if(item.getItemId()==R.id.location)
                {
                    f=3;
                    mNavigationViewv.setCheckedItem(R.id.location);
                    editor.putInt("f",f);
                    editor.commit();
                    restartAct();

                }



                return true;
            }
        });

        f=sharedpreferences.getInt("f",1);
        if(f==1) mNavigationViewv.setCheckedItem(R.id.Home);
        else if(f==2) mNavigationViewv.setCheckedItem(R.id.types);
        else if(f==3) mNavigationViewv.setCheckedItem(R.id.location);
        Retrofit retrofit= new Retrofit.Builder().baseUrl(pokeAPI.BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create()).build();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("fff",Integer.toString(f));

        Adapter = new recyclerAdapter(this,this,f);


          if(f==1)
          {pokeAPI api =retrofit.create(pokeAPI.class);
          Call<pokemonResults> call  = api.getPokemonResults();
              call.enqueue(new Callback<pokemonResults>() {
                  @Override
                  public void onResponse(Call<pokemonResults> call, Response<pokemonResults> response) {
                      if(response.isSuccessful()){
                          pokemonResults PokemonResults =response.body();
                          ArrayList<pokemon> pokemonResultsList= PokemonResults.getResults();
                          List=pokemonResultsList;
                          for(int i=0;i<pokemonResultsList.size();++i)
                          {String b = pokemonResultsList.get(i).getName();
                              pokemonResultsList.get(i).name=b.substring(0,1).toUpperCase()+b.substring(1);
                          }
                          Adapter.pokemonAdapter(pokemonResultsList);
                          recyclerView.setAdapter(Adapter);
                          for(int i=0;i<pokemonResultsList.size();++i)
                          {
                              pokemon p = pokemonResultsList.get(i);
                              Log.d("Tag",p.getName()+" "+p.getUrl()+" " +p.getNumber());
                          }

                      }
                      else {
                          Log.e("Tag","on res"+ response.errorBody());
                      }
                  }

                  @Override
                  public void onFailure(Call<pokemonResults> call, Throwable t) {
                      Log.e("Tag","on res"+ t);
                  }
              });
          }
          else if(f==2)
          { pokeAPI api1 =retrofit.create(pokeAPI.class);
              Call<pokemonResults> call  = api1.getType();
              call.enqueue(new Callback<pokemonResults>() {
                  @Override
                  public void onResponse(Call<pokemonResults> call, Response<pokemonResults> response) {
                      if(response.isSuccessful()){
                          pokemonResults PokemonResults =response.body();
                          ArrayList<pokemon> pokemonResultsList= PokemonResults.getResults();
                          for(int i=0;i<pokemonResultsList.size();++i)
                          {String b = pokemonResultsList.get(i).getName();
                              pokemonResultsList.get(i).name=b.substring(0,1).toUpperCase()+b.substring(1);
                          }
                          pokemonResultsList.remove(pokemonResultsList.size()-1);
                          pokemonResultsList.remove(pokemonResultsList.size()-1);
                          TypesList=pokemonResultsList;
                          Adapter.pokemonAdapter(pokemonResultsList);
                          recyclerView.setAdapter(Adapter);
                          Adapter.notifyDataSetChanged();

                      }
                      else {
                          Log.e("Tag","on res"+ response.errorBody());
                      }
                  }

                  @Override
                  public void onFailure(Call<pokemonResults> call, Throwable t) {
                      Log.e("Tag","on res"+ t);
                  }
              });}
          else if(f==3)
          {
              pokeAPI api1 =retrofit.create(pokeAPI.class);
              Call<pokemonResults> call  = api1.getLocation();
              call.enqueue(new Callback<pokemonResults>() {
                  @Override
                  public void onResponse(Call<pokemonResults> call, Response<pokemonResults> response) {
                      if(response.isSuccessful()){
                          pokemonResults PokemonResults =response.body();
                          ArrayList<pokemon> pokemonResultsList= PokemonResults.getResults();
                          for(int i=0;i<pokemonResultsList.size();++i)
                          {String b = pokemonResultsList.get(i).getName();
                              pokemonResultsList.get(i).name=b.substring(0,1).toUpperCase()+b.substring(1);
                          }
                          RegionList=pokemonResultsList;
                          Adapter.pokemonAdapter(pokemonResultsList);
                          recyclerView.setAdapter(Adapter);
                          Adapter.notifyDataSetChanged();
                          for(int i=0;i<pokemonResultsList.size();++i)
                          {
                              pokemon p = pokemonResultsList.get(i);
                              Log.d("Tag","reached");
                          }

                      }
                      else {
                          Log.e("Tag","on res"+ response.errorBody());
                      }
                  }

                  @Override
                  public void onFailure(Call<pokemonResults> call, Throwable t) {
                      Log.e("Tag","on res"+ t);
                  }
              });
          }

    }
    public void restartAct(){
        Adapter.clearApplications();
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onItemClick(int position) {
  if(f==1) {

      Intent intent = new Intent(MainActivity.this, singlepokemon.class);
    int n = List.get(position).getNumber();
    int typesnum=TypesList.size();
    String na = List.get(position).getName();
    String ur = List.get(position).getUrl();
    intent.putExtra("number", n);
    intent.putExtra("name", na);
    intent.putExtra("url", ur);
    finish();
    startActivity(intent);
}
else if(f==2)
{

    Intent intent = new Intent(MainActivity.this, TypesPokemon.class);
    int n = TypesList.indexOf(TypesList.get(position));
    String na =  TypesList.get(position).getName();
    String ur =  TypesList.get(position).getUrl();
    intent.putExtra("number", n);
    intent.putExtra("name", na);
    intent.putExtra("url", ur);
    finish();
    startActivity(intent);
}
  else if(f==3)
  {

      Intent intent = new Intent(MainActivity.this, Regionpokemon.class);
      int n =position+1;
      intent.putExtra("num", n);
      intent.putExtra("name",RegionList.get(position).getName());
      finish();
      startActivity(intent);
  }


    }

    @Override
    public void onLongerClick(int position) {

    }
}
