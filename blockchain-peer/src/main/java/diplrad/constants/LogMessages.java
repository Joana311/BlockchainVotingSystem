package diplrad.constants;

public class LogMessages {

    public static final String voteAddedMessage = "Vote for %s added to blockChain.";
    public static final String createdBlockChainMessage = "Created a blockChain: %s.";
    public static final String startedTcpServer = "TCP server is started.";
    public static final String registeredOwnPeer = "Own peer is registered.";
    public static final String receivedInitialBlockChain = "Sent blockchain requests and set initial blockchain: %s.";
    public static final String votingBlockChainAlreadyCreatedMessage = "VotingBlockChain is already created";
    public static final String listOfPeersAlreadyCreatedMessage = "List of peers is already created";
    public static final String invalidBlockChainReceivedMessage = "Received blockChain is invalid.";
    public static final String overrideBlockChainMessage = "Overridden current blockChain with the received instance.";
    public static final String overrideBlockChainDiscardLastBlockMessage = "Overridden current blockChain with the received instance. Last block was discarded because it was added after the last block of the received instance.";
    public static final String incompatibleBlockChainMessage = "Received blockChain is incompatible with the current instance.";
    public static final String incompatibleBlockChainLastBlockMessage = "Received blockChain's last block was added after current instance's last block.";
    public static final String receivedBlockChainTooSmallMessage = "Received blockChain is too small.";
    public static final String receivedBlockChainTooBigMessage = "Received blockChain is too big.";
    public static final String unableToDeleteOwnPeer = "Unable to delete own peer on shutdown.";

}
