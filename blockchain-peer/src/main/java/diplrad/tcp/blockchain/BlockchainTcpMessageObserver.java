package diplrad.tcp.blockchain;

import com.google.gson.Gson;
import diplrad.constants.Constants;
import diplrad.models.blockchain.VotingBlockChain;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.tcp.ITcpMessageObserver;

public class BlockchainTcpMessageObserver implements ITcpMessageObserver {

    private final Gson gson;

    public BlockchainTcpMessageObserver(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String messageReceived(String message) {

        if (message.equals(Constants.BLOCKCHAIN_REQUEST)) {
            return blockChainRequestMessageReceived(gson);
        }
        VotingBlockChain blockchain = gson.fromJson(message, VotingBlockChain.class);
        if (blockchain != null) {
            return blockChainMessageReceived(blockchain);
        }

        System.out.println("Invalid message received.");
        System.exit(1);
        return null;

    }

    public String blockChainRequestMessageReceived(Gson gson) {
        return gson.toJson(VotingBlockChainSingleton.getInstance());
    }

    public String blockChainMessageReceived(VotingBlockChain blockchain) {

        // we are overriding our current instance with the received one if it is valid
        // and contains exactly one block more than our current instance
        // or if it is the same length as our current instance, but the last block was added before the last block of our current instance
        if (blockchain.validate() && blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance())) {
            if (blockchain.size() == VotingBlockChainSingleton.getInstance().size() + 1
                || (blockchain.size() == VotingBlockChainSingleton.getInstance().size()
                    && blockchain.getLastBlockTimeStamp() < VotingBlockChainSingleton.getInstance().getLastBlockTimeStamp())) {
                VotingBlockChainSingleton.setInstance(blockchain);
            }
        } else {
            System.out.println("Received blockchain is invalid.");
        }

        return null;

    }

}
