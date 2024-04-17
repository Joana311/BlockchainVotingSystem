package diplrad.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.ErrorMessages;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.ParseException;
import diplrad.helpers.ListSerializationHelper;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;

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

    public HttpSender() throws HttpException {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.client = new SslDisabledHttpClient().getClient();
    }

    public Peer registerPeer(PeerRequest peerRequest) throws ParseException, HttpException {
        try {
            String json = gson.toJson(peerRequest);
            if (json == null) {
                throw new ParseException(ErrorMessages.parsePeerRequestErrorMessage);
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
            if (responseStatusCode == 400) {
                throw new HttpException(ErrorMessages.unsuccessfulHttpRequestErrorMessage);
            }

            Peer peer = gson.fromJson(responseBody, Peer.class);
            if (peer == null) {
                throw new ParseException(ErrorMessages.parsePeerErrorMessage);
            }

            return peer;
        } catch (URISyntaxException e) {
            throw new HttpException(ErrorMessages.incorrectUrlErrorMessage);
        } catch (IOException | InterruptedException e) {
            throw new HttpException(ErrorMessages.sendHttpRequestErrorMessage);
        }
    }

    public List<Peer> getPeers(Peer ownPeer) throws HttpException, ParseException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://localhost:7063/api/peers"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            int responseStatusCode = response.statusCode();
            if (responseStatusCode != 200) {
                throw new HttpException("Sending HTTP request was unsuccessful.");
            }

            List<Peer> peers = ListSerializationHelper.deserializeList(responseBody, Peer.class);
            if (peers == null) {
                throw new ParseException("Unable to parse peers.");
            }

            peers = peers.stream().filter(peer -> !peer.getId().equals(ownPeer.getId())).toList();

            return peers;
        } catch (URISyntaxException e) {
            throw new HttpException("Url is incorrect.");
        } catch (IOException | InterruptedException e) {
            throw new HttpException("Unable to send HTTP request.");
        }
    }

}
