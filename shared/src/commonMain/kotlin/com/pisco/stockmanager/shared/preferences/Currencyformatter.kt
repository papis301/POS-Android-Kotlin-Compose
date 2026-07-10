package com.pisco.stockmanager.desktop

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatCfa(amount: Any): String {

    val symbols = DecimalFormatSymbols(Locale.FRANCE).apply {
        groupingSeparator = '.'
    }

    return DecimalFormat("#,###", symbols).format(amount)
}