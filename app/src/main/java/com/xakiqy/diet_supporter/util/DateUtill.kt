package com.xakiqy.diet_supporter.util

import java.util.*

fun getDateTodayWithoutTime(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time
}

fun getWholeWeekDateWithoutTime(): List<Date> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val weekList = ArrayList<Date>()
    for (i in 0..6) {
        weekList.add(calendar.time)
        calendar.add(Calendar.DATE, 1)
    }
    return weekList
}