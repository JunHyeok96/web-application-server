package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line = br.readLine();
        headerMap.put("Request-Line", line);
        log.debug("Request-Line : {}", line);
        while (!(line = br.readLine()).equals("")) {
            log.debug(line);
            String[] header = line.split(":");
            headerMap.put(header[0].trim(), header[1].trim());
        }
        return headerMap;
    }
}
