package diplrad.blockchain;

import diplrad.Constants;
import diplrad.blockchain.Block;
import diplrad.blockchain.BlockChain;

import java.util.HashSet;
import java.util.List;

public class VotingBlockChain extends BlockChain {

    private List<String> candidates;

    public VotingBlockChain(List<String> candidates) {
        super();
        this.candidates = candidates;
    }

    @Override
    public boolean validate() {
        boolean isGenesisBlockDataCorrect = getBlocks().get(0).getData().equals(Constants.GENESIS_BLOCK_DATA);
        List<Block> blocksExceptGenesisBlock = getBlocks().subList(1, getBlocks().size());
        boolean areOtherBlockDataCorrect = new HashSet<>(candidates).containsAll(blocksExceptGenesisBlock.stream().map(Block::getData).toList());
        return isGenesisBlockDataCorrect && areOtherBlockDataCorrect && super.validate();
    }

}
