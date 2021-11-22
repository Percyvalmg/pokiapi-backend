package com.percyval.pokiapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultList {
    private String next;
    private String previous;
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
