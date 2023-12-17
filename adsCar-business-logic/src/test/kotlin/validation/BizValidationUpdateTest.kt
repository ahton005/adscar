package validation

import AdProcessor
import AdRepoStub
import MkplCorSettings
import models.InnerCommand
import kotlin.test.Test

class BizValidationUpdateTest {

    private val command = InnerCommand.UPDATE
    private val settings by lazy {
        MkplCorSettings(
            repoTest = AdRepoStub()
        )
    }
    private val processor by lazy { AdProcessor(settings) }

    @Test
    fun correctTitle() = validationTitleCorrect(command, processor)

    @Test
    fun trimTitle() = validationTitleTrim(command, processor)

    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)

    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)

    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)

    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)

    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

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
