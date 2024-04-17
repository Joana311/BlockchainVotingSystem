package diplrad.helpers;

import com.google.gson.Gson;
import diplrad.constants.ErrorMessages;
import diplrad.exceptions.TcpException;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.blockchain.BlockChainTcpClient;

import java.io.IOException;

public class BlockChainTcpClientHelper {

    public static void CreateTcpClientsAndSendBlockChainRequests(Gson gson, Peer ownPeer) throws TcpException {
        for (Peer peer : PeersSingleton.getInstance()) {
            CreateTcpClientAndSendBlockChainRequest(gson, peer, ownPeer);
        }
    }

    public static void CreateTcpClientsAndSendBlockChains(Gson gson) throws TcpException {
        for (Peer peer : PeersSingleton.getInstance()) {
            CreateTcpClientAndSendBlockChain(gson, peer);
        }
    }

    public static void CreateTcpClientAndSendBlockChainRequest(Gson gson, Peer peer, Peer ownPeer) throws TcpException {
        try {
            BlockChainTcpClient client = new BlockChainTcpClient();
            client.startConnection(peer.getIpAddress(), peer.getPort());
            client.sendBlockchainRequest(gson, ownPeer);
            client.stopConnection();
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.sendBlockChainRequestErrorMessage);
        }
    }

    public static void CreateTcpClientAndSendBlockChain(Gson gson, Peer peer) throws TcpException {
        try {
            BlockChainTcpClient client = new BlockChainTcpClient();
            client.startConnection(peer.getIpAddress(), peer.getPort());
            client.sendBlockchain(gson);
            client.stopConnection();
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.sendBlockChainErrorMessage);
        }
    }

}
