import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.InnerCommand
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = InnerCommand.READ
    private val processor by lazy { AdProcessor() }

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun badFormatId() = validationIdFormat(command, processor)
}
