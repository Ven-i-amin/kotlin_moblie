package ru.vsu.servicesback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ServicesBackApplication

fun main(args: Array<String>) {
    runApplication<ServicesBackApplication>(*args)
}
