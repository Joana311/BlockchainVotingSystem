package diplrad;

import diplrad.blockchain.Block;
import diplrad.blockchain.BlockChain;
import diplrad.blockchain.VotingBlockChain;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VotingBlockChainTest {

    public static VotingBlockChain setUpBlockchain()  {
        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        return new VotingBlockChain(candidates);
    }

    @Test
    public void givenBlockchain_whenNotChanged_thenValidationOk() {
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(thirdBlock);
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);
    }

    @Test
    public void givenBlockchain_whenChanged_thenValidationFailed() {
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        Block thirdBlock = new Block("Candidate3", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(thirdBlock);
        firstBlock.setData("Candidate3");
        secondBlock.setData("Candidate3");
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);
    }

    @Test
    public void givenBlockchain_whenDataNotCandidate_thenValidationFailed() {
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("Candidate4", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);
    }

}
