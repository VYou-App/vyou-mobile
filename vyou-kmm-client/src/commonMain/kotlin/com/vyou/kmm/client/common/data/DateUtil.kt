package com.vyou.kmm.client.common.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object DateUtil {

    // 2022-04-11T14:59:30.835835
    fun toTimeMillis(isoString: String): Long {
        return LocalDateTime.parse(isoString).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}