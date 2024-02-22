package diplrad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockChain {

    private List<Block> blocks;
    private int prefix;
    private String prefixString;

    public int getPrefix() {
        return prefix;
    }
    public String getPrefixString() {
        return prefixString;
    }

    private BlockChain(int prefix) {
        this.blocks = new ArrayList<>();
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public static BlockChain createBlockChain(int prefix) {
        BlockChain blockChain = new BlockChain(prefix);
        Block genesisBlock = new Block("The is the Genesis Block.", "0", new Date().getTime());
        genesisBlock.mineBlock(blockChain.getPrefix());
        blockChain.addBlock(genesisBlock);
        return blockChain;
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public Block getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public String getLastBlockHash() {
        return getLastBlock().getHash();
    }

    public boolean validate() {
        boolean flag;
        for (int i = 0; i < blocks.size(); i++) {
            String previousHash = i == 0 ? "0" : blocks.get(i - 1).getHash();
            flag = blocks.get(i).getHash().equals(blocks.get(i).calculateHash()) // value stored in hash is actually the hash of the block
                    && previousHash.equals(blocks.get(i).getPreviousHash()) // value stored in previous hash is actually the hash of the previous block
                    && blocks.get(i).getHash().substring(0, prefix).equals(prefixString); // hash of the block begins with prefixString
            if (!flag) {
                return false;
            }
        }
        return true;
    }

}
