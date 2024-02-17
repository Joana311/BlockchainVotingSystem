package diplrad;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> blocks;
    private int prefix;
    private String prefixString;

    public int getPrefix() {
        return prefix;
    }
    public String getPrefixString() {
        return prefixString;
    }

    public Blockchain(int prefix) {
        this.blocks = new ArrayList<>();
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public Block getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public boolean validate() throws NoSuchAlgorithmException {
        boolean flag;
        for (int i = 0; i < blocks.size(); i++) {
            String previousHash = i == 0 ? "0" : blocks.get(i - 1).getHash();
            flag = blocks.get(i).getHash().equals(blocks.get(i).calculateBlockHash()) // value stored in hash is actually the hash of the block
                    && previousHash.equals(blocks.get(i).getPreviousHash()) // value stored in previous hash is actually the hash of the previous block
                    && blocks.get(i).getHash().substring(0, prefix).equals(prefixString); // hash of the block begins with prefixString
            if (!flag) {
                return false;
            }
        }
        return true;
    }

}
