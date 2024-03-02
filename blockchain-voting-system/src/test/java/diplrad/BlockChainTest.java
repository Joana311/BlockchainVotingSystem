package diplrad;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockChainTest {

    public static BlockChain setUpBlockchain()  {
        BlockChain blockchain = new BlockChain();
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        return blockchain;
    }

    @Test
    public void givenBlockchain_whenNewBlockIsMined_thenItsHashBeginsWithPrefixString() {
        BlockChain blockchain = setUpBlockchain();
        Block newBlock = new Block("The is a New Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(newBlock);
        int prefix = Constants.DIFFICULTY;
        String leadingZeros = new String(new char[prefix]).replace('\0', '0');
        assertEquals(newBlock.getHash().substring(0, prefix), leadingZeros);
    }

    @Test
    public void givenBlockchain_whenNotChanged_thenValidationOk() {
        BlockChain blockchain = setUpBlockchain();
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);
    }

    @Test
    public void givenBlockchain_whenChanged_thenValidationFailed() {
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        firstBlock.setData("The is the changed First Block.");
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);
    }

}
