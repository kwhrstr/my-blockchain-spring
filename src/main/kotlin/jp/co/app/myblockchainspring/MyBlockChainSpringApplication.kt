package jp.co.app.myblockchainspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SpringBootApplication
@RestController
class MyBlockChainSpringApplication

fun main(args: Array<String>) {
    if (System.getProperty("blockchain.node.id") == null) {
        System.setProperty("blockchain.node.id", UUID.randomUUID().toString().replace("-", ""));
    }
    runApplication<MyBlockChainSpringApplication>(*args)
}
