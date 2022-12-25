package com.example.grpc_app_demo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    private var channel: ManagedChannel? = null
    fun onIpChange(value: String) {
        _ip.value = value
    }

    fun onPortChange(value: String) {
        _port.value = value
    }

    private val _ip = mutableStateOf("")
    val ip: State<String> = _ip

    private val _port = mutableStateOf("")
    val port:State<String> = _port

    private val _result = mutableStateOf("")
    val result:State<String> = _result

    private val _hostEnabled = mutableStateOf(true)
    val hostEnabled:State<Boolean> = _hostEnabled

    private val _portEnabled = mutableStateOf(true)
    val portEnabled:State<Boolean> = _portEnabled

    private val _startEnabled = mutableStateOf(true)
    val startEnabled:State<Boolean> = _startEnabled

    private val _endEnabled = mutableStateOf(false)
    val endEnabled:State<Boolean> = _endEnabled

    private val _buttonsEnabled = mutableStateOf(false)
    val buttonsEnabled:State<Boolean> = _buttonsEnabled


    fun start() {
        try {
            val host: String = _ip.value
            val portStr: String = _port.value
            val port = if (portStr.isEmpty()) 0 else Integer.valueOf(portStr)
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            _endEnabled.value = true
            _hostEnabled.value = false
            _portEnabled.value = false
            _startEnabled.value = false
            _buttonsEnabled.value = true
        } catch (e: Exception) {
            _result.value = (System.currentTimeMillis().toString()  ) + "\n" + (e.message?: "")
        }
    }

    fun exit() {
        channel?.shutdown()
        _endEnabled.value = false
        _hostEnabled.value = true
        _portEnabled.value = true
        _startEnabled.value = true
        _buttonsEnabled.value = false
    }

    fun sayHello(name: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                updateResult(sendMessage(name,channel) ?: "")
            } catch (e: Exception) {
                updateResult(e.message?:"")
            }
        }
    }

    fun sendMessageWithReplies(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateResult(sendMessageWithReplies(message,channel).toString())
            } catch (e: Exception) {
                updateResult(e.message?:"")
            }
        }
    }

    fun sendWithRequests(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateResult(sendMessageWithRequests(channel).toString())
            } catch (e: Exception) {
                updateResult(e.message?:"")
            }
        }
    }

    fun sendWithBidirectionalMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateResult(sendMessageBiDirectional(channel).toString())
            } catch (e: Exception) {
                updateResult(e.message?:"")
            }
        }
    }


    private suspend fun updateResult(message: String){
        withContext(Dispatchers.Main){
            _result.value = System.currentTimeMillis().toString() +"\n"+ message
        }
    }
}