package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.provider.osm.OSMFileProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class StaticOSMFileProvider implements OSMFileProvider {
    @Override
    public File createOSMFile() throws IOException, URISyntaxException {
        return new File(StaticOSMFileProvider.class.getClassLoader().getResource("map").toURI());
    }
}
