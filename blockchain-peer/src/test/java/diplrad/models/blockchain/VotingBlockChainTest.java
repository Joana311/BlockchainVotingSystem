package diplrad.models.blockchain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VotingBlockChainTest {

    public static VotingBlockChain setUpBlockchain()  {
        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        return VotingBlockChainSingleton.createInstance(candidates);
    }

    @Test
    public void givenBlockchain_whenNotChanged_thenValidationOk() {

        // Arrange
        VotingBlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(thirdBlock);

        // Act

        // Assert
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);

    }

    @Test
    public void givenBlockchain_whenChanged_thenValidationFailed() {

        // Arrange
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(thirdBlock);

        // Act
        firstBlock.setData("Candidate3");
        secondBlock.setData("Candidate3");

        // Assert
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);

    }

    @Test
    public void givenBlockchain_whenDataNotCandidate_thenValidationFailed() {

        // Arrange
        BlockChain blockchain = setUpBlockchain();

        // Act
        Block firstBlock = new Block("Candidate4", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);

        // Assert
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);

    }

}
