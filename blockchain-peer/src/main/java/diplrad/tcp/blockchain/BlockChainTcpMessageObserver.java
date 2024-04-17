package diplrad.tcp.blockchain;

import com.google.gson.Gson;
import diplrad.constants.ErrorMessages;
import diplrad.constants.LogMessages;
import diplrad.constants.ReceivedBlockChainResponseMessages;
import diplrad.exceptions.TcpException;
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
    public String messageReceived(String message) throws TcpException {

        Peer peer = gson.fromJson(message, Peer.class);
        if (peer != null && peer.getId() != null) {
            return blockChainRequestMessageReceived(peer, gson);
        }

        VotingBlockChain blockchain = gson.fromJson(message, VotingBlockChain.class);
        if (blockchain != null && blockchain.getBlock(0) != null) {
            return blockChainMessageReceived(blockchain);
        }

        throw new TcpException(ErrorMessages.invalidTcpMessageReceivedErrorMessage);

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
            System.out.println(LogMessages.invalidBlockChainReceivedMessage);
            return ReceivedBlockChainResponseMessages.invalidBlockChainReceivedMessage;
        }

        int currentBlockChainSize;
        if (VotingBlockChainSingleton.getInstance() == null) {
            currentBlockChainSize = 0;
        } else {
            currentBlockChainSize = VotingBlockChainSingleton.getInstance().size();
        }
        int incomingBlockChainSize = blockchain.size();

        if (currentBlockChainSize == 0 || currentBlockChainSize == 1) {
            VotingBlockChainSingleton.setInstance(blockchain);
            System.out.println(LogMessages.overrideBlockChainMessage);
            return ReceivedBlockChainResponseMessages.okMessage;
        }

        if (incomingBlockChainSize == currentBlockChainSize + 1) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), currentBlockChainSize)) {
                System.out.println(LogMessages.incompatibleBlockChainMessage);
                return ReceivedBlockChainResponseMessages.incompatibleBlockChainMessage;
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            return null;
        } else if (incomingBlockChainSize == currentBlockChainSize) {
            if (!blockchain.validateAgainstCurrent(VotingBlockChainSingleton.getInstance(), currentBlockChainSize - 1)){
                System.out.println(LogMessages.incompatibleBlockChainMessage);
                return ReceivedBlockChainResponseMessages.incompatibleBlockChainMessage;
            }
            if (blockchain.getLastBlockTimeStamp() > VotingBlockChainSingleton.getInstance().getLastBlockTimeStamp()) {
                System.out.println(LogMessages.incompatibleBlockChainLastBlockMessage);
                return ReceivedBlockChainResponseMessages.incompatibleBlockChainLastBlockMessage;
            }
            VotingBlockChainSingleton.setInstance(blockchain);
            System.out.println(LogMessages.overrideBlockChainDiscardLastBlockMessage);
            return ReceivedBlockChainResponseMessages.okMessage;
        } else if (incomingBlockChainSize < currentBlockChainSize) {
            System.out.println(LogMessages.receivedBlockChainTooSmallMessage);
            return ReceivedBlockChainResponseMessages.receivedBlockChainTooSmallMessage;
        } else {
            System.out.println(LogMessages.receivedBlockChainTooBigMessage);
            return ReceivedBlockChainResponseMessages.receivedBlockChainTooBigMessage;
        }

    }

}
