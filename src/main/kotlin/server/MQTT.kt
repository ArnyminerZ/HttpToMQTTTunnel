package server

import java.nio.charset.Charset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object MQTT {
    private lateinit var client: MqttClient

    private val messagesFlow = MutableStateFlow<Pair<String, MqttMessage>?>(null)

    private val _uptimeFlow = MutableStateFlow<Int?>(null)
    val uptimeFlow: StateFlow<Int?> = _uptimeFlow.asStateFlow()

    fun init() {
        val broker = EnvironmentVariables.MQTTBroker.get()
        val clientId = EnvironmentVariables.MQTTClientId.get()
        client = MqttClient(broker, clientId, MemoryPersistence())

        client.setCallback(
            object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {}

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    println("::MQTT:: Delivery complete. Message ID: ${token?.messageId}")
                }

                override fun messageArrived(topic: String, message: MqttMessage?) {
                    if (message != null) {
                        println("::MQTT:: Received new message in $topic")
                        messagesFlow.tryEmit(topic to message)
                    }
                }
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            subscribe("\$SYS/broker/uptime").collect { message ->
                val payload = message.payload
                val msg = payload.toString(Charset.defaultCharset())
                _uptimeFlow.tryEmit(msg.substringBefore(' ').toInt())
            }
        }
    }

    fun isConnected(): Boolean = client.isConnected

    fun connect() {
        client.connect()
        println("::MQTT:: Connected")
    }

    fun disconnect() {
        client.disconnect()
        println("::MQTT:: Disconnected")
    }

    fun publish(topic: String, message: String) {
        if (!isConnected()) error("MQTT client is not connected")

        val msg = MqttMessage(message.toByteArray()).apply {
            qos = 2
        }
        client.publish(topic, msg)
    }

    fun subscribe(topic: String): Flow<MqttMessage> {
        if (!isConnected()) error("MQTT client is not connected")

        client.subscribe(topic)

        return messagesFlow.filter { it?.first == topic }.map { it!!.second }.cancellable()
    }
}
