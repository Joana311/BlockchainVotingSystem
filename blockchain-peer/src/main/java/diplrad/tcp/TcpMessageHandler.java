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
            blockChainMessageReceived(blockchain);
            return;
        }
        Peer peer = gson.fromJson(inputLine, Peer.class);
        if (peer != null) {
            blockChainRequestMessageReceived(peer);
            return;
        }

        System.out.println("Invalid message received.");
        System.exit(1);

    }

    public void blockChainRequestMessageReceived(Peer peer) {
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

    public void blockChainMessageReceived(VotingBlockChain blockchain) {

        // we are overriding our current instance with the received one if it is valid
        // and contains exactly one block more than our current instance
        // or if it is the same length as our current instance, but the last block was added before the last block of our current instance
        if (blockchain.validate() && blockchain.validateAgainstCurrent(VotingBlockChain.getInstance())) {
            if (blockchain.size() == VotingBlockChain.getInstance().size() + 1
                || (blockchain.size() == VotingBlockChain.getInstance().size()
                    && blockchain.getTimeStampOfLastBlock() < VotingBlockChain.getInstance().getTimeStampOfLastBlock())) {
                VotingBlockChain.setInstance(blockchain);
            }
        } else {
            System.out.println("Received blockchain is invalid.");
        }

    }

}
