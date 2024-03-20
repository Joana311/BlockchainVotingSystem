package diplrad.tcp;

import com.google.gson.Gson;
import diplrad.models.Peer;
import diplrad.models.VotingBlockChain;

import java.io.IOException;

public class TcpMessageHandler implements TcpMessageObserver {

    private final Gson gson;

    public TcpMessageHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void messageReceived(String inputLine) {

        VotingBlockChain blockchain = gson.fromJson(inputLine, VotingBlockChain.class);
        if (blockchain != null) {
            blockchainMessageReceived(blockchain);
            return;
        }
        Peer peer = gson.fromJson(inputLine, Peer.class);
        if (peer != null) {
            blockchainRequestMessageReceived(peer);
            return;
        }

        System.out.println("Invalid message received.");
        System.exit(1);

    }

    public void blockchainRequestMessageReceived(Peer peer) {
        try {
            BlockchainTcpClient client = new BlockchainTcpClient();
            client.startConnection(peer.getIpAddress().getHostAddress(), peer.getPort());
            client.sendBlockchain(VotingBlockChain.getInstance());
            client.stopConnection();
        } catch (IOException e) {
            System.out.println("Unable to start TCP client.");
            System.exit(1);
        }
    }

    public void blockchainMessageReceived(VotingBlockChain blockchain) {

        if (blockchain.validate()) {
            // TODO validate blockchain by comparing it to current instance
            // TODO must have all the same blocks 
            // TODO either has one block more than current instance
            // TODO or has the same length and the last block is different
            // TODO if the have the same length, compare the last block timestamp
            // TODO if the received blockchain is valid, replace the current instance with it
        } else {
            System.out.println("Received blockchain is invalid.");
        }

    }

}
