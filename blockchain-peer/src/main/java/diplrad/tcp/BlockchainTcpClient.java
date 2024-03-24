package diplrad.tcp;

import diplrad.constants.Constants;

import java.io.IOException;

public class BlockchainTcpClient extends TcpClient {

    public void sendBlockchainRequest() throws IOException {
        sendMessage(Constants.BLOCKCHAIN_REQUEST);
    }

}
