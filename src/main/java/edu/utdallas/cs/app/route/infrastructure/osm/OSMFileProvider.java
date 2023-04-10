package edu.utdallas.cs.app.route.infrastructure.osm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface OSMFileProvider {
    File getOSMFile() throws IOException, URISyntaxException;
}
