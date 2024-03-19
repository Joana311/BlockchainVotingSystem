package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpSender {

    private final Gson gson;
    private final HttpClient client;

    public HttpSender() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.client = new SslDisabledHttpClient().getClient();
    }

    public Peer registerPeer(PeerRequest peerRequest)
    {
        try {
            String json = gson.toJson(peerRequest);
            if (json == null) {
                System.out.println("Unable to parse peer request.");
                System.exit(1);
            }
            byte[] bytes = json.getBytes();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://localhost:7063/api/peers"))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            int responseStatusCode = response.statusCode();
            if (responseStatusCode != 200) {
                System.out.println("Sending HTTP request was unsuccessful.");
                System.exit(1);
            }

            Peer peer = gson.fromJson(responseBody, Peer.class);
            if (peer == null) {
                System.out.println("Unable to parse peer.");
                System.exit(1);
            }

            return peer;
        } catch (URISyntaxException e) {
            System.out.println("Url is incorrect.");
            System.exit(1);
        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to send HTTP request.");
            System.exit(1);
        }

        return null;
    }

    public List<Peer> getPeers()
    {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://localhost:7063/api/peers"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            int responseStatusCode = response.statusCode();
            if (responseStatusCode != 200) {
                System.out.println("Sending HTTP request was unsuccessful.");
                System.exit(1);
            }

            List<Peer> peers = gson.fromJson(responseBody, List.class);
            if (peers == null) {
                System.out.println("Unable to parse peers.");
                System.exit(1);
            }

            return peers;
        } catch (URISyntaxException e) {
            System.out.println("Url is incorrect.");
            System.exit(1);
        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to send HTTP request.");
            System.exit(1);
        }

        return null;
    }

}
