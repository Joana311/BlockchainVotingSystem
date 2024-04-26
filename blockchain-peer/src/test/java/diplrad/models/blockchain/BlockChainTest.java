package diplrad.models.blockchain;

import diplrad.constants.Constants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockChainTest {

    public static BlockChain setUpBlockChain()  {
        BlockChain blockChain = new BlockChain();
        Block firstBlock = new Block("The is the First Block.", blockChain.getLastBlockHash());
        blockChain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockChain.getLastBlockHash());
        blockChain.mineBlock(secondBlock);
        return blockChain;
    }

    @Test
    public void givenBlockChain_whenNewBlockIsMined_thenItsHashBeginsWithPrefixString() {

        // Arrange
        BlockChain blockChain = setUpBlockChain();
        Block newBlock = new Block("The is a New Block.", blockChain.getLastBlockHash());
        int prefix = Constants.DIFFICULTY;
        String expected = new String(new char[prefix]).replace('\0', '0');

        // Act
        blockChain.mineBlock(newBlock);

        // Assert
        String actual = newBlock.getHash().substring(0, prefix);
        assertEquals(actual, expected);

    }

    @Test
    public void givenBlockChain_whenNotChanged_thenValidationOk() {

        // Arrange
        BlockChain blockChain = setUpBlockChain();

        // Act

        // Assert
        boolean validationResult = blockChain.validate();
        assertTrue(validationResult);

    }

    @Test
    public void givenBlockChain_whenChanged_thenValidationFailed() {

        // Arrange
        BlockChain blockChain = setUpBlockChain();

        // Act
        blockChain.getBlock(1).setData("The is the changed First Block.");

        // Assert
        boolean validationResult = blockChain.validate();
        assertFalse(validationResult);

    }

}
