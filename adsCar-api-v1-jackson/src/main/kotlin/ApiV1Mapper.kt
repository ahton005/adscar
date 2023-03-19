package ru.otus.otuskotlin.marketplace.api.v1

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper

val apiV1Mapper = ObjectMapper().apply {
    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
//    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}