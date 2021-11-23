package com.percyval.pokiapi.api;

import com.percyval.pokiapi.model.Pokemon;
import com.percyval.pokiapi.model.PokemonList;
import com.percyval.pokiapi.service.PokemonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("api/v1/pokemon")
@RestController
public class PokemonController {
    private final PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @CrossOrigin(origins = "http://pokiapi-frontend.s3-website.us-east-2.amazonaws.com")
    @GetMapping
    public PokemonList getPokemonList(@RequestParam String offset, @RequestParam String limit, HttpServletRequest request){
        return pokemonService.getPokemonList(offset, limit, request);
    }

    @CrossOrigin(origins = "http://pokiapi-frontend.s3-website.us-east-2.amazonaws.com")
    @GetMapping(path = "{name}")
    public Pokemon getPokemonByName(@PathVariable("name") String name){
        return pokemonService.getPokemonByName(name);
    }
}
