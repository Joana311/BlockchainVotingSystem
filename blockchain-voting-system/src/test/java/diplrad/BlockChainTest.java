package diplrad;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockChainTest {

    /*public static BlockChain setUpBlockchain()  {
        BlockChain blockchain = BlockChain.createBlockChain(4);
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash(), new Date().getTime());
        firstBlock.mineBlock(blockchain.getPrefix());
        blockchain.addBlock(firstBlock);
        return blockchain;
    }

    @Test
    public void givenBlockchain_whenNewBlockIsMined_thenItsHashBeginsWithPrefixString() {
        BlockChain blockchain = setUpBlockchain();
        Block newBlock = new Block("The is a New Block.", blockchain.getLastBlock().getHash(), new Date().getTime());
        newBlock.mineBlock(blockchain.getPrefix());
        assertEquals(newBlock.getHash().substring(0, blockchain.getPrefix()), blockchain.getPrefixString());
        blockchain.addBlock(newBlock);
    }

    @Test
    public void givenBlockchain_whenValidated_thenSuccess() {
        BlockChain blockchain = setUpBlockchain();
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);
    }*/

}
