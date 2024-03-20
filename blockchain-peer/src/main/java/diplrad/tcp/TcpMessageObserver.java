package diplrad.tcp;

public interface TcpMessageObserver {
    void messageReceived(String inputLine);
}
