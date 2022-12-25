package com.example.grpc_app_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.grpc_app_demo.ui.theme.GRPC_APP_DEMOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val mainViewModel: MainViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {
            val mainButtonColor = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red,
                contentColor = Color.White
            )
            GRPC_APP_DEMOTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text("GRPC Demo")
                                }
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                        ){
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ){
                                OutlinedTextField(
                                    enabled = mainViewModel.hostEnabled.value,
                                    value = mainViewModel.ip.value,
                                    onValueChange = {
                                        mainViewModel.onIpChange(it)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    label = {
                                        Text("Server Address")
                                    }
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                OutlinedTextField(
                                    enabled = mainViewModel.portEnabled.value,
                                    value = mainViewModel.port.value,
                                    onValueChange = {
                                        mainViewModel.onPortChange(it)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    label = {
                                        Text("Port")
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Button(
                                    enabled = mainViewModel.startEnabled.value,
                                    onClick = {
                                        mainViewModel.start()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    colors = mainButtonColor
                                ) {
                                    Text("Start")
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                                Button(
                                    enabled = mainViewModel.endEnabled.value,
                                    onClick = {
                                        mainViewModel.exit()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text("End")
                                }
                            }
                            Button(
                                enabled = mainViewModel.buttonsEnabled.value,
                                onClick = {
                                    mainViewModel.sayHello("Arun")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = mainButtonColor
                            ) {
                                Text("Simple RPC: Say Hello", modifier = Modifier)
                            }
                            Button(
                                enabled = mainViewModel.buttonsEnabled.value,
                                onClick = {
                                    mainViewModel.sendMessageWithReplies("Arun")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = mainButtonColor
                            ) {
                                Text("Server Streaming: Message With Replies")
                            }
                            Button(
                                enabled = mainViewModel.buttonsEnabled.value,
                                onClick = {
                                    mainViewModel.sendWithRequests()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = mainButtonColor
                            ) {
                                Text("Client Streaming: Lots of Requests")
                            }
                            Button(
                                enabled = mainViewModel.buttonsEnabled.value,
                                onClick = {
                                    mainViewModel.sendWithBidirectionalMessage()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = mainButtonColor
                            ) {
                                Text("Bi-directional Streaming: Chat")
                            }
                            Text("Result: ${mainViewModel.result.value}")
                        }
                    }
                }
            }
        }
    }
}
