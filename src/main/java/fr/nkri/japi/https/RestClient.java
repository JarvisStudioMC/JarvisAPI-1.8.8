package fr.nkri.japi.https;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RestClient {

    private static final int TIMEOUT = 10_000;

    /* ========================================================= */
    /* ======================== ASYNC ========================== */
    /* ========================================================= */

    /**
     * Send an asynchronous POST request.
     *
     * @param targetUrl target endpoint
     * @param jsonBody  JSON body to send (can be null)
     * @param headers   optional headers (can be null)
     * @return CompletableFuture containing server response
     */
    public static CompletableFuture<String> postAsync(
            final String targetUrl,
            final String jsonBody,
            final Map<String, String> headers
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return post(targetUrl, jsonBody, headers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Send an asynchronous GET request.
     *
     * @param targetUrl target endpoint
     * @param headers   optional headers (can be null)
     * @return CompletableFuture containing server response
     */
    public static CompletableFuture<String> getAsync(
            final String targetUrl,
            final Map<String, String> headers
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return get(targetUrl, headers);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /* ========================================================= */
    /* ===================== SYNC CORE ========================= */
    /* ========================================================= */

    private static String post(
            final String targetUrl,
            final String jsonBody,
            final Map<String, String> headers
    ) throws Exception {

        HttpURLConnection connection = null;

        try {
            final URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            applyHeaders(connection, headers);

            if (jsonBody != null) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }
            }

            return readResponse(connection);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String get(
            final String targetUrl,
            final Map<String, String> headers
    ) throws Exception {

        HttpURLConnection connection = null;

        try {
            final URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.setRequestProperty("Content-Type", "application/json");

            applyHeaders(connection, headers);

            return readResponse(connection);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /* ========================================================= */
    /* ======================= HELPERS ========================= */
    /* ========================================================= */

    private static void applyHeaders(
            HttpURLConnection connection,
            Map<String, String> headers
    ) {
        if (headers == null) return;

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static String readResponse(final HttpURLConnection connection) throws Exception {
        final InputStream stream =
                connection.getResponseCode() >= 400
                        ? connection.getErrorStream()
                        : connection.getInputStream();

        if (stream == null) return "";

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
    }
}