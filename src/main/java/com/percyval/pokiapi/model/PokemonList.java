package com.percyval.pokiapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonList {
    private String next;
    private String previous;
    private List<Pokemon> pokemonList;

    @JsonProperty("pokemonList")
    public void setPokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @JsonProperty("next")
    public void setNext(String next) {
        this.next = next;
    }

    @JsonProperty("previous")
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @JsonProperty("pokemonList")
    public List<Pokemon> getPokemonList() {
        return pokemonList;
    }

    @JsonProperty("previous")
    public String getPrevious() {
        return previous;
    }

    @JsonProperty("next")
    public String getNext() {
        return next;
    }
}
