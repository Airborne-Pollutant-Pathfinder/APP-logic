package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;

public class TexasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws URISyntaxException {
        System.out.println("texas");
        return new File(TexasOSMFileProvider.class.getClassLoader().getResource("texas").toURI());
    }
}
