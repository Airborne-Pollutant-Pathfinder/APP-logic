package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class StaticOSMFileProvider implements OSMFileProvider {
    @Override
    public File createOSMFile(double[] boundingBox) throws IOException, URISyntaxException {
        return new File(StaticOSMFileProvider.class.getClassLoader().getResource("map").toURI());
    }
}
