package com.timekeeping.smart.extension

import android.content.res.Resources
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DateUtils
import java.io.File
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun Int.formatGender(): String {
    return when {
        this == 1 -> {
            "Nam"
        }
        this == 2 -> {
            "Nữ"
        }
        this == 3 -> {
            "Khác"
        }
        else -> "-"
    }
}

fun String?.formatAgeNull(): String {
    return if (this.isNullOrEmpty())
        "-"
    else
        this
}

fun String.formatAge(): String {  // YYYY-MM-DD
    val year = this.substring(0, 4).toInt()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return "${(currentYear - year)} tuổi"
}
fun String?.formatString(): String {
    if(this.isNullOrEmpty() || this == "NULL" || this == "null"){
        return ""
    }
    return this
}
fun String?.formatDateString(): String {
    if(this.isNullOrEmpty() || this == "NULL" || this == "null"){
        return ""
    }
    try {
        val date = DateUtils.stringToDate(this, Constant.DATE_FORMAT_3)
        return DateUtils.dateToString(date, Constant.DATE_FORMAT_2)
    }catch (e: Exception){
        return ""
    }
}
fun String.formatAgebyBirthday(): String {  // YYYY-MM-DD
    val year = this.substring(0, 4).toInt()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return "${(currentYear - year)}"
}

fun Double.formatHeight(): String {
    val heightInMeter = this / 100
    val heightInCentimeter = this % 100

    return heightInMeter.toInt().toString() + "m" + if (heightInCentimeter > 0) heightInCentimeter.toInt().toString() else ""
}

fun Double.formatHeightbyCm(): String {
    return "$this cm"
}

fun CharSequence?.nullOrEmpty(): CharSequence? = if (isNullOrEmpty()) null else this

// tra ve ten anh khi upload tu thu vien
fun String.getNameImage(): String {
    val dir = this
    val f = File(dir)
    return f.name
}

val Int.dpToPx: Int get() = Math.round(this * Resources.getSystem().displayMetrics.density)

fun getToday(): String {  // yyyy-MM-dd
    val date = Calendar.getInstance().time
    return date.convertFomat("yyyy-MM-dd")
}

fun Date.convertFomat(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

val Int.degit: String get() = if (this <= 9) "0${this}" else this.toString()

/**
 * Function to convert milliseconds time to
 * Timer Format
 * Hours:Minutes:Seconds
 */
fun Long.formatMilliSecond(): String {
    val milliseconds = this
    // Convert total duration into time
    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

    // Add hours if there
    if (hours > 0) {
        return String.format("%02d:%02d:%02d",hours,minutes,seconds)
    }else{
        return String.format("%02d:%02d",minutes,seconds)
    }
}

fun Int.formatIntToTime(): String{
    val seconds = (this % 60)
    val minutes = (this % 3600) / 60
    val hour = this / 3600
    val secondFormatter = String.format("%02d", seconds)
    val minutesFormatter = String.format("%02d", minutes)
    val hourFormatter = String.format("%02d", hour)
    return if(hour == 0) {
        "$minutesFormatter : $secondFormatter"
    }else{
        "$hourFormatter :  $minutesFormatter : $secondFormatter"
    }
}

fun String.flattenToAscii(): String {
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return try {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        pattern.matcher(temp).replaceAll("").replace("Đ", "D").replace("đ", "d")
    } catch (e: java.lang.Exception) {
        ""
    }
}

fun String.dateTimeToDate(): String{
    return if(this.length> 10){
        val date = DateUtils.stringToDate(this.substring(0, 10), Constant.DATE_FORMAT_3)
        DateUtils.dateToString(date, Constant.DATE_FORMAT_2)
    }else
        this
}

fun String.dateTimeToTime(): String{
    return if(this.length> 19){
        this.substring(11, 19)
    }else
        this
}