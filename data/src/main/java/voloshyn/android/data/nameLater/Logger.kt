package voloshyn.android.data.nameLater

import java.util.logging.Logger
import kotlin.reflect.KClass

internal  fun <T:Any> Logger.logError(klass: KClass<T>, e: Throwable) {
    info(" ${klass.simpleName}, ${Thread.currentThread().stackTrace[2].methodName}, ${e.message} ")
}