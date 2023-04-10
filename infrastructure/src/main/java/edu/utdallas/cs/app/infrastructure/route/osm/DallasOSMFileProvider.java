package edu.utdallas.cs.app.infrastructure.route.osm;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DallasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() {
        return new File(DallasOSMFileProvider.class.getClassLoader().getResource("maps/dallas").getFile());
    }
}
