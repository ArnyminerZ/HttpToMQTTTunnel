package server

object EnvironmentVariables {
    data object MQTTBroker: EnvironmentVariable("MQTT_BROKER")
    data object MQTTClientId: EnvironmentVariable("MQTT_CLIENT_ID", "http-tunnel")
}
