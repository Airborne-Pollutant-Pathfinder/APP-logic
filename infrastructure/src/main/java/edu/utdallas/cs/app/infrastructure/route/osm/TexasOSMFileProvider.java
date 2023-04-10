package edu.utdallas.cs.app.infrastructure.route.osm;

import java.io.File;

public class TexasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() {
        return new File(TexasOSMFileProvider.class.getClassLoader().getResource("maps/texas").getFile());
    }
}
