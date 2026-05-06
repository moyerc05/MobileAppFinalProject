package edu.moravian.csci395.flashfocus

import com.tweener.alarmee.configuration.AlarmeeIosPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration

actual fun createAlarmeePlatformConfiguration(): AlarmeePlatformConfiguration {
    return AlarmeeIosPlatformConfiguration
}