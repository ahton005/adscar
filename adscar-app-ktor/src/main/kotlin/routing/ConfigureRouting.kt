import controllers.createAd
import controllers.deleteAd
import controllers.readAd
import controllers.searchAd
import controllers.updateAd
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Ad(processor: AdProcessor) {
    route("ad") {
        post("create") {
            call.createAd(processor)
        }
        post("read") {
            call.readAd(processor)
        }
        post("update") {
            call.updateAd(processor)
        }
        post("delete") {
            call.deleteAd(processor)
        }
        post("search") {
            call.searchAd(processor)
        }
    }
}
