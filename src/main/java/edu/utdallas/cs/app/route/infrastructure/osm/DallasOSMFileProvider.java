package edu.utdallas.cs.app.route.infrastructure.osm;

import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;

@Component
public class DallasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws URISyntaxException {
        return new File(DallasOSMFileProvider.class.getClassLoader().getResource("maps/dallas").toURI());
    }
}
