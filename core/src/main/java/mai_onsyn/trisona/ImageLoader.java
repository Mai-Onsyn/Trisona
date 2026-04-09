package mai_onsyn.trisona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static mai_onsyn.trisona.Global.USER_AGENT;

//public class ImageLoader {
//
//    private static final HttpClient client = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(10))
//            .build();
//    private static final Logger log = LoggerFactory.getLogger(ImageLoader.class);
//
//    public static BufferedImage fromURL(String url) {
//        try {
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .header("User-Agent", USER_AGENT)
//                    .build();
//
//            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
//
//            return ImageIO.read(new ByteArrayInputStream(response.body()));
//        } catch (IOException | InterruptedException | IllegalArgumentException e) {
//            log.warn("Failed to load image from URL: {}", url, e);
//            return null;
//        }
//    }
//}
