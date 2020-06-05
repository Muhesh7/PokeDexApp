package com.example.pokedex;

import java.util.ArrayList;

public class pokemon_species {
    ArrayList<pokemon> pokemon_species;

    public pokemon_species(ArrayList<pokemon> pokemon_species) {
        this.pokemon_species = pokemon_species;
    }

    public ArrayList<pokemon> getPokemon_species() {
        return pokemon_species;
    }
}
