package edu.utdallas.cs.app.infrastructure.route.osm;

import java.io.File;
import java.io.IOException;

public class TexasOSMFileProvider implements OSMFileProvider {
    @Override
    public File getOSMFile() throws IOException {
        return loadResource("maps/texas");
    }
}
