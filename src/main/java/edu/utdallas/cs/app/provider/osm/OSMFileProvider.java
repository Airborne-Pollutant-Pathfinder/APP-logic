package edu.utdallas.cs.app.provider.osm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface OSMFileProvider {
    File createOSMFile(double[] boundingBox) throws IOException, URISyntaxException;
}
