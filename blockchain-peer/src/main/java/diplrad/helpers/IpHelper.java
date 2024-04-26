package diplrad.helpers;

import diplrad.constants.ErrorMessages;
import diplrad.exceptions.IpException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IpHelper {

    public static InetAddress getOwnIpAddress() throws IpException {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new IpException(ErrorMessages.getOwnIpAddressErrorMessage);
        }
    }

}
