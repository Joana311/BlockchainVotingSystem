package diplrad.constants;

public class LogMessages {

    public static final String voteAddedMessage = "Vote for %s added to block chain.";
    public static final String createdBlockChainMessage = "Created a block chain: %s.";
    public static final String startedTcpServer = "TCP server is started.";
    public static final String registeredOwnPeer = "Own peer is registered.";
    public static final String receivedInitialBlockChain = "Sent block chain requests and set initial block chain: %s.";
    public static final String votingBlockChainAlreadyCreatedMessage = "Voting block chain is already created";
    public static final String listOfPeersAlreadyCreatedMessage = "List of peers is already created";
    public static final String invalidBlockChainReceivedMessage = "Received block chain is invalid.";
    public static final String overrideBlockChainMessage = "Overridden current block chain with the received instance.";
    public static final String overrideBlockChainDiscardLastBlockMessage = "Overridden current block chain with the received instance. Last block was discarded because it was added after the last block of the received instance.";
    public static final String incompatibleBlockChainMessage = "Received block chain is incompatible with the current instance.";
    public static final String incompatibleBlockChainLastBlockMessage = "Received block chain's last block was added after current instance's last block.";
    public static final String receivedBlockChainTooSmallMessage = "Received block chain is too small.";
    public static final String receivedBlockChainTooBigMessage = "Received block chain is too big.";
    public static final String deleteOwnPeer = "Own peer is deregistered.";
    public static final String unableToDeleteOwnPeer = "Unable to deregister own peer.";

}
