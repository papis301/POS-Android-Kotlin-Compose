package com.pisco.stockmanager.desktop.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatCfa(
    amount: Double
): String {

    val symbols =
        DecimalFormatSymbols(Locale.FRANCE).apply {
            groupingSeparator = '.'
        }

    return DecimalFormat(
        "#,###",
        symbols
    ).format(amount)
}

fun formatDate(
    timestamp: Long
): String {

    return SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(
        Date(timestamp)
    )
}