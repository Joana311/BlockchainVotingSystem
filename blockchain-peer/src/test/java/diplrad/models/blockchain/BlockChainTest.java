package diplrad.models.blockchain;

import diplrad.constants.Constants;
import diplrad.models.blockchain.Block;
import diplrad.models.blockchain.BlockChain;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockChainTest {

    public static BlockChain setUpBlockchain()  {
        return new BlockChain();
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
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
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
