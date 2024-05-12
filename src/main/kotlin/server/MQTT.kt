package server

import java.nio.charset.Charset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    private const val UPTIME_TOPIC = "\$SYS/broker/uptime"

    private lateinit var client: MqttClient

    private var uptimeSupervisionJob: Job? = null

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
                        // Only log messages that are not system messages
                        if (!topic.startsWith('$')) {
                            println("::MQTT:: Received new message in $topic.")
                        }
                        messagesFlow.tryEmit(topic to message)
                    }
                }
            }
        )
    }

    fun isConnected(): Boolean = client.isConnected

    fun connect() {
        if (isConnected()) return
        client.connect()
        println("::MQTT:: Connected")

        uptimeSupervisionJob = CoroutineScope(Dispatchers.IO).launch {
            subscribe(UPTIME_TOPIC).collect { message ->
                val payload = message.payload
                val msg = payload.toString(Charset.defaultCharset())
                val uptime = msg.substringBefore(' ').toInt()
                _uptimeFlow.tryEmit(uptime)
            }
        }
    }

    fun disconnect() {
        if (!isConnected()) return
        client.disconnect()
        println("::MQTT:: Disconnected")

        uptimeSupervisionJob?.cancel()
        uptimeSupervisionJob = null
    }

    fun publish(topic: String, message: String) {
        if (!isConnected()) error("MQTT client is not connected")

        val payload = message.encodeToByteArray()
        val msg = MqttMessage(payload).apply {
            qos = 2
        }
        client.publish(topic, msg)
        println("::MQTT:: Published ${payload.size} bytes to $topic")
    }

    fun subscribe(topic: String): Flow<MqttMessage> {
        if (!isConnected()) error("MQTT client is not connected")

        client.subscribe(topic)
        println("::MQTT:: Subscribed to $topic")

        return messagesFlow.filter { it?.first == topic }.map { it!!.second }.cancellable()
    }
}
