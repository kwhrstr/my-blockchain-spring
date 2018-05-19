package jp.co.app.myblockchainspring

import jp.co.app.myblockchainspring.model.BlockChain
import jp.co.app.myblockchainspring.model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
class MyBlockchainSpringApplicationTests {

	private val blockChain = BlockChain(HashHelper())

	@Test
	fun createTransaction() {
		val transaction = Transaction(UUID.randomUUID().toString(), UUID.randomUUID().toString(), BigDecimal.valueOf(1000.00).toInt())

		assertThat(blockChain.newTransaction(transaction))
	}

	@Test
	fun createBlock() {

		blockChain.addBlock("100")
		val block = blockChain.lastBlock()

		assertThat(block)
		assertThat(block.index).isEqualTo(2)
		assertThat(block.proof).isEqualTo(100L)
		assertThat(block.timestamp).isLessThan(System.currentTimeMillis())
		assertThat(block.previousHash).isNotBlank()
	}

}
