package diplrad;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockchainTest {

    public static Blockchain setUpBlockchain() throws NoSuchAlgorithmException {
        Blockchain blockchain = new Blockchain(4);
        Block genesisBlock = new Block("The is the Genesis Block.", "0", new Date().getTime());
        genesisBlock.mineBlock(blockchain.getPrefix());
        blockchain.addBlock(genesisBlock);
        Block firstBlock = new Block("The is the First Block.", genesisBlock.getHash(), new Date().getTime());
        firstBlock.mineBlock(blockchain.getPrefix());
        blockchain.addBlock(firstBlock);
        return blockchain;
    }

    @Test
    public void givenBlockchain_whenNewBlockIsMined_thenItsHashBeginsWithPrefixString() throws NoSuchAlgorithmException {
        Blockchain blockchain = setUpBlockchain();
        Block newBlock = new Block("The is a New Block.", blockchain.getLastBlock().getHash(), new Date().getTime());
        newBlock.mineBlock(blockchain.getPrefix());
        assertEquals(newBlock.getHash().substring(0, blockchain.getPrefix()), blockchain.getPrefixString());
        blockchain.addBlock(newBlock);
    }

    @Test
    public void givenBlockchain_whenValidated_thenSuccess() throws NoSuchAlgorithmException {
        Blockchain blockchain = setUpBlockchain();
        boolean validationResult = blockchain.validate();
        assertTrue(validationResult);
    }

}
