package com.mps.elk.sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.elk.sample.model.Pet;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private RestHighLevelClient client;
    private ObjectMapper objectMapper;

    public PetService(RestHighLevelClient client) {
        this.client = client;
        objectMapper = new ObjectMapper();
    }

    public List<Pet> list(String name, Integer age, String species) throws IOException {
//        String jsonObject = String.format("{\"age\":%s,\"name\":%s,"
//                +"\"species\":\"%s\"}", age, name, species);

        SearchRequest searchRequest = new SearchRequest("pet");

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = response.getHits().getHits();

        return
                Arrays.stream(searchHits)
                        .map(hit -> objectMapper.convertValue(hit.getSourceAsString(), Pet.class))
                        .collect(Collectors.toList());
    }

    public Pet create(Pet pet) {
        //TODO Insert pet into ES index
        return null;
    }

    public Pet update(Pet pet) {
        //TODO Update pet into ES index
        return null;
    }

    public boolean delete(Pet pet) {
        //TODO Delete pet from ES index
        return false;
    }
}
