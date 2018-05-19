package jp.co.app.myblockchainspring

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.hash.Hashing
import jp.co.app.myblockchainspring.model.Block
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets


@Component
class HashHelper {

    private val objectMapper = ObjectMapper()

    fun hashBlock(block: Block): String {
        return try {
            hash(objectMapper.writeValueAsString(block))
        } catch (e: JsonProcessingException) {
            "1"
        }

    }

    fun hash(content: String): String {
        return Hashing
                .sha256()
                .hashString(content, StandardCharsets.UTF_8)
                .toString()

    }

}