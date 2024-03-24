package server

object EnvironmentVariables {
    data object MQTT_BROKER: EnvironmentVariable("MQTT_BROKER")
    data object MQTT_CLIENT_ID: EnvironmentVariable("MQTT_CLIENT_ID", "http-tunnel")
}
