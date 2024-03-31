package diplrad.models.blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.tcp.blockchain.BlockChainTcpMessageObserver;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class BlockChainTcpMessageObserverTest {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private final BlockChainTcpMessageObserver observer = new BlockChainTcpMessageObserver(gson);

    @Test
    public void messageReceived_whenMessageIsBlockChainRequest_thenRespondWithBlockChainInstance() {

        // Arrange
        String expected = gson.toJson(VotingBlockChainSingleton.getInstance());

        // Act
        String actual = observer.messageReceived(Constants.BLOCKCHAIN_REQUEST);

        // Assert
        assertEquals(actual, expected);

    }

    @Test
    public void messageReceived_whenMessageIsInvalidBlockChain_thenDoNothing() {

        // Arrange
        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        VotingBlockChain invalidBlockChain = VotingBlockChainSingleton.createInstance(candidates);
        Block firstBlock = new Block("Candidate1", invalidBlockChain.getLastBlockHash());
        invalidBlockChain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", invalidBlockChain.getLastBlock().getHash());
        invalidBlockChain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", invalidBlockChain.getLastBlock().getHash());
        invalidBlockChain.mineBlock(thirdBlock);
        firstBlock.setData("Candidate3");
        secondBlock.setData("Candidate3");

        // Act
        String actual = observer.messageReceived(gson.toJson(invalidBlockChain));

        // Assert
        // TODO assert that set instance was not called

    }

}
