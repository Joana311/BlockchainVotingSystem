package diplrad.tcp.blockchain;

import com.google.gson.Gson;
import diplrad.models.blockchain.VotingBlockChain;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.ITcpMessageObserver;

public class BlockChainTcpMessageObserver implements ITcpMessageObserver {

    private final Gson gson;

    public BlockChainTcpMessageObserver(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String messageReceived(String message) {

        Peer peer = gson.fromJson(message, Peer.class);
        if (peer != null && peer.getId() != null) {
            return blockChainRequestMessageReceived(peer, gson);
        }

        VotingBlockChain blockchain = gson.fromJson(message, VotingBlockChain.class);
        if (blockchain != null && blockchain.getBlock(0) != null) {
            return blockChainMessageReceived(blockchain);
        }

        System.out.println("Invalid message received.");
        System.exit(1);
        return null;

    }

    public String blockChainRequestMessageReceived(Peer peer, Gson gson) {
        PeersSingleton.addPeer(peer);
        return gson.toJson(VotingBlockChainSingleton.getInstance());
    }

    public String blockChainMessageReceived(VotingBlockChain blockchain) {

        // we are overriding our current instance with the received one if it is valid
        // and contains exactly one block more than our current instance
        // or if it is the same length as our current instance, but the last block was added before the last block of our current instance

        if (!blockchain.validate()) {
            System.out.println("Received blockchain is invalid.");
            return "Received blockchain is invalid.";
        }

        int currentBlockChainSize;
        if (VotingBlockChainSingleton.getInstance() == null) {
            currentBlockChainSize = 0;
        } else {
            currentBlockChainSize = VotingBlockChainSingleton.getInstance().size();
        }
        int incomingBlockChainSize = blockchain.size();

        if (currentBlockChainSize == 1) {
            VotingBlockChainSingleton.setInstance(blockchain);
            return "OK";
        }

        if (incomingBlockChainSize == currentBlockChainSize + 1) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), currentBlockChainSize)) {
                System.out.println("Received blockchain is incompatible with the current instance.");
                return "Received blockchain is incompatible with the current instance.";
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            return null;
        } else if (incomingBlockChainSize == currentBlockChainSize) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), currentBlockChainSize - 1)){
                System.out.println("Received blockchain is incompatible with the current instance.");
                return "Received blockchain is incompatible with the current instance.";
            }
            if (blockchain.getLastBlockTimeStamp() > VotingBlockChainSingleton.getInstance().getLastBlockTimeStamp()) {
                System.out.println("Received blockchain's last block was added after current instance's last block.");
                return "Received blockchain's last block was added after current instance's last block.";
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            return "OK";
        } else if (incomingBlockChainSize < currentBlockChainSize) {
            System.out.println("Received blockchain is too small.");
            return "Received blockchain is too small.";
        } else {
            System.out.println("Received blockchain is too big.");
            return "Received blockchain is too big.";
        }

    }

}
