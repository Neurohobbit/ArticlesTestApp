package data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class MockHttpClientFactory {

    private val server = MockWebServer().apply {

        var counter = 0

        dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                counter++
                return if (counter % 3 == 0) {
                    MockResponse()
                        .setResponseCode(500)
                        .setBody("Internal Server Error")
                } else when (request.path) {
                    "/articles" -> MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json")
                        .setBody(generateMockResponse())
                        .setBodyDelay(2, TimeUnit.SECONDS)

                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    fun startMockServer() {
        server.start()
    }

    fun stopMockServer() {
        server.shutdown()
    }

    fun create(): HttpClient {
        val baseUrl = server.url("/")

        return HttpClient(OkHttp) {
            engine {
                config {
                    followRedirects(true)
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = baseUrl.host
                    port = baseUrl.port
                }
            }
        }
    }

    fun generateMockResponse() = """[
  {
    "id": "6915fa879cb3301f2db921fa",
    "title": "commodo",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Sit ut pariatur minim ullamco. In pariatur consequat proident fugiat. Consequat amet sunt sunt id ad mollit duis labore velit fugiat labore adipisicing culpa. Laboris id elit consectetur eiusmod labore esse. Veniam in veniam ut id in excepteur pariatur fugiat dolor elit quis.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 1</title>
</head>
<body>

<h1>Article 1</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa8720bb20ab537eaac0",
    "title": "incididunt",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Dolore ullamco Lorem occaecat ea veniam qui sint nulla duis quis enim elit veniam. Magna nulla exercitation culpa Lorem. Aliqua amet in voluptate non velit. Non laborum aliquip fugiat dolor qui dolore ipsum excepteur tempor irure ipsum voluptate enim.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 2</title>
</head>
<body>

<h1>Article 2</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa87e417edda1f8e8de4",
    "title": "ut",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Anim eiusmod reprehenderit enim reprehenderit. Aute do fugiat eu voluptate excepteur. Consectetur velit laborum et laboris nisi reprehenderit. Laboris sunt anim ex velit. Nostrud ad sit aliqua cupidatat eu consequat nisi.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 3</title>
</head>
<body>

<h1>Article 3</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa87b94c6d11a726bf08",
    "title": "irure",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Labore officia eu laborum deserunt consequat excepteur. Mollit ut qui commodo elit. Exercitation reprehenderit ex magna aliquip labore elit laborum sunt. Pariatur elit quis sit voluptate tempor tempor cillum.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 4</title>
</head>
<body>

<h1>Article 4</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa870013ee6fc5905c14",
    "title": "est",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Exercitation excepteur et deserunt consequat. Eiusmod eu commodo pariatur incididunt sint ut anim enim aute do deserunt do commodo nisi. Dolore sint elit ullamco amet voluptate laborum eu dolor ut sit proident cupidatat sunt dolore. Labore aliqua officia elit adipisicing do excepteur Lorem magna nulla dolore officia Lorem excepteur. Laborum dolor tempor dolor laborum nulla nulla velit adipisicing exercitation in Lorem occaecat eiusmod amet. Laboris esse enim irure adipisicing nostrud in do commodo elit laboris magna aliqua dolore eu.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 5</title>
</head>
<body>

<h1>Article 5</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa87552e26c127a3e7f5",
    "title": "nulla",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Tempor veniam cupidatat laboris eiusmod veniam officia labore laborum consequat ullamco. Dolor fugiat velit reprehenderit adipisicing commodo aliquip irure incididunt non exercitation est aute. Occaecat non incididunt dolore consectetur ipsum minim. Nisi labore cupidatat aliqua ut qui. Fugiat et duis aliquip exercitation occaecat qui mollit nisi est. Elit duis consectetur officia amet ut laboris ipsum qui elit tempor. Sint minim id laborum et culpa consequat minim non qui occaecat deserunt eu.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 6</title>
</head>
<body>

<h1>Article 6</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  },
  {
    "id": "6915fa87fd51fb76c4c0533d",
    "title": "est",
    "expirationTimestamp" : ${generateExpirationTimestamp()},
    "summary": "Ipsum quis reprehenderit dolore et consequat aute. Sint incididunt occaecat cillum nulla do voluptate nostrud ipsum non id culpa consequat cillum do. Consequat nisi in ex enim incididunt aliqua non velit mollit tempor aliqua velit fugiat. Culpa sunt reprehenderit nulla non id.\r\n"
    "text": "<!DOCTYPE html>
<html>
<head>
<title>Article 1</title>
</head>
<body>

<h1>Article 1</h1>
<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>

</body>
</html>
"
  }
]"""

    fun generateExpirationTimestamp(): Long {
        val now = System.currentTimeMillis()
        val randomSeconds = Random.nextInt(5, 120)
        return now + randomSeconds.seconds.inWholeMilliseconds
    }
}