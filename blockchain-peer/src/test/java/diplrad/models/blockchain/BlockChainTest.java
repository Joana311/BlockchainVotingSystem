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

        // Arrange
        BlockChain blockchain = setUpBlockchain();
        Block newBlock = new Block("The is a New Block.", blockchain.getLastBlock().getHash());
        int prefix = Constants.DIFFICULTY;
        String expected = new String(new char[prefix]).replace('\0', '0');

        // Act
        blockchain.mineBlock(newBlock);

        // Assert
        String actual = newBlock.getHash().substring(0, prefix);
        assertEquals(actual, expected);

    }

    @Test
    public void givenBlockchain_whenNotChanged_thenValidationOk() {

        // Arrange
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);

        // Act

        // Assert
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);

    }

    @Test
    public void givenBlockchain_whenChanged_thenValidationFailed() {

        // Arrange
        BlockChain blockchain = setUpBlockchain();
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);

        // Act
        firstBlock.setData("The is the changed First Block.");

        // Assert
        boolean validationResult = blockchain.validate();
        assertFalse(validationResult);

    }

}
