package edu.moravian.csci395.flashfocus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform