package id.dsc

import android.app.Application
import com.google.firebase.FirebaseApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class RealApplication : Application() {

    lateinit var  mInstance: RealApplication
    override fun onCreate() {
        super.onCreate()
        mInstance = this

        FirebaseApp.initializeApp(this);
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("Poppins-Regular.ttf")
                            .build()
                    )
                )
                .build()
        )
    }
    @Synchronized
    fun getInstance(): RealApplication {
        return mInstance
    }
}