package com.mps.elk.sample;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        try {
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(HttpHost.create("localhost:9200")));
            MainResponse info = client.info(RequestOptions.DEFAULT);
            logger.info("Connected to a cluster running version {} at {}.", info.getVersion().getNumber(), "localhost:9200");
            return client;
        } catch (Exception e) {
            logger.info("No cluster is running yet at localhost:9200.");
            return null;
        }
    }
}
