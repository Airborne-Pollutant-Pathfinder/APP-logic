package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.data.BoundingBox;
import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;

@Component
public class StaticOSMFileProvider implements OSMFileProvider {
    @Override
    public File createOSMFile(BoundingBox boundingBox) throws URISyntaxException {
        return new File(StaticOSMFileProvider.class.getClassLoader().getResource("map").toURI());
    }
}
