
//class MapperTest {
//
//    @Test
//    fun fromContext() {
//        val context = MkplContext(
//            requestId = MkplRequestId("1234"),
//            command = MkplCommand.CREATE,
//            adResponse = MkplAd(
//                title = "title",
//                description = "desc",
//                adType = MkplDealSide.DEMAND,
//                visibility = MkplVisibility.VISIBLE_PUBLIC,
//            ),
//            errors = mutableListOf(
//                MkplError(
//                    code = "err",
//                    group = "request",
//                    field = "title",
//                    message = "wrong title",
//                )
//            ),
//            state = MkplState.RUNNING,
//        )
//
//        val log = context.toLog("test-id")
//
//        assertEquals("test-id", log.logId)
//        assertEquals("ok-marketplace", log.source)
//        assertEquals("1234", log.ad?.requestId)
//        assertEquals("VISIBLE_PUBLIC", log.ad?.responseAd?.visibility)
//        val error = log.errors?.firstOrNull()
//        assertEquals("wrong title", error?.message)
//        assertEquals("ERROR", error?.level)
//    }
//}
