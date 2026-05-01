package ru.vsu.servicesback

import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.hamcrest.MatcherAssert.assertThat
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import ru.vsu.servicesback.entity.OrderEntity
import ru.vsu.servicesback.entity.UserCoinEntity
import ru.vsu.servicesback.entity.UserEntity
import ru.vsu.servicesback.repository.OrderRepository
import ru.vsu.servicesback.repository.UserCoinRepository
import ru.vsu.servicesback.repository.UserRepository
import ru.vsu.servicesback.util.OrderStatus
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "DB_HOST=localhost",
        "DB_PORT=5432",
        "DB_NAME=testdb",
        "DB_USERNAME=sa",
        "DB_PASSWORD=",
        "JWT_SECRET=change-me-change-me-change-me-change-me-change-me-change-me",
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
    ],
)
class ServicesBackApplicationTests {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userCoinRepository: UserCoinRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @LocalServerPort
    private var port: Int = 0

    private val httpClient: HttpClient = HttpClient.newHttpClient()
    private val objectMapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    @BeforeEach
    fun cleanUp() {
        orderRepository.deleteAll()
        userCoinRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun loginReturnsFlatUserPayloadWithToken() {
        userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 150.0,
            ),
        )

        val response = request(
            method = "POST",
            path = "/api/user/log",
            body = """{"email":"alice@example.com","password":"password123"}""",
        )

        assertEquals(200, response.statusCode())
        val json = readJson(response)
        assertThat(json["id"].asLong(), notNullValue())
        assertEquals("Alice", json["fullName"].asText())
        assertEquals("alice@example.com", json["email"].asText())
        assertEquals("password123", json["password"].asText())
        assertEquals(150.0, json["balance"].asDouble())
        assertThat(json["token"].asText(), notNullValue())
    }

    @Test
    fun authenticatedUserCannotReadAnotherUsersProfile() {
        val alice = userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        val bob = userRepository.save(
            UserEntity(
                fullName = "Bob",
                email = "bob@example.com",
                password = "password123",
                balance = 200.0,
            ),
        )

        val token = loginAndGetToken(alice.email, "password123")

        val response = request(
            method = "GET",
            path = "/api/user/${bob.id}",
            token = token,
        )

        assertEquals(403, response.statusCode())
    }

    @Test
    fun legacyBalancePatchRouteStillWorks() {
        val alice = userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        val token = loginAndGetToken(alice.email, "password123")

        val response = request(
            method = "PATCH",
            path = "/api/user/${alice.id}",
            token = token,
            body = "25.5",
        )

        assertEquals(200, response.statusCode())

        val updated = userRepository.findById(requireNotNull(alice.id)).orElseThrow()
        assertEquals(125.5, updated.balance)
    }

    @Test
    fun userCoinEndpointsSupportLegacyBodyAndTrailingSlash() {
        val alice = userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        userCoinRepository.save(
            UserCoinEntity(
                currencyId = "BTC",
                name = "Bitcoin",
                amount = 2.0,
                user = alice,
            ),
        )
        val token = loginAndGetToken(alice.email, "password123")

        val getResponse = request(
            method = "GET",
            path = "/api/usercoin/byEmail",
            token = token,
            body = "\"alice@example.com\"",
        )
        assertEquals(200, getResponse.statusCode())
        val getJson = readJson(getResponse)
        assertEquals("BTC", getJson[0]["currencyId"].asText())

        val postResponse = request(
            method = "POST",
            path = "/api/usercoin/",
            token = token,
            body = """{"currencyId":"ETH","name":"Ethereum","amount":1.0,"ownerEmail":"alice@example.com"}""",
        )
        assertEquals(200, postResponse.statusCode())
    }

    @Test
    fun orderListWithoutEmailReturnsOnlyCurrentUsersOrders() {
        val alice = userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        userRepository.save(
            UserEntity(
                fullName = "Bob",
                email = "bob@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        orderRepository.save(
            OrderEntity(
                currencyId = "BTC",
                amount = 1.0,
                exchangeCurrencyId = "USDT",
                exchangeAmount = 50000.0,
                type = "BUY",
                status = OrderStatus.STARTED,
                email = "alice@example.com",
                executedAt = LocalDateTime.now(),
            ),
        )
        orderRepository.save(
            OrderEntity(
                currencyId = "ETH",
                amount = 2.0,
                exchangeCurrencyId = "USDT",
                exchangeAmount = 6000.0,
                type = "BUY",
                status = OrderStatus.STARTED,
                email = "bob@example.com",
                executedAt = LocalDateTime.now(),
            ),
        )

        val token = loginAndGetToken(alice.email, "password123")

        val response = request(
            method = "GET",
            path = "/api/order",
            token = token,
        )
        assertEquals(200, response.statusCode())
        val json = readJson(response)
        assertEquals(1, json.size())
        assertEquals("alice@example.com", json[0]["email"].asText())
    }

    @Test
    fun mobileAuthAndProfileRoutesMatchFrontendContract() {
        val registerResponse = request(
            method = "POST",
            path = "/api/mobile/auth/register",
            body = """{"email":"alice@example.com","password":"password123","name":"Alice"}""",
        )
        assertEquals(200, registerResponse.statusCode())
        val token = readJson(registerResponse).asText()

        val meResponse = request(
            method = "GET",
            path = "/api/mobile/users/me",
            token = token,
        )
        assertEquals(200, meResponse.statusCode())
        val meJson = readJson(meResponse)
        assertEquals("Alice", meJson["full_name"].asText())
        assertEquals("alice@example.com", meJson["email"].asText())
        assertEquals("password123", meJson["password"].asText())
        assertEquals(0.0, meJson["balance"].asDouble())

        val balanceResponse = request(
            method = "PATCH",
            path = "/api/mobile/users/me/balance",
            token = token,
            body = """{"balance":245.5}""",
        )
        assertEquals(200, balanceResponse.statusCode())

        val updatedMeResponse = request(
            method = "GET",
            path = "/api/mobile/users/me",
            token = token,
        )
        assertEquals(245.5, readJson(updatedMeResponse)["balance"].asDouble())
    }

    @Test
    fun mobileOrdersTransactionsAndWatchlistRoutesMatchFrontendContract() {
        val alice = userRepository.save(
            UserEntity(
                fullName = "Alice",
                email = "alice@example.com",
                password = "password123",
                balance = 100.0,
            ),
        )
        orderRepository.save(
            OrderEntity(
                currencyId = "btc",
                currencyName = "Bitcoin",
                amount = 0.5,
                exchangeCurrencyId = "USD",
                exchangeAmount = 25000.0,
                type = "buy",
                status = OrderStatus.COMPLETED,
                email = "alice@example.com",
                finishedAt = LocalDateTime.now(),
            ),
        )
        userCoinRepository.save(
            UserCoinEntity(
                currencyId = "btc",
                name = "Bitcoin",
                amount = 0.5,
                user = alice,
            ),
        )

        val token = loginAndGetMobileToken("alice@example.com", "password123")

        val userCoinsResponse = request(
            method = "GET",
            path = "/api/mobile/user-coins",
            token = token,
        )
        assertEquals(200, userCoinsResponse.statusCode())
        assertEquals("btc", readJson(userCoinsResponse)[0]["currency_id"].asText())

        val createOrderResponse = request(
            method = "POST",
            path = "/api/mobile/orders",
            token = token,
            body = """{"currencyId":"eth","currencyName":"Ethereum","type":"sell","amount":1.25,"price":3000.0}""",
        )
        assertEquals(200, createOrderResponse.statusCode())
        val ordersJson = readJson(createOrderResponse)
        assertEquals(true, ordersJson.any { it["currencyId"].asText() == "eth" && it["currencyName"].asText() == "Ethereum" })

        val transactionsResponse = request(
            method = "GET",
            path = "/api/mobile/transactions",
            token = token,
        )
        assertEquals(200, transactionsResponse.statusCode())
        val transactionsJson = readJson(transactionsResponse)
        assertEquals("btc", transactionsJson[0]["currency_id"].asText())
        assertEquals("USD", transactionsJson[0]["change_currency"].asText())

        val addWatchlistResponse = request(
            method = "POST",
            path = "/api/mobile/watchlist",
            token = token,
            body = """{"coinId":"bitcoin"}""",
        )
        assertEquals(200, addWatchlistResponse.statusCode())

        val getWatchlistResponse = request(
            method = "GET",
            path = "/api/mobile/watchlist",
            token = token,
        )
        assertEquals(200, getWatchlistResponse.statusCode())
        assertEquals("bitcoin", readJson(getWatchlistResponse)[0].asText())
    }

    private fun loginAndGetToken(email: String, password: String): String =
        readJson(
            request(
                method = "POST",
                path = "/api/user/log",
                body = """{"email":"$email","password":"$password"}""",
            ),
        )
            .get("token")
            .asText()

    private fun loginAndGetMobileToken(email: String, password: String): String =
        readJson(
            request(
                method = "POST",
                path = "/api/mobile/auth/login",
                body = """{"email":"$email","password":"$password"}""",
            ),
        ).asText()

    private fun request(
        method: String,
        path: String,
        token: String? = null,
        body: String? = null,
    ): HttpResponse<String> {
        val builder = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:$port$path"))

        if (token != null) {
            builder.header("Authorization", "Bearer $token")
        }
        if (body != null) {
            builder.header("Content-Type", "application/json")
        }

        when (method) {
            "GET" -> {
                if (body == null) {
                    builder.GET()
                } else {
                    builder.method("GET", HttpRequest.BodyPublishers.ofString(body))
                }
            }

            "POST" -> builder.POST(HttpRequest.BodyPublishers.ofString(body ?: ""))
            "PATCH" -> builder.method("PATCH", HttpRequest.BodyPublishers.ofString(body ?: ""))
            else -> error("Unsupported method: $method")
        }

        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString())
    }

    private fun readJson(response: HttpResponse<String>): JsonNode =
        objectMapper.readTree(response.body())
}
