package diplrad.helpers;

import com.google.gson.Gson;
import diplrad.constants.ErrorMessages;
import diplrad.exceptions.TcpException;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.blockchain.BlockChainTcpClient;

import java.io.IOException;

public class BlockChainTcpClientHelper {

    public static void createTcpClientsAndSendConnects(Gson gson, Peer ownPeer) throws TcpException {
        for (Peer peer : PeersSingleton.getInstance()) {
            createTcpClientAndSendConnect(gson, peer, ownPeer);
        }
    }

    public static void createTcpClientAndSendConnect(Gson gson, Peer peer, Peer ownPeer) throws TcpException {
        try {
            BlockChainTcpClient client = new BlockChainTcpClient();
            client.startConnection(peer.getIpAddress(), peer.getPort());
            client.sendConnect(gson, ownPeer);
            client.stopConnection();
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.sendConnectErrorMessage);
        }
    }

    public static void tryCreateTcpClientsAndSendDisconnects(Gson gson, Peer ownPeer) {
        try {
            createTcpClientsAndSendDisconnects(gson, ownPeer);
        } catch (TcpException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTcpClientsAndSendDisconnects(Gson gson, Peer ownPeer) throws TcpException {
        for (Peer peer : PeersSingleton.getInstance()) {
            createTcpClientAndSendDisconnect(gson, peer, ownPeer);
        }
    }

    public static void createTcpClientAndSendDisconnect(Gson gson, Peer peer, Peer ownPeer) throws TcpException {
        try {
            BlockChainTcpClient client = new BlockChainTcpClient();
            client.startConnection(peer.getIpAddress(), peer.getPort());
            client.sendDisconnect(gson, ownPeer);
            client.stopConnection();
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.sendDisconnectErrorMessage);
        }
    }

    public static void createTcpClientsAndSendBlockChains(Gson gson) throws TcpException {
        for (Peer peer : PeersSingleton.getInstance()) {
            createTcpClientAndSendBlockChain(gson, peer);
        }
    }

    public static void createTcpClientAndSendBlockChain(Gson gson, Peer peer) throws TcpException {
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
