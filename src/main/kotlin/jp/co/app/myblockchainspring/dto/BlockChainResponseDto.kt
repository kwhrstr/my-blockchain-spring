package jp.co.app.myblockchainspring.dto

import jp.co.app.myblockchainspring.model.Block
import java.io.Serializable


data class BlockChainResponseDto(val chain: List<Block>) : Serializable