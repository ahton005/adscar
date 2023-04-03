package ru.zyablov.otus.otuskotlin.adscar.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to InnerContext")
