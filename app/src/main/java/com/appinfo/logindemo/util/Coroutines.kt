package com.appinfo.logindemo.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Kotlin Coroutines — For making non-blocking (main thread) network requests.

object  Coroutines{
    fun main(work: suspend (()-> Unit))=
        CoroutineScope(Dispatchers.Main).launch{
            work()
        }
}