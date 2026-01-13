package edu.moravian.mindscape360

import App
import androidx.compose.ui.window.ComposeUIViewController
import edu.moravian.cardboard.MainViewControllerWrapper

fun MainViewController() = MainViewControllerWrapper(ComposeUIViewController { App() })