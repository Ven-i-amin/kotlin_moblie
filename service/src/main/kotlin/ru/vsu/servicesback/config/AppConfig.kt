package ru.vsu.servicesback.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Random

@Configuration
class AppConfig {
    @Bean
    fun random(): Random = Random()
}
