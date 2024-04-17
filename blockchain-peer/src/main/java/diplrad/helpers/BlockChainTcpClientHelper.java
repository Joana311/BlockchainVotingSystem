package diplrad.helpers;

import com.google.gson.Gson;
import diplrad.exceptions.TcpException;
import diplrad.models.peer.Peer;
import diplrad.tcp.blockchain.BlockChainTcpClient;

import java.io.IOException;

public class BlockChainTcpClientHelper {

    public static void CreateTcpClientAndSendBlockChainRequest(Gson gson, Peer peer, Peer ownPeer) throws TcpException {
        try {
            BlockChainTcpClient client = new BlockChainTcpClient();
            client.startConnection(peer.getIpAddress(), peer.getPort());
            client.sendBlockchainRequest(gson, ownPeer);
            client.stopConnection();
        } catch (IOException e) {
            throw new TcpException("Unable to send blockchain request to a peer");
        }
    }

}
