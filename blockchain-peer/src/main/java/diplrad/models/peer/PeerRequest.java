package diplrad.models.peer;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerRequest implements Serializable {

    private String ipAddress;
    private int port;

    public PeerRequest(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

}
