package jp.co.app.myblockchainspring.model

import jp.co.app.myblockchainspring.HashHelper
import jp.co.app.myblockchainspring.dto.BlockChainResponseDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.io.Serializable
import java.net.URL
import java.util.*
import kotlin.collections.HashSet
@Component
class BlockChain (val hashHelper: HashHelper ):Serializable {
    final var chain :MutableList<Block> = Collections.synchronizedList(mutableListOf())
    var currentTransactions:MutableList<Transaction> = Collections.synchronizedList( mutableListOf())
    val nodes :MutableSet<URL> = Collections.synchronizedSet(HashSet())
    val restTemplate = RestTemplate()
    init {
        val genesisBlock = Block(
                1,
                System.currentTimeMillis() / 1000,
                mutableListOf(),
                100,
                "1"
        )
        chain.add(genesisBlock)
    }

    fun lastBlock() = chain.last()

    fun addBlock(proof: String) {
        val newBlock = Block(
                chain.last().index + 1,
                System.currentTimeMillis() / 1000,
                currentTransactions,
                proof.toInt(),
                hashHelper.hashBlock(chain.last())
        )
        chain.add(newBlock)
        currentTransactions = mutableListOf()
    }

    fun registerNode(node: URL) {
            nodes.add(node)
    }

    fun newTransaction(transaction: Transaction) {
        // add a new transaction
        currentTransactions.add(transaction)
    }

    fun proofOfWork(lastProof: String): String {
        var proof = 0
        while (!validWork(lastProof, proof.toString())) {
            proof += 1
        }
        return Integer.toString(proof)
    }



    private fun validWork(lastProof: String, proof: String): Boolean {
        val hashVal = hashHelper.hash(lastProof + proof)
        return hashVal.substring(0, 4) == "0000"
    }

    fun resolveConflicts(): Boolean {
        var maxLength = chain.count()
        var currentChain = chain

        nodes.forEach {
            val response = try{
                restTemplate.getForObject<BlockChainResponseDto>(it.toString() + "/chain")
            } catch (ex:RestClientException){
                ex.stackTrace
                return@forEach
            }
            val resChain = response?.chain?: mutableListOf()
            if (resChain.size > maxLength && validChain(resChain)) {
                maxLength = resChain.size
                currentChain = resChain.toMutableList()
            }
        }

        if (maxLength > chain.count()) {
            chain = currentChain
            return true
        }
        return false
    }

    private fun validChain(chain: List<Block>):Boolean {
        chain.forEachIndexed { index, block ->
            if(index == 0) {
                return@forEachIndexed
            }
            if (block.previousHash != hashHelper.hashBlock(chain[index -1])){
                return false
            }
        }
        return true
    }
}