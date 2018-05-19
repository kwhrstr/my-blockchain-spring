package jp.co.app.myblockchainspring

import jp.co.app.myblockchainspring.model.BlockChain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.net.MalformedURLException
import java.net.URL
import java.util.*


@RestController
@RequestMapping("/nodes")
class NodeController {

    @Autowired
    lateinit var  blockChain: BlockChain

    @RequestMapping(value = ["/register"], method = [(RequestMethod.POST)])
    fun registerNodes(@RequestBody nodes: String): ResponseEntity<Any> {
        if ( nodes.isEmpty()) {
            return ResponseEntity("Error: Please supply a valid list of nodes", HttpStatus.BAD_REQUEST)
        }

        try {
            blockChain.registerNode(URL(nodes))
        } catch (e: MalformedURLException) {
            return ResponseEntity("Error: Invalid node $nodes, Please supply a valid node", HttpStatus.BAD_REQUEST)

        }
        val response = HashMap<String, Any>()
        response["message"] = "New nodes have been added"
        response["total_nodes"] = blockChain.nodes

        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @RequestMapping("/resolve")
    fun consensus(): Map<String, Any> {
        val replaced = blockChain.resolveConflicts()

        val response = HashMap<String, Any>()
        response["new_chain"] = blockChain.chain
        response["message"] = if(replaced) "Our chain was replaced" else "Our chain is authoritative"
        return response
    }

}