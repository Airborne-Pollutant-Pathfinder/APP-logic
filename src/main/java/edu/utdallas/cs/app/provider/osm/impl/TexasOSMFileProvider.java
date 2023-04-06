package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.provider.osm.OSMFileProvider;

import java.io.File;
import java.net.URISyntaxException;

public class TexasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws URISyntaxException {
        System.out.println("maps/texas");
        return new File(TexasOSMFileProvider.class.getClassLoader().getResource("maps/texas").toURI());
    }
}
