package validation

import AdProcessor
import AdRepoStub
import MkplCorSettings
import models.InnerCommand
import kotlin.test.Test

class BizValidationDeleteTest {

    private val command = InnerCommand.DELETE
    private val settings by lazy {
        MkplCorSettings(
            repoTest = AdRepoStub()
        )
    }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun correctId() = validationIdCorrect(command, processor)

    @Test
    fun trimId() = validationIdTrim(command, processor)

    @Test
    fun emptyId() = validationIdEmpty(command, processor)

    @Test
    fun badFormatId() = validationIdFormat(command, processor)

    @Test
    fun correctLock() = validationLockCorrect(command, processor)

    @Test
    fun trimLock() = validationLockTrim(command, processor)

    @Test
    fun emptyLock() = validationLockEmpty(command, processor)

    @Test
    fun badFormatLock() = validationLockFormat(command, processor)
}
