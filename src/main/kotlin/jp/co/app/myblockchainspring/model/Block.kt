package jp.co.app.myblockchainspring.model

import java.io.Serializable

data class Block(val index: Int,
                 val timestamp: Long,
                 val transactions: MutableList<Transaction>,
                 val proof: Int,
                 val previousHash: String):Serializable
