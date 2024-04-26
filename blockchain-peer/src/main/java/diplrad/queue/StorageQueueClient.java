package diplrad.queue;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.models.QueueMessageItem;
import com.google.gson.Gson;
import diplrad.constants.Constants;
import diplrad.constants.LogMessages;
import diplrad.exceptions.TcpException;
import diplrad.tcp.blockchain.BlockChainTcpClientHelper;
import diplrad.models.blockchain.Block;
import diplrad.models.blockchain.VotingBlockChain;
import diplrad.models.blockchain.VotingBlockChainSingleton;

import java.util.List;

import static diplrad.helpers.ExceptionHandler.handleFatalException;

public class StorageQueueClient {

    private QueueClient queueClient;
    private Gson gson;

    public StorageQueueClient(Gson gson) {
        this.queueClient = new QueueClientBuilder()
                .endpoint(Constants.AZURE_STORAGE_ENDPOINT)
                .queueName(Constants.AZURE_STORAGE_QUEUE)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        this.gson = gson;
    }

    public void receiveAndHandleQueueMessage() {
        queueClient.receiveMessages(1).forEach(message -> {
            handleQueueMessage(message);
            queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
        });
    }

    private void handleQueueMessage(QueueMessageItem message) {

        String vote = message.getBody().toString();
        System.out.printf((LogMessages.voteReceivedMessage) + "%n", vote);

        if (!isVoteValid(vote)) {
            System.out.printf((LogMessages.voteInvalidMessage) + "%n", vote);
            return;
        }

        VotingBlockChain blockChain = VotingBlockChainSingleton.getInstance();
        Block block = new Block(vote, blockChain.getLastBlockHash());
        blockChain.mineBlock(block);
        System.out.printf((LogMessages.voteAddedMessage) + "%n", block.getData());

        try {
            BlockChainTcpClientHelper.createTcpClientsAndSendBlockChains(gson);
            System.out.println(LogMessages.blockChainSentMessage);
        } catch (TcpException e) {
            handleFatalException(e);
        }

    }

    private boolean isVoteValid(String vote) {
        VotingBlockChain blockChain = VotingBlockChainSingleton.getInstance();
        List<String> candidates = blockChain.getCandidates();
        return candidates.contains(vote);
    }

}
