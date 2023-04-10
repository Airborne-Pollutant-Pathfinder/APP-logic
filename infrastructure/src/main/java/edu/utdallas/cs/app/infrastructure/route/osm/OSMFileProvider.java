package edu.utdallas.cs.app.infrastructure.route.osm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public interface OSMFileProvider {
    File getOSMFile() throws IOException;

    default File loadResource(String path) throws IOException {
        File temp = File.createTempFile("temp_map", "");
        Path tempPath = temp.toPath();
        try (InputStream inputStream = OSMFileProvider.class.getClassLoader().getResourceAsStream(path)) {
            Files.copy(inputStream, tempPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return tempPath.toFile();
    }
}
