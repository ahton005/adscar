package stubs

import models.InnerAd
import models.InnerAdId
import models.InnerAdPermissionClient
import models.InnerUserId
import models.InnerVisibility
import java.math.BigDecimal

object AdStub {
    fun get(): InnerAd = AD_CAR

    fun prepareResult(block: InnerAd.() -> Unit): InnerAd = get().apply(block)

    fun prepareSearchList(filter: String) = prepareList().filter { it.description.contains(filter, true) }

    fun prepareList() = listOf(
        AD_CAR.copy(id = InnerAdId("1"), description = "ГАЗ", title = "ГАЗ"),
        AD_CAR.copy(id = InnerAdId("2"), description = "ГАЗ", title = "ГАЗ"),
        AD_CAR.copy(id = InnerAdId("3"), description = "ГАЗ", title = "ГАЗ"),
        AD_CAR.copy(id = InnerAdId("4"), description = "ВАЗ", title = "ВАЗ"),
        AD_CAR.copy(id = InnerAdId("5"), description = "ВАЗ", title = "ВАЗ"),
        AD_CAR.copy(id = InnerAdId("6"), description = "ВАЗ", title = "ВАЗ")
    )
}

val AD_CAR: InnerAd
    get() = InnerAd(
        id = InnerAdId("777"),
        title = "Продается автомобиль",
        description = "Продаю автомобиль ВАЗ-2101. Пробег 1000000 км.",
        visibility = InnerVisibility.VISIBLE_PUBLIC,
        logos = listOf("Картинка 1, Картинка 2"),
        ownerId = InnerUserId("111"),
        price = BigDecimal("100000"),
        permissionsClient = InnerAdPermissionClient.values().toMutableSet()
    )
