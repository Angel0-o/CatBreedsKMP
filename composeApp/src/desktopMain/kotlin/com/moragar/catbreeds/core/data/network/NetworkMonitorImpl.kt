package com.moragar.catbreeds.core.data.network

import com.moragar.catbreeds.core.domain.network.NetworkMonitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.InetSocketAddress
import java.net.Socket

class NetworkMonitorImpl : NetworkMonitor {
    override suspend fun isOnline(): Flow<Boolean> = flow {
        while (true) {
            val isOnline = try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                    true
                }
            } catch (e: Exception) {
                false
            }
            emit(isOnline)
            delay(5000) // check every 5s
        }
    }
}