package diplrad.models.blockchain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VotingBlockChainTest {

    public static VotingBlockChain setUpBlockchain()  {
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
    public void givenBlockchain_whenNotChanged_thenValidationOk() {

        // Arrange
        VotingBlockChain blockChain = setUpBlockchain();

        // Act

        // Assert
        boolean validationResult = blockChain.validate();
        assertTrue(validationResult);

    }

    @Test
    public void givenBlockchain_whenChanged_thenValidationFailed() {

        // Arrange
        BlockChain blockChain = setUpBlockchain();

        // Act
        blockChain.getBlock(1).setData("Candidate3");
        blockChain.getBlock(2).setData("Candidate3");

        // Assert
        boolean validationResult = blockChain.validate();
        assertFalse(validationResult);

    }

    @Test
    public void givenBlockchain_whenDataNotCandidate_thenValidationFailed() {

        // Arrange
        BlockChain blockChain = setUpBlockchain();

        // Act
        Block invalidDataBlock = new Block("Candidate4", blockChain.getLastBlockHash());
        blockChain.mineBlock(invalidDataBlock);

        // Assert
        boolean validationResult = blockChain.validate();
        assertFalse(validationResult);

    }

}
