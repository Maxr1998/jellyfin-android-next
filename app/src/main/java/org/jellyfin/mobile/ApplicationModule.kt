package org.jellyfin.mobile

import coil.ImageLoader
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import org.jellyfin.apiclient.Jellyfin
import org.jellyfin.apiclient.android
import org.jellyfin.apiclient.model.ClientInfo
import org.jellyfin.mobile.controller.ServerController
import org.jellyfin.mobile.fragment.ConnectFragment
import org.jellyfin.mobile.fragment.WebViewFragment
import org.jellyfin.mobile.media.car.LibraryBrowser
import org.jellyfin.mobile.player.PlayerEvent
import org.jellyfin.mobile.player.PlayerFragment
import org.jellyfin.mobile.utils.Constants
import org.jellyfin.mobile.utils.PermissionRequestHelper
import org.jellyfin.mobile.viewmodel.MainViewModel
import org.jellyfin.mobile.webapp.RemoteVolumeProvider
import org.jellyfin.mobile.webapp.WebappFunctionChannel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val PLAYER_EVENT_CHANNEL = "PlayerEventChannel"

val applicationModule = module {
    single { AppPreferences(androidApplication()) }
    single { OkHttpClient() }
    single { ImageLoader(androidApplication()) }
    single {
        Jellyfin {
            android(androidApplication())

            clientInfo = ClientInfo(
                name = Constants.APP_INFO_NAME,
                version = Constants.APP_INFO_VERSION
            )
        }
    }
    single { PermissionRequestHelper() }
    single { WebappFunctionChannel() }
    single { RemoteVolumeProvider(get()) }
    single(named(PLAYER_EVENT_CHANNEL)) { Channel<PlayerEvent>() }

    // Controllers
    single { ServerController(get(), get(), get(), get()) }

    // ViewModels
    viewModel { MainViewModel(get(), get(), get()) }

    // Fragments
    fragment { ConnectFragment() }
    fragment { WebViewFragment() }
    fragment { PlayerFragment() }

    // Media
    factory { LibraryBrowser(get(), get(), get(), get(), get(), get(), get(), get())  }
}
