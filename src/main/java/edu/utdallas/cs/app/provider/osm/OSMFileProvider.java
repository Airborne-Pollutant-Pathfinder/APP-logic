package edu.utdallas.cs.app.provider.osm;

import edu.utdallas.cs.app.data.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface OSMFileProvider {
    File createOSMFile(BoundingBox boundingBox) throws IOException, URISyntaxException;
}
