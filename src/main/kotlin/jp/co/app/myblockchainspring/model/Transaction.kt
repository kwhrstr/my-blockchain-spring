package jp.co.app.myblockchainspring.model

import java.io.Serializable

data class Transaction(val sender: String,
                       val recipient: String,
                       val amount: Int):Serializable
