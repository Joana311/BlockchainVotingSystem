package diplrad.models.blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.tcp.blockchain.BlockChainTcpMessageObserver;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;


public class BlockChainTcpMessageObserverTest {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private final BlockChainTcpMessageObserver observer = new BlockChainTcpMessageObserver(gson);

    public VotingBlockChain setUpBlockChain()  {
        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        VotingBlockChain blockChain = new VotingBlockChain(candidates);
        Block firstBlock = new Block("Candidate1", blockChain.getLastBlockHash());
        blockChain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockChain.getLastBlock().getHash());
        blockChain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockChain.getLastBlock().getHash());
        blockChain.mineBlock(thirdBlock);
        return blockChain;
    }

    @Test
    public void messageReceived_whenMessageIsBlockChainRequest_thenRespondWithBlockChainInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(setUpBlockChain());

            // Act
            observer.messageReceived(Constants.BLOCKCHAIN_REQUEST);

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.getInstance(), times(1));

        }

    }

    @Test
    public void messageReceived_whenMessageIsInvalidBlockChain_thenDoNothing() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain invalidBlockChain = setUpBlockChain();
            invalidBlockChain.getBlock(1).setData("Candidate3");
            invalidBlockChain.getBlock(2).setData("Candidate3");

            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(invalidBlockChain);

            // Act
            observer.messageReceived(gson.toJson(invalidBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.getInstance(), times(0));

        }

    }

}
