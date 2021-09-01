package br.com.tiago.vanguarda_messaging.di

import android.R
import android.content.Context
import br.com.tiago.vanguarda_messaging.data.CoroutineDispatcherProvider
import br.com.tiago.vanguarda_messaging.domain.UseCase
import br.com.tiago.vanguarda_messaging.network.SignsRepository
import br.com.tiago.vanguarda_messaging.network.remote.SignsApi
import br.com.tiago.vanguarda_messaging.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSignsRepository(
        api: SignsApi
    ) = SignsRepository(api)

    @Singleton
    @Provides
    fun provideSignsApi(): SignsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SignsApi::class.java)
    }

    @Singleton
    @ExperimentalCoroutinesApi
    @Provides
    fun providesUseCase(
        repository: SignsRepository,
        dispatcherProvider: CoroutineDispatcherProvider
    ): UseCase = UseCase(repository, dispatcherProvider = dispatcherProvider)


    private fun getSSLConfig(context: Context): SSLContext? {

        // Loading CAs from an InputStream
        var cf: CertificateFactory? = null
        cf = CertificateFactory.getInstance("X.509")
        var ca: Certificate
        context.resources.openRawResource(
            context.resources.getIdentifier(
                "ssl_cert",
                "raw",
                context.packageName
            )
        ).use { cert ->
            ca = cf.generateCertificate(cert)
        }

        // Creating a KeyStore containing our trusted CAs
        val keyStoreType: String = KeyStore.getDefaultType()
        val keyStore: KeyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        val tmfAlgorithm: String = TrustManagerFactory.getDefaultAlgorithm()
        val tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)

        // Creating an SSLSocketFactory that uses our TrustManager
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext
    }

}