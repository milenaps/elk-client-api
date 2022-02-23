package com.mps.elk.sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.elk.sample.model.Pet;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private RestHighLevelClient client;
    private ObjectMapper mapper;

    public PetService(RestHighLevelClient client) {
        this.client = client;

        mapper = new ObjectMapper();
    }

    public List<Pet> list(String name, Integer age, String species) throws IOException {
        SearchRequest searchRequest = new SearchRequest("pets");

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = response.getHits().getHits();

        return Arrays.stream(searchHits)
                .map(hit -> mapper.convertValue(hit.getSourceAsString(), Pet.class))
                .collect(Collectors.toList());
    }

    public Pet save(Pet pet) throws IOException {
        String jsonObject = String.format("{\"age\":%s,\"name\":\"%s\","
                + "\"species\":\"%s\"}", pet.getAge(), pet.getName(), pet.getSpecies());
        IndexRequest request = new IndexRequest("pets");
        request.source(jsonObject, XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
        return pet;
    }

    public boolean delete(Pet pet) {
        //TODO Delete pet from ES index
        return false;
    }
}
