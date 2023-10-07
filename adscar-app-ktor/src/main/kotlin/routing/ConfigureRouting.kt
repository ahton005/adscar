package routing // ktlint-disable filename

import AppSettings
import controllers.createAd
import controllers.deleteAd
import controllers.readAd
import controllers.searchAd
import controllers.updateAd
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Ad(appSettings: AppSettings) {
    route("ad") {
        post("create") {
            call.createAd(appSettings)
        }
        post("read") {
            call.readAd(appSettings)
        }
        post("update") {
            call.updateAd(appSettings)
        }
        post("delete") {
            call.deleteAd(appSettings)
        }
        post("search") {
            call.searchAd(appSettings)
        }
    }
}
