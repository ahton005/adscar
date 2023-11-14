import kotlinx.coroutines.ExperimentalCoroutinesApi
import models.InnerCommand
import kotlin.test.Test

// TODO-validation-5: смотрим пример теста валидации, собранного из тестовых функций-оберток
@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = InnerCommand.CREATE
    private val processor by lazy { AdProcessor() }

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
}
