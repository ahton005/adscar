import groups.operation
import groups.stubs
import models.InnerAdId
import models.InnerCommand
import validation.finishAdFilterValidation
import validation.finishAdValidation
import validation.validateDescriptionHasContent
import validation.validateDescriptionNotEmpty
import validation.validateIdNotEmpty
import validation.validateIdProperFormat
import validation.validateTitleHasContent
import validation.validateTitleNotEmpty
import validation.validation
import workers.initStatus
import workers.stubCreateSuccess
import workers.stubDbError
import workers.stubDeleteSuccess
import workers.stubNoCase
import workers.stubReadSuccess
import workers.stubSearchSuccess
import workers.stubUpdateSuccess
import workers.stubValidationBadDescription
import workers.stubValidationBadId
import workers.stubValidationBadTitle

class AdProcessor(
    @Suppress("unused")
    private val corSettings: MkplCorSettings = MkplCorSettings.NONE
) {
    suspend fun exec(ctx: InnerContext) = BusinessChain.exec(ctx)

    companion object {
        private val BusinessChain = rootChain {
            initStatus("Инициализация статуса")

            operation("Создание объявления", InnerCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = InnerAdId.NONE }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishAdValidation("Завершение проверок")
                }
            }
            operation("Получить объявление", InnerCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = InnerAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Изменить объявление", InnerCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") { adValidating = adRequest.deepCopy() }
                    worker("Очистка id") { adValidating.id = InnerAdId(adValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { adValidating.title = adValidating.title.trim() }
                    worker("Очистка описания") { adValidating.description = adValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishAdValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Удалить объявление", InnerCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adValidating") {
                        adValidating = adRequest.deepCopy()
                    }
                    worker("Очистка id") { adValidating.id = InnerAdId(adValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    finishAdValidation("Успешное завершение процедуры валидации")
                }
            }
            operation("Поиск объявлений", InnerCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в adFilterValidating") { adFilterValidating = adFilterRequest.copy() }

                    finishAdFilterValidation("Успешное завершение процедуры валидации")
                }
            }
        }.build()
    }
}
