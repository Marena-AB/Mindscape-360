package edu.moravian.mindscape360

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import edu.moravian.cardboard.sdk.Cardboard

@Composable
actual fun EnsureCardboardInit() {
    Cardboard.ensureInitialized(LocalContext.current)
}