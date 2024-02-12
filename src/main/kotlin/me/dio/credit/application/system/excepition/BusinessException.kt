package me.dio.credit.application.system.excepition

data class BusinessException(override val message: String?) : RuntimeException(message)
