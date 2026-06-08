package com.skyscanner;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) {
        
        // Load rental cars and hotels from JSON files
        List<SearchResult> searchResults = new ArrayList<>();
        
        // Load rental_cars.json
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream carsStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json");
            List<SearchResult> cars = mapper.readValue(carsStream, 
                new TypeReference<List<SearchResult>>() {});
            searchResults.addAll(cars);
            System.out.println("Loaded " + cars.size() + " rental cars");
        } catch (Exception e) {
            System.out.println("Error loading rental_cars.json: " + e.getMessage());
        }
        
        // Load hotels.json
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream hotelsStream = getClass().getClassLoader().getResourceAsStream("hotels.json");
            List<SearchResult> hotels = mapper.readValue(hotelsStream,
                new TypeReference<List<SearchResult>>() {});
            searchResults.addAll(hotels);
            System.out.println("Loaded " + hotels.size() + " hotels");
        } catch (Exception e) {
            System.out.println("Error loading hotels.json: " + e.getMessage());
        }
        
        // Register the search resource
        environment.jersey().register(new SearchResource(searchResults));
        
        System.out.println("Welcome to Hoen Scanner!");
    }
}