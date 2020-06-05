package com.example.pokedex;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface pokeAPI {

    String BASE_URL="https://pokeapi.co/api/v2/";
    @GET("pokemon?limit=964")
    Call<pokemonResults> getPokemonResults();
    @GET
    Call<Model> getAttr(@Url String url);
    @GET("type/")
    Call<pokemonResults> getType();
    @GET("region/")
    Call<pokemonResults> getLocation();
    @GET
    Call<pokemon_species> getGen(@Url String url);
    @GET
    Call<mainTypeResults> getPokeType(@Url String url);

}
