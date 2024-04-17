package diplrad.tcp;

import diplrad.exceptions.TcpException;

public interface ITcpMessageObserver {
    String messageReceived(String message) throws TcpException;
}
