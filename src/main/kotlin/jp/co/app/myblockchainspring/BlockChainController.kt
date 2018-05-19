package jp.co.app.myblockchainspring

import jp.co.app.myblockchainspring.dto.BlockChainResponseDto
import jp.co.app.myblockchainspring.model.BlockChain
import jp.co.app.myblockchainspring.model.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*


@RestController
class BlockChainController  {

    @Autowired
    lateinit var blockChain: BlockChain

    val blockChainNodeId: String = UUID.randomUUID().toString()

    @RequestMapping(value = ["/transaction"], method = [(RequestMethod.POST)])
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransaction(@RequestBody transaction: Transaction): Map<String, String> {
        blockChain.newTransaction(transaction)

        return Collections.singletonMap("message", String.format("Transaction will be added to Block {%d}", blockChain.chain.last().index))
    }

    @RequestMapping("/mine")
    fun mine(): Map<String, Any> {
        val lastBlock = blockChain.lastBlock()
        val lastProof = lastBlock.proof

        val proof = blockChain.proofOfWork(lastProof.toString())

        blockChain.newTransaction(Transaction("0", blockChainNodeId, BigDecimal.valueOf(1).toInt()))

        blockChain.addBlock(proof)
        val block = blockChain.lastBlock()
        val response = HashMap<String, Any>()
        response["message"] = "New Block Forged"
        response["index"] = block.index
        response["transactions"] = block.transactions
        response["proof"] = block.proof
        response["previous_hash"] = block.previousHash

        return response
    }

    @RequestMapping("/chain")
    fun chain(): BlockChainResponseDto {
        return BlockChainResponseDto(blockChain.chain)
    }


}