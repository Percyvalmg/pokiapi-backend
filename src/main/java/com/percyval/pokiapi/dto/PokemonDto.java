package com.percyval.pokiapi.dto;

import com.percyval.pokiapi.model.Pokemon;
import com.percyval.pokiapi.model.PokemonList;

import javax.servlet.http.HttpServletRequest;

public interface PokemonDto {
    PokemonList getPokemonList(String offset, String Limit, HttpServletRequest request);
    Pokemon getPokemonByName(String name);
}
