package com.percyval.pokiapi.service;

import com.percyval.pokiapi.dto.PokemonDto;
import com.percyval.pokiapi.model.Pokemon;
import com.percyval.pokiapi.model.PokemonList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PokemonService {
    private final PokemonDto pokemonDto;

    @Autowired
    public PokemonService(@Qualifier("PokemonDto") PokemonDto pokemonDto) {
        this.pokemonDto = pokemonDto;
    }

    public PokemonList getPokemonList(String offset, String limit, HttpServletRequest request){
        return pokemonDto.getPokemonList(offset, limit, request);
    }

    public Pokemon getPokemonByName(String name){
        return pokemonDto.getPokemonByName(name);
    }
}
