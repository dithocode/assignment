package io.ditho.assignment.model.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ContactResponse {

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("results")
        private List<Contact> results = new ArrayList<>();

        public List<Contact> getResults() {
            return results;
        }

        public void setResults(List<Contact> results) {
            this.results = results;
        }
    }

    @JsonProperty("d")
    private Data data;
}
