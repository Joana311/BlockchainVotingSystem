package diplrad.models.blockchain;

import com.google.gson.annotations.Expose;
import diplrad.constants.Constants;

import java.util.HashSet;
import java.util.List;

public class VotingBlockChain extends BlockChain {

    @Expose
    private List<String> candidates;

    VotingBlockChain(List<String> candidates) {
        super();
        this.candidates = candidates;
    }

    private VotingBlockChain(List<String> candidates, List<Block> blocks) {
        super(blocks);
        this.candidates = candidates;
    }


    public List<String> getCandidates() {
        return candidates;
    }

    @Override
    public boolean validate() {
        boolean isGenesisBlockDataCorrect = getBlocks().get(0).getData().equals(Constants.GENESIS_BLOCK_DATA);
        List<Block> blocksExceptGenesisBlock = getBlocks().subList(1, getBlocks().size());
        boolean areOtherBlockDataCorrect = new HashSet<>(candidates).containsAll(blocksExceptGenesisBlock.stream().map(Block::getData).toList());
        return isGenesisBlockDataCorrect && areOtherBlockDataCorrect && super.validate();
    }

    @Override
    public VotingBlockChain copy() {
        BlockChain baseBlockChain = super.copy();
        return new VotingBlockChain(candidates, baseBlockChain.getBlocks());
    }
}
