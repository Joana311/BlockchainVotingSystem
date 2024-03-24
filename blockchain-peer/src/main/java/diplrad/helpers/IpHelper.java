package diplrad.helpers;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IpHelper {

    public static InetAddress getOwnIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress();
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Unable to fetch own IP address.");
            System.exit(1);
        }
        return null;
    }

}
