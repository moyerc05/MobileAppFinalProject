package edu.moravian.csci395.flashfocus

import androidx.compose.ui.window.ComposeUIViewController
import edu.moravian.csci395.flashfocus.data.getRoomDatabase

@Suppress("ktlint:standard:function-naming")
fun MainViewController() =
    ComposeUIViewController {
        App(getRoomDatabase(getDatabaseBuilder()))
    }
