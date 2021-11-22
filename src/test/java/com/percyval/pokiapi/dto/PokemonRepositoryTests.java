package com.percyval.pokiapi.dto;

import static org.mockito.Mockito.*;

import com.percyval.pokiapi.model.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PokemonRepositoryTests {

    @Mock
    RestTemplate restTemplate;

    @Mock
    HttpServletRequest httpServletRequest;

    @InjectMocks
    PokemonRepository classUnderTest;

    private final String NEXT = "http://localhost:8080/api/v1/pokemon?offset=16&limit=3";
    private final String PREVIOUS = "http://localhost:8080/api/v1/pokemon?offset=16&limit=3";
    private final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private final String SPRITE_BACK = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png";
    private final String SPRITE_FRONT = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/9.png";
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(classUnderTest, "pokemonUrl", URL);
        ReflectionTestUtils.setField(classUnderTest, "restTemplate", restTemplate);
    }

    @Test
    public void getPokemonByName_ShouldUseURl_WithPokemonNameAsPath() {
        when(restTemplate.getForEntity(
                URL + "name", Pokemon.class))
                .thenReturn(new ResponseEntity<>(createPokemon(1, "name"), HttpStatus.OK));

        classUnderTest.getPokemonByName("name");

        verify(restTemplate).getForEntity(URL + "name", Pokemon.class);
    }

    @Test
    public void getPokemonByName_ShouldReturnPokemonWithCorrectFields_WhenResponseHttpStatusIsOK() {
        Pokemon pokemon = createPokemon(1, "name");
        when(restTemplate.getForEntity(
                URL + "name", Pokemon.class))
                .thenReturn(new ResponseEntity<>(pokemon, HttpStatus.OK));

        Pokemon poki = classUnderTest.getPokemonByName("name");

        Assertions.assertEquals("name", poki.getName());
        Assertions.assertEquals(1, poki.getId());
        Assertions.assertEquals(2, poki.getWeight());
        Assertions.assertEquals(SPRITE_BACK, poki.getSprites().getBack_default());
        Assertions.assertEquals(SPRITE_FRONT, poki.getSprites().getFront_default());
    }

    @Test
    public void getPokemonByName_ShouldReturnNull_WhenPokemonIsNotFound() {
        when(restTemplate.getForEntity(
                URL + "name", Pokemon.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        Pokemon poki = classUnderTest.getPokemonByName("name");

        Assertions.assertEquals(null, poki);
    }

    @Test
    public void getPokemonByName_ShouldHandleRestClientException_ThenReturnNull() {
        when(restTemplate.getForEntity(
                URL + "name", Pokemon.class))
                .thenThrow(new RestClientException("Test Error"));

        Pokemon poki = classUnderTest.getPokemonByName("name");

        Assertions.assertEquals(null, poki);
    }

    @Test
    public void getPokemonList_ShouldUseCorrectQueryParams() {
        initGetPokemonListTestConfig();

        classUnderTest.getPokemonList("0", "3", httpServletRequest);

        verify(restTemplate).getForEntity("https://pokeapi.co/api/v2/pokemon/?offset=0&limit=3", ResultList.class);
    }

    @Test
    public void getPokemonList_ShouldReturnThreePokemon_WhenLimitIsThree() {
        initGetPokemonListTestConfig();

        PokemonList pokemonListResponse = classUnderTest.getPokemonList("0", "3", httpServletRequest);

        Assertions.assertEquals(3, pokemonListResponse.getPokemonList().size());
    }


    private ResultList createResultListResponse(List<Result> resultList, String previous, String next) {
        ResultList resultListResponse = new ResultList();
        resultListResponse.setResults(resultList);
        resultListResponse.setPrevious(PREVIOUS);
        resultListResponse.setNext(NEXT);

        return resultListResponse;
    }

    private List<Result> createResultList() {
        List<Result> resultList = new ArrayList<>();
        resultList.add(createResult("blastoise"));
        resultList.add(createResult("caterpie"));
        resultList.add(createResult("metapod"));
        return resultList;
    }

    private Result createResult(String name) {
        Result result = new Result();
        result.setName(name);
        return result;
    }

    private Sprite createSprite() {
        Sprite sprite = new Sprite();
        sprite.setBack_default("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png");
        sprite.setFront_default("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/9.png");
        return sprite;
    }

    private Pokemon createPokemon(Integer Id, String name) {
        return new Pokemon.Builder(Id)
                .withName(name)
                .withWeight(2)
                .withSprites(createSprite()).build();
    }

    private void initGetPokemonListTestConfig() {
        List<Result> resultList = createResultList();
        when(restTemplate.getForEntity(
                URL + "?offset=0&limit=3", ResultList.class))
                .thenReturn(new ResponseEntity<>(createResultListResponse(resultList, PREVIOUS, NEXT), HttpStatus.OK));
        when(restTemplate.getForEntity(
                URL + "blastoise", Pokemon.class))
                .thenReturn(new ResponseEntity<>(createPokemon(1, "blastoise"), HttpStatus.OK));
        when(restTemplate.getForEntity(
                URL + "caterpie", Pokemon.class))
                .thenReturn(new ResponseEntity<>(createPokemon(2, "caterpie"), HttpStatus.OK));
        when(restTemplate.getForEntity(
                URL + "metapod", Pokemon.class))
                .thenReturn(new ResponseEntity<>(createPokemon(3, "metapod"), HttpStatus.OK));
    }
}
