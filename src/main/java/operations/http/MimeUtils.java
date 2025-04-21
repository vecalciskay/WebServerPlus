package operations.http;

public class MimeUtils {
    public static String getDefault() {
        return "";
    }

    public static String getMime(String extension) {
        /*
            image/apng: Animated Portable Network Graphics (APNG)
            image/avif : AV1 Image File Format (AVIF)
            image/gif: Graphics Interchange Format (GIF)
            image/jpeg: Joint Photographic Expert Group image (JPEG)
            image/png: Portable Network Graphics (PNG)
            image/svg+xml: Scalable Vector Graphics (SVG)
            image/webp: Web Picture format (WEBP)
            */
        String mime = "";
        if (extension.endsWith("png")) mime = "image/png";
        else if (extension.endsWith("gif")) mime = "image/gif";
        else if (extension.endsWith("jpg")) mime = "image/jpeg";
        else if (extension.endsWith("jpeg")) mime = "image/jpeg";
        else if (extension.endsWith("svg")) mime = "image/svg+xml";

        else if (extension.endsWith("html")) mime = "text/html";
        else if (extension.endsWith("htm")) mime = "text/html";
        else if (extension.endsWith("txt")) mime = "text/plain";
        else if (extension.endsWith("css")) mime = "text/css";
        else if (extension.endsWith("js")) mime = "text/javascript";

        return mime;
    }
}
