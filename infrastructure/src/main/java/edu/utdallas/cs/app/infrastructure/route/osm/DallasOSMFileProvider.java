package edu.utdallas.cs.app.infrastructure.route.osm;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class DallasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws IOException {
        return loadResource("maps/dallas");
    }
}
