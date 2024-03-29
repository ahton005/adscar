package ru.zyablov.otus.otuskotlin.adscar.api.v1

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL
import com.fasterxml.jackson.databind.json.JsonMapper
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IRequest
import ru.zyablov.otus.otuskotlin.adscar.api.v1.models.IResponse

val apiV1Mapper: JsonMapper = JsonMapper
    .builder()
    .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    .enable(USE_BASE_TYPE_AS_DEFAULT_IMPL)
    .enable(ACCEPT_CASE_INSENSITIVE_ENUMS)
    .build()

fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IRequest::class.java) as T

fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.readValue(json, IResponse::class.java) as T
