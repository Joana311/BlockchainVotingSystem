package diplrad.models.blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.tcp.blockchain.BlockChainTcpMessageObserver;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        Block secondBlock = new Block("Candidate2", blockChain.getLastBlockHash());
        blockChain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockChain.getLastBlockHash());
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
    public void messageReceived_whenMessageIsBlockChainInvalid_thenDoNotSetInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);
            incomingBlockChain.getBlock(1).setData("Candidate3");
            incomingBlockChain.getBlock(2).setData("Candidate3");

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainBiggerAndIncompatibleWithCurrent_thenDoNotSetInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            Block fourthBlockCurrentBlockChain = new Block("Candidate1", currentBlockChain.getLastBlock().getHash());
            currentBlockChain.mineBlock(fourthBlockCurrentBlockChain);
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);
            Block fifthBlockIncomingBlockChain = new Block("Candidate2", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fifthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainBiggerThanCurrent_thenSetInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate2", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(1));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainOfSameSizeAndIncompatibleWithCurrent_thenDoNotSetInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            Block fourthBlockCurrentBlockChain = new Block("Candidate1", currentBlockChain.getLastBlockHash());
            currentBlockChain.mineBlock(fourthBlockCurrentBlockChain);
            Block fifthBlockCurrentBlockChain = new Block("Candidate2", currentBlockChain.getLastBlockHash());
            currentBlockChain.mineBlock(fifthBlockCurrentBlockChain);
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);
            Block fifthBlockIncomingBlockChain = new Block("Candidate3", currentBlockChain.getLastBlockHash());
            currentBlockChain.mineBlock(fifthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    // TODO messageReceived_whenMessageIsBlockChainOfSameSizeAsCurrentWithSmallerLastBlockTimeStamp_thenDoNotSetInstance

    @Test
    public void messageReceived_whenMessageIsBlockChainOfSameSizeAsCurrentWithBiggerLastBlockTimeStamp_thenSetInstance() {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            Block fourthBlockCurrentBlockChain = new Block("Candidate1", currentBlockChain.getLastBlockHash());
            currentBlockChain.mineBlock(fourthBlockCurrentBlockChain);
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(1));

        }

    }

    // TODO messageReceived_whenMessageIsBlockChainTooSmall_thenDoNotSetInstance

    // TODO messageReceived_whenMessageIsBlockChainTooBig_thenDoNotSetInstance

}
