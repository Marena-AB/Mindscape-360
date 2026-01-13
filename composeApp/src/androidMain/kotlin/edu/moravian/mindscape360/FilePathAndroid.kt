package edu.moravian.mindscape360

import android.content.Context

// Android implementation
private lateinit var applicationContext: Context

fun initializeFileSystem(context: Context) {
    applicationContext = context.applicationContext
}

actual fun filePathTo(fileName: String): String {
    return "${applicationContext.filesDir.absolutePath}/$fileName"
}