package edu.utdallas.cs.app.provider.osm;

import java.io.File;
import java.io.IOException;

public interface OSMFileProvider {
    File createOSMFile() throws IOException;
}
