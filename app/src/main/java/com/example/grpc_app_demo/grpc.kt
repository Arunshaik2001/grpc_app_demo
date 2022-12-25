package com.example.grpc_app_demo

import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun sendMessage(
    message: String,
    channel: ManagedChannel?
):String? {
    return try {
        val stub = GreeterGrpc.newBlockingStub(channel)
        val request = HelloRequest.newBuilder().setName(message).build()
        val reply = stub.sayHello(request)
        reply.message
    } catch (e: Exception) {
        e.message
    }
}

fun sendMessageWithReplies(
    message: String,
    channel: ManagedChannel?
):Any? {
    return try {
        val stub = GreeterGrpc.newBlockingStub(channel)
        val request = HelloRequest.newBuilder().setName(message).build()
        val reply = stub.lotsOfReplies(request)
        reply.asSequence().toList().map { it -> it.message+"\n" }
    } catch (e: Exception) {
        e
    }
}

fun sendMessageWithRequests(
    channel: ManagedChannel?
):Any {
    return try {
        val stub = GreeterGrpc.newStub(channel)
        var failed: Throwable? = null
        val finishLatch = CountDownLatch(1)
        val responseList = mutableListOf<HelloResponse>()
        val requestObserver = stub.lotsOfRequests(object : StreamObserver<HelloResponse> {
            override fun onNext(response: HelloResponse) {
                responseList.add(response)
            }

            override fun onError(t: Throwable) {
                failed = t
                finishLatch.countDown()
            }

            override fun onCompleted() {
                finishLatch.countDown()
            }
        })


        try {
            val requests = arrayOf(
                newHelloResponse("TOM"),
                newHelloResponse("ANDY"),
                newHelloResponse("MANDY"),
                newHelloResponse("John")
            )
            for (request in requests) {
                requestObserver.onNext(request)
            }
        } catch (e: java.lang.RuntimeException) {
            requestObserver.onError(e)
            return e.message?:""
        }

        requestObserver.onCompleted()

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            return "Timeout error"
        }

        if (failed != null) {
            return failed?.message?:""
        }

        return responseList.map { it.message }
    } catch (e: Exception) {
        e
    }
}

fun sendMessageBiDirectional(channel: ManagedChannel?): Any{
    return try {
        val stub = GreeterGrpc.newStub(channel)
        var failed: Throwable? = null
        val finishLatch = CountDownLatch(1)
        val responseList = mutableListOf<HelloResponse>()
        val requestObserver = stub.bidirectionalHello(object : StreamObserver<HelloResponse> {
            override fun onNext(response: HelloResponse) {
                responseList.add(response)
            }

            override fun onError(t: Throwable) {
                failed = t
                finishLatch.countDown()
            }

            override fun onCompleted() {
                finishLatch.countDown()
            }
        })


        try {
            val requests = arrayOf(
                newHelloResponse("TOM"),
                newHelloResponse("ANDY"),
                newHelloResponse("MANDY"),
                newHelloResponse("John")
            )
            for (request in requests) {
                requestObserver.onNext(request)
            }
        } catch (e: java.lang.RuntimeException) {
            requestObserver.onError(e)
            return e.message?:""
        }

        requestObserver.onCompleted()

        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            return "Timeout error"
        }

        if (failed != null) {
            return failed?.message?:""
        }

        return responseList.map { it.message }
    } catch (e: Exception) {
        e
    }
}

private fun newHelloResponse(message: String): HelloRequest {
    return HelloRequest.newBuilder().setName(message).build()
}