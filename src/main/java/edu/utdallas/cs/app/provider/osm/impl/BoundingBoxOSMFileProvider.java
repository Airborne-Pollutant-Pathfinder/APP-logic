package edu.utdallas.cs.app.provider.osm.impl;

import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.file.Files;

/**
 * Downloads the latest data from OpenStreetMap's Overpass API from a bounding box and saves it to a temp file.
 */
public class BoundingBoxOSMFileProvider implements OSMFileProvider {
    private final double minLatitude;
    private final double minLongitude;
    private final double maxLatitude;
    private final double maxLongitude;

    public BoundingBoxOSMFileProvider(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }

    @Override
    public File createOSMFile() throws IOException {
        String url = "http://overpass-api.de/api/map?bbox=" + minLongitude + "," + minLatitude + "," + maxLongitude + "," + maxLatitude;
        System.out.println(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        byte[] osmDataBytes = EntityUtils.toByteArray(response.getEntity());
        InputStream stream = new ByteArrayInputStream(osmDataBytes);

        File tempFile = Files.createTempFile("osm_data_", ".osm").toFile();
        tempFile.deleteOnExit();

        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
