package diplrad.models.peer;

import com.google.gson.annotations.Expose;

import java.net.InetAddress;
import java.util.UUID;

public class Peer {

    @Expose
    private UUID id;
    @Expose
    private String ipAddress;
    @Expose
    private int port;

    public Peer(UUID id, String ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public UUID getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
