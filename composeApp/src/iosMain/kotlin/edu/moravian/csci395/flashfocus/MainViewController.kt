package edu.moravian.csci395.flashfocus

import androidx.compose.ui.window.ComposeUIViewController
import edu.moravian.csci395.flashfocus.data.getRoomDatabase

fun MainViewController() = ComposeUIViewController {
    App(getRoomDatabase(getDatabaseBuilder()))
}