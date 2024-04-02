package diplrad.tcp.blockchain;

import com.google.gson.Gson;
import diplrad.models.blockchain.VotingBlockChain;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeersInstance;
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
        PeersInstance.addPeer(peer);
        return gson.toJson(VotingBlockChainSingleton.getInstance());
    }

    public String blockChainMessageReceived(VotingBlockChain blockchain) {

        // we are overriding our current instance with the received one if it is valid
        // and contains exactly one block more than our current instance
        // or if it is the same length as our current instance, but the last block was added before the last block of our current instance

        if (!blockchain.validate()) {
            System.out.println("Received blockchain is invalid.");
            return null;
        }

        int currentBlockChainSize = VotingBlockChainSingleton.getInstance().size();
        int incomingBlockChainSize = blockchain.size();

        if (currentBlockChainSize == 1) {
            VotingBlockChainSingleton.setInstance(blockchain);
            return null;
        }

        if (incomingBlockChainSize == currentBlockChainSize + 1) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), VotingBlockChainSingleton.getInstance().size())) {
                System.out.println("Received blockchain is incompatible with the current instance.");
                return null;
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            return null;
        } else if (incomingBlockChainSize == currentBlockChainSize) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), VotingBlockChainSingleton.getInstance().size() - 1)){
                System.out.println("Received blockchain is incompatible with the current instance.");
                return null;
            }
            if (blockchain.getLastBlockTimeStamp() > VotingBlockChainSingleton.getInstance().getLastBlockTimeStamp()) {
                System.out.println("Received blockchain's last block was added after current instance's last block.");
                return null;
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            return null;
        } else if (incomingBlockChainSize < currentBlockChainSize) {
            System.out.println("Received blockchain is too small.");
            return null;
        } else {
            System.out.println("Received blockchain is too big.");
            return null;
        }

    }

}
