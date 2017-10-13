package com.arran.askaquestion.dagger

import com.arran.askaquestion.firebase.FirebaseApi
import com.arran.askaquestion.firebase.FirebaseRepository
import com.arran.askaquestion.firebase.IFirebaseApi
import com.arran.askaquestion.firebase.IFirebaseRepository
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@Component(modules = arrayOf(NetworkModule::class))
interface FirebaseRepositoryComponent {
    fun getFirebaseRepository(): IFirebaseRepository
}

@Module
class NetworkModule {

    @Provides
    fun providefirebaseRepository(firebaseApi: IFirebaseApi): IFirebaseRepository {
        return FirebaseRepository(firebaseApi)

    }

    @Provides
    fun provideFirebaseApi(): IFirebaseApi {
        return FirebaseApi()
    }
}



