package com.percyval.pokiapi.dto;

import com.percyval.pokiapi.model.Pokemon;
import com.percyval.pokiapi.model.PokemonList;
import com.percyval.pokiapi.model.ResultList;
import com.percyval.pokiapi.util.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository("PokemonDto")
public class PokemonRepository implements PokemonDto {
    private static final Logger logger = LoggerFactory.getLogger(PokemonRepository.class);

    @Value("${pokiapi.endpoint.pokemon}")
    private String pokemonUrl;

    private RestTemplate restTemplate;

    public PokemonRepository() {
        ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        this.restTemplate = new RestTemplate(factory);
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new LoggingInterceptor());
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        this.restTemplate.setInterceptors(interceptors);
    }

    @Override
    public Pokemon getPokemonByName(String name) {
        logger.info("Request: getPokemonByName()");
        String url = UriComponentsBuilder.fromHttpUrl(pokemonUrl)
                .path(name)
                .encode()
                .toUriString();
        try {
            ResponseEntity<Pokemon> pokemonResponseEntity = restTemplate.getForEntity(url, Pokemon.class);
            if (pokemonResponseEntity.getStatusCode() == HttpStatus.OK){
                Pokemon pokemon = pokemonResponseEntity.getBody();
                logger.info("Request Body:" + pokemon.toString());
                return pokemon;
            }

        } catch (RestClientException restClientException) {
            logger.error(restClientException.getMessage());
            logger.error(Arrays.toString(restClientException.getStackTrace()));
        }

        return null;
    }

    @Override
    public PokemonList getPokemonList(String offset, String limit, HttpServletRequest request) {
        logger.info("Request: getPokemonList()");
        PokemonList pokemonList = new PokemonList();
        String url = UriComponentsBuilder.fromHttpUrl(pokemonUrl)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .encode()
                .toUriString();

        try {
            ResponseEntity<ResultList> resultListResponseEntity = restTemplate.getForEntity(url, ResultList.class);

            if (resultListResponseEntity.getStatusCode() == HttpStatus.OK) {
                ResultList resultList = resultListResponseEntity.getBody();
                pokemonList.setPokemonList(tryGetPokemonDetails(resultList));
                pokemonList.setNext(createNextPaginationURL(request, resultList));
                pokemonList.setPrevious(createPreviousPaginationURL(request, resultList));
                return pokemonList;
            }
        } catch (RestClientException | MalformedURLException exception) {
            logger.error(exception.getMessage());
            logger.error(Arrays.toString(exception.getStackTrace()));
        }

        return null;
    }

    private String createPreviousPaginationURL(HttpServletRequest request, ResultList resultListResponse) throws MalformedURLException {
        if (resultListResponse.getPrevious() == null) {
            return null;
        }

        String previousQuery = new URL(resultListResponse.getPrevious()).getQuery();
        return request.getRequestURL() + "?" + previousQuery;
    }

    private String createNextPaginationURL(HttpServletRequest request, ResultList resultListResponse) throws MalformedURLException {
        if (resultListResponse.getNext() == null) {
            return null;
        }
        String nextQuery = new URL(resultListResponse.getNext()).getQuery();
        return request.getRequestURL() + "?" + nextQuery;
    }

    private List<Pokemon> tryGetPokemonDetails(ResultList resultListResponse) {
        List<Pokemon> pokemonList = new ArrayList<>();
        resultListResponse.getResults().forEach(pokemon -> {
            try {
                Pokemon pokemonDetails = getPokemonByName(pokemon.getName());
                if (pokemonDetails != null) {
                    pokemonList.add(
                            new Pokemon.Builder(pokemonDetails.getId())
                                    .withSprites(pokemonDetails.getSprites())
                                    .withName(pokemonDetails.getName())
                                    .withWeight(pokemonDetails.getWeight())
                                    .build());
                }
            } catch (RestClientException restClientException) {
                logger.error(restClientException.getMessage());
                logger.error(Arrays.toString(restClientException.getStackTrace()));
            }
        });

        return pokemonList;
    }
}
