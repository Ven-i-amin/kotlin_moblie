package ru.vsu.servicesback.controller

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vsu.servicesback.dto.request.OrderAmountPatchRequest
import ru.vsu.servicesback.dto.request.OrderRequest
import ru.vsu.servicesback.dto.request.OrderTypePatchRequest
import ru.vsu.servicesback.dto.response.OrderResponse
import ru.vsu.servicesback.dto.response.OrderStatusResponse
import ru.vsu.servicesback.service.OrderService

@Validated
@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderService: OrderService,
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): OrderResponse =
        orderService.getById(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) email: String?): List<OrderResponse> =
        if (email.isNullOrBlank()) orderService.getAll() else orderService.getAllByEmail(email)

    @GetMapping("/statuses")
    fun getStatuses(): List<OrderStatusResponse> =
        orderService.getStatusesReadyForProcessing()

    @PostMapping
    fun create(@Valid @RequestBody request: OrderRequest) {
        orderService.create(request)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: OrderRequest) {
        orderService.update(id, request)
    }

    @PatchMapping("/{id}/amount")
    fun setAmount(@PathVariable id: Long, @Valid @RequestBody request: OrderAmountPatchRequest) {
        orderService.setCurrencyAmount(id, request.amount)
    }

    @PatchMapping("/{id}/exchange-amount")
    fun setExchangeAmount(@PathVariable id: Long, @Valid @RequestBody request: OrderAmountPatchRequest) {
        orderService.setExchangeAmount(id, request.amount)
    }

    @PatchMapping("/{id}/type")
    fun setType(@PathVariable id: Long, @Valid @RequestBody request: OrderTypePatchRequest) {
        orderService.setType(id, request.type)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        orderService.delete(id)
    }
}
