package edu.utdallas.cs.app.route.infrastructure.osm;

import java.io.File;
import java.net.URISyntaxException;

public class TexasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws URISyntaxException {
        return new File(TexasOSMFileProvider.class.getClassLoader().getResource("maps/texas").toURI());
    }
}
