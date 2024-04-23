package diplrad.constants;

public class ErrorMessages {

    // TcpException
    public static final String sendConnectErrorMessage = "Unable to send connect TCP message to a peer.";
    public static final String sendDisconnectErrorMessage = "Unable to send disconnect TCP message to a peer.";
    public static final String sendBlockChainErrorMessage = "Unable to send block chain TCP message to a peer.";
    public static final String invalidTcpMessageReceivedErrorMessage = "Invalid TCP message is received.";
    public static final String cannotStartTcpServerErrorMessage = "Unable to start TCP server.";
    public static final String cannotStartTcpServerPortInUseErrorMessage = "Unable to start TCP server because port is already in use.";
    public static final String cannotStopTcpServerErrorMessage = "Unable to stop TCP server.";

    // ReadFromFileException
    public static final String readFromFileErrorMessage = "Unable to read from file.";

    // InvalidFileException
    public static final String duplicateEntriesInFileErrorMessage = "Duplicate entries found in the file.";

    // IpException
    public static final String getOwnIpAddressErrorMessage = "Unable to fetch own IP address.";

    // ParseException
    public static final String parsePeerRequestErrorMessage = "Unable to parse peer request.";
    public static final String parsePeerErrorMessage = "Unable to parse peer.";

    // HttpException
    public static final String unsuccessfulHttpRequestErrorMessage = "Sending HTTP request was unsuccessful.";
    public static final String incorrectUrlErrorMessage = "Url is incorrect.";
    public static final String sendHttpRequestErrorMessage = "Unable to send HTTP request.";

}
