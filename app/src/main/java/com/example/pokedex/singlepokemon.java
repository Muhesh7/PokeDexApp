package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class singlepokemon extends AppCompatActivity {
    int num;
    String mName;
    String mUrl;
    int typesSize;
    TextView name,Poktype,height,weight,Poktype2;
    int colors[];

    ArrayList<String> typesname=new ArrayList<>();
    ImageView mImg;
    SharedPref sharedpref;
    type mType;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            Intent intent = new Intent(singlepokemon.this,MainActivity.class);
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
        setContentView(R.layout.activity_singlepokemon);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (TextView)findViewById(R.id.name);
        Poktype = (TextView)findViewById(R.id.types0);
        Poktype2 = (TextView)findViewById(R.id.types1);
        height = (TextView)findViewById(R.id.height);
        weight= (TextView)findViewById(R.id.weight);
        mImg=(ImageView)findViewById(R.id.imageView);
        Retrive();
        setter();
        colors=getResources().getIntArray(R.array.colors);
        Retrofit retrofit= new Retrofit.Builder().baseUrl(pokeAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        pokeAPI api =retrofit.create(pokeAPI.class);
        Call<Model> call=api.getAttr("pokemon/"+num+"/");
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                Model model;
                if(response.isSuccessful())
                {
                    model = response.body();
                   String b=model.getTypes().get(0).getType().getname();
                    height.setText(model.getHeight());
                    weight.setText(model.getWeight());
                    Log.d("vvv",b);
                    Poktype.setText(b);
                    if(model.getTypes().size()>1)
                    { String a=model.getTypes().get(1).getType().getname();
                    Poktype2.setText(a);
                    }
                    else{Poktype2.setText(null);}

                }
                else {
                    model = response.body();
                    Log.d("vvv","on res" );
                }


            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d("vvv",t.toString() );
            }
        });

    }

    public void Retrive()
    {
        Intent intent = getIntent();
        num=intent.getIntExtra("number",0);
        mName=intent.getStringExtra("name");
        mUrl=intent.getStringExtra("url");

    }
    public void setter()
    {
        name.setText(mName);
        Picasso.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+num+".png")
                .into(mImg);
    }
    public  void typesColor(String b,TextView textView)
    { String c=b;
        b=c.substring(0,1).toUpperCase()+c.substring(1);
        for(int i=0;i<typesname.size();++i)
        {
            if(b.equals(typesname.get(i)))
            {
                textView.setBackgroundColor(colors[i]);
            }
        }
        Log.d("pokk",typesname.get(1));
    }
}
