package com.percyval.pokiapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    private Integer id;
    private String name;
    private Sprite sprites;
    private Integer weight;

    public static class Builder {
        private Integer id;
        private String name;
        private Sprite sprites;
        private Integer weight;

        public Builder(Integer id) {
            this.id = id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withSprites(Sprite sprites) {
            this.sprites = sprites;
            return this;
        }

        public Builder withWeight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Pokemon build() {
            Pokemon pokemon = new Pokemon();
            pokemon.id = this.id;
            pokemon.name = this.name;
            pokemon.sprites = this.sprites;
            pokemon.weight = this.weight;

            return pokemon;
        }
    }

    private Pokemon() {
    }

    @JsonProperty("id")
    public Integer getId() {
        return this.id;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("sprites")
    public Sprite getSprites() {
        return this.sprites;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "Id=" + this.id +
                ", name='" + this.name + '\'' +
                ", sprites=" + this.sprites +
                ", weight=" + this.weight +
                '}';
    }
}
