package edu.utdallas.cs.app;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.models.GeoPoint;
import com.azure.core.models.GeoPointCollection;
import com.azure.core.models.GeoPosition;
import com.azure.identity.*;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.MapsRouteClientBuilder;
import com.azure.maps.route.models.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class AirbornePollutantPathfinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirbornePollutantPathfinderApplication.class, args);
    }

}
