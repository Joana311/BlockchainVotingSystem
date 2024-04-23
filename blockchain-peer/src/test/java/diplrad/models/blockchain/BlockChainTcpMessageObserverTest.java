package diplrad.models.blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.exceptions.TcpException;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.blockchain.BlockChainTcpMessageObserver;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public class BlockChainTcpMessageObserverTest {

    private final Gson gson = new GsonBuilder().create();
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

    @BeforeAll
    public static void setUpPeers() {
        Peer peer1 = new Peer(UUID.randomUUID(), "168.198.2.23", 5000);
        Peer peer2 = new Peer(UUID.randomUUID(), "168.198.2.24", 5000);
        Peer peer3 = new Peer(UUID.randomUUID(), "168.198.2.25", 5000);
        List<Peer> peers = List.of(peer1, peer2, peer3);
        PeersSingleton.createInstance(peers);
    }

    @Test
    public void messageReceived_whenMessageIsConnect_thenRespondWithBlockChainInstance() throws TcpException {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(setUpBlockChain());
            Peer peer = new Peer(UUID.randomUUID(), "168.182.1.11", 5000);

            var a = gson.toJson(peer);
            // Act
            observer.messageReceived(gson.toJson(peer));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.getInstance(), times(1));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainInvalid_thenDoNotSetInstance() throws TcpException {

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
    public void messageReceived_whenMessageIsBlockChainButCurrentIsEmpty_thenSetInstance() throws TcpException {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
            VotingBlockChain currentBlockChain = new VotingBlockChain(candidates);
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block firstBlockIncomingBlockChain = new Block("Candidate1", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(firstBlockIncomingBlockChain);
            Block secondBlockIncomingBlockChain = new Block("Candidate2", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(secondBlockIncomingBlockChain);
            Block thirdBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(thirdBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(1));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainBiggerAndIncompatibleWithCurrent_thenDoNotSetInstance() throws TcpException {

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
    public void messageReceived_whenMessageIsBlockChainBiggerThanCurrent_thenSetInstance() throws TcpException {

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
    public void messageReceived_whenMessageIsBlockChainOfSameSizeAndIncompatibleWithCurrent_thenDoNotSetInstance() throws TcpException {

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
            incomingBlockChain.mineBlock(fifthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainOfSameSizeAsCurrentWithBiggerLastBlockTimeStamp_thenDoNotSetInstance() throws TcpException {

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
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainOfSameSizeAsCurrentWithSmallerLastBlockTimeStamp_thenSetInstance() throws TcpException {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);

            Block fourthBlockCurrentBlockChain = new Block("Candidate1", currentBlockChain.getLastBlockHash());
            currentBlockChain.mineBlock(fourthBlockCurrentBlockChain);
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(1));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainTooSmall_thenDoNotSetInstance() throws TcpException {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            Block fourthBlockCurrentBlockChain = new Block("Candidate1", currentBlockChain.getLastBlock().getHash());
            currentBlockChain.mineBlock(fourthBlockCurrentBlockChain);
            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

    @Test
    public void messageReceived_whenMessageIsBlockChainTooBig_thenDoNotSetInstance() throws TcpException {

        try (MockedStatic<VotingBlockChainSingleton> mockedStatic = mockStatic(VotingBlockChainSingleton.class)) {

            // Arrange
            VotingBlockChain currentBlockChain = setUpBlockChain();
            VotingBlockChain incomingBlockChain = currentBlockChain.copy();

            mockedStatic.when(VotingBlockChainSingleton::getInstance).thenReturn(currentBlockChain);

            Block fourthBlockIncomingBlockChain = new Block("Candidate3", incomingBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fourthBlockIncomingBlockChain);
            Block fifthBlockIncomingBlockChain = new Block("Candidate3", currentBlockChain.getLastBlockHash());
            incomingBlockChain.mineBlock(fifthBlockIncomingBlockChain);

            // Act
            observer.messageReceived(gson.toJson(incomingBlockChain));

            // Assert
            mockedStatic.verify(() -> VotingBlockChainSingleton.setInstance(any(VotingBlockChain.class)), times(0));

        }

    }

}
