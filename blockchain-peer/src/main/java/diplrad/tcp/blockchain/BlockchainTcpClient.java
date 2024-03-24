package diplrad.tcp.blockchain;

import diplrad.constants.Constants;
import diplrad.tcp.TcpClient;

import java.io.IOException;

public class BlockchainTcpClient extends TcpClient {

    public void sendBlockchainRequest() throws IOException {
        sendMessage(Constants.BLOCKCHAIN_REQUEST);
    }

}
