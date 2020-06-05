package com.example.pokedex;

public class mainType {
    String slot;
    pokemon pokemon;

    public mainType(String slot, pokemon type) {
        this.slot = slot;
        this.pokemon = type;
    }

    public String getSlot() {
        return slot;
    }

    public pokemon getPokemon() {
        return pokemon;
    }
}
