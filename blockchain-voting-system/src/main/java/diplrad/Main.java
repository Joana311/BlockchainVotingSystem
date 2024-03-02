package diplrad;

public class Main {

    public static void main(String[] args) {
        BlockChain blockchain = new BlockChain();
        Block firstBlock = new Block("The is the First Block.", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("The is a Second Block.", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);
        System.out.println("Blockchain is valid: " + blockchain.validate());
        Block thirdBlock = new Block("The is a Second Block.", firstBlock.getHash());
        blockchain.mineBlock(thirdBlock);
        System.out.println("Blockchain is valid: " + blockchain.validate());
    }

}
