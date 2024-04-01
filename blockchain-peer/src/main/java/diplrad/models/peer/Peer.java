package diplrad.models.peer;

import java.net.InetAddress;
import java.util.UUID;

public class Peer {

    private UUID id;
    private String ipAddress;
    private int port;

    public Peer(UUID id, String ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
