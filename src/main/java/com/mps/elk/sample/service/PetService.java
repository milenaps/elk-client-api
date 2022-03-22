package com.mps.elk.sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mps.elk.sample.model.Pet;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PetService {

    private static final String ES_INDEX = "pets";

    private RestHighLevelClient client;
    private ObjectMapper mapper;

    public PetService(RestHighLevelClient client) {
        this.client = client;

        mapper = new ObjectMapper();
    }

    public List<Map> list(String name, Integer age, String species) throws IOException {
        SearchHit[] hits = getSearchHits(name, age, species, true);
        if (hits == null) return null;

        return Arrays.stream(hits)
                .map(hit -> mapper.convertValue(hit.getSourceAsMap(), Map.class))
                .collect(Collectors.toList());
    }

    public Pet save(Pet pet) throws IOException {
        String jsonObject = String.format("{\"age\":%s,\"name\":\"%s\","
                + "\"species\":\"%s\"}", pet.getAge(), pet.getName(), pet.getSpecies());
        IndexRequest request = new IndexRequest(ES_INDEX);
        request.source(jsonObject, XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
        return pet;
    }

    public boolean delete(String name, Integer age, String species) throws IOException {
        SearchHit[] hits = getSearchHits(name, age, species, false);
        if (hits == null) return false;

        for (SearchHit hit : hits) {
            DeleteRequest request = new DeleteRequest(ES_INDEX, hit.getId());
            client.delete(request, RequestOptions.DEFAULT);
        }
        return true;
    }

    private SearchHit[] getSearchHits(String name, Integer age, String species, boolean isPartialSearch) throws IOException {
        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (isPartialSearch) {
            if (StringUtils.hasText(name)) {
                builder.query(QueryBuilders.termQuery("name", name));
            }
            if (age != null) {
                builder.query(QueryBuilders.termQuery("age", age));
            }
            if (StringUtils.hasText(species)) {
                builder.query(QueryBuilders.termQuery("species", species));
            }
            searchRequest.source(builder);
        } else {
            builder.query(QueryBuilders.matchQuery("name", name));
            builder.query(QueryBuilders.matchQuery("age", age));
            builder.query(QueryBuilders.matchQuery("species", species));
        }
        searchRequest.source(builder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        if (response == null || response.getHits() == null) return null;

        return response.getHits().getHits();
    }
}
