package diplrad.models;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerRequest implements Serializable {

    private InetAddress ipAddress;
    private int port;

    public PeerRequest(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

}
