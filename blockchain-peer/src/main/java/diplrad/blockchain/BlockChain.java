package diplrad.blockchain;

import diplrad.Constants;
import diplrad.blockchain.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    private List<Block> blocks;

    public BlockChain() {
        this.blocks = new ArrayList<>();
        Block genesisBlock = new Block(Constants.GENESIS_BLOCK_DATA, Constants.GENESIS_BLOCK_PREVIOUS_HASH);
        mineBlock(genesisBlock);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int size() {
        return this.blocks.size();
    }

    private void addBlock(Block block) {
        blocks.add(block);
    }

    public Block getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public String getLastBlockHash() {
        return getLastBlock().getHash();
    }

    public void mineBlock(Block block) {
        while(!block.checkIfHashBeginsWithLeadingZeroes()) {
            block.incrementNonceAndRegenerateHash();
        }
        addBlock(block);
    }

    public boolean validate() {
        boolean flag;
        for (int i = 0; i < blocks.size(); i++) {
            String previousHash = i == 0 ? "0" : blocks.get(i - 1).getHash();
            flag = blocks.get(i).getHash().equals(blocks.get(i).calculateHash()) // value stored in hash is actually the hash of the block
                    && previousHash.equals(blocks.get(i).getPreviousHash()) // value stored in previous hash is actually the hash of the previous block
                    && blocks.get(i).checkIfHashBeginsWithLeadingZeroes(); // hash of the block begins with prefixString
            if (!flag) {
                return false;
            }
        }
        return true;
    }

}
