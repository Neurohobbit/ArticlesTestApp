package di

import data.remote.ApiRepository
import data.remote.MockHttpClientFactory
import data.remote.result.ApiWrapper
import io.ktor.client.HttpClient
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClient> { MockHttpClientFactory().apply { startMockServer() }.create() }
    single { ApiWrapper(get()) }
    single { ApiRepository(get()) }
}