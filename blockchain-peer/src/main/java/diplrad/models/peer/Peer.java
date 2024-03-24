package diplrad.models.peer;

import java.net.InetAddress;
import java.util.UUID;

public class Peer {

    private UUID id;
    private InetAddress ipAddress;
    private int port;

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
