package sk.mkdigital.androidshowcase.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import sk.mkdigital.androidshowcase.presentation.base.Route.HomeSection
import sk.mkdigital.androidshowcase.presentation.base.Route.SignIn
import sk.mkdigital.androidshowcase.presentation.base.Route.SignUp
import sk.mkdigital.androidshowcase.presentation.base.Route.Settings
import kotlin.reflect.KClass

interface NavRouter<T : NavKey> {
    val backStack: NavBackStack<T>

    fun navigateTo(page: T)
    fun <R : Any> navigateTo(page: T, popUpTo: KClass<R>? = null, inclusive: Boolean = false)
    fun onBack()
}

class NavRouterImpl<T : NavKey>(
    override val backStack: NavBackStack<T>,
) : NavRouter<T> {

    override fun navigateTo(page: T) {
        backStack.add(page)
    }

    override fun <R : Any> navigateTo(page: T, popUpTo: KClass<R>?, inclusive: Boolean) {
        if (popUpTo != null) {
            while (backStack.lastOrNull()?.let { !popUpTo.isInstance(it) } == true) {
                backStack.removeLastOrNull()
            }
            if (inclusive) {
                backStack.removeLastOrNull()
            }
        }
        backStack.add(page)
    }

    override fun onBack() {
        backStack.removeLastOrNull()
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T : NavKey> rememberNavRouter(): NavRouter<T> {
    val backStack = rememberNavBackStack(saveStateConfiguration, SignIn)
    return remember(backStack) {
        NavRouterImpl(backStack as NavBackStack<T>)
    }
}

@Composable
fun <T : NavKey> rememberNavEntryDecorators(): List<NavEntryDecorator<T>> = listOf(
    rememberSaveableStateHolderNavEntryDecorator(),
    rememberViewModelStoreNavEntryDecorator()
)

private val saveStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(SignIn.serializer())
            subclass(SignUp.serializer())
            subclass(HomeSection.Home.serializer())
            subclass(HomeSection.UiComponents.serializer())
            subclass(HomeSection.Networking.serializer())
            subclass(HomeSection.Storage.serializer())
            subclass(HomeSection.Apis.serializer())
            subclass(HomeSection.Scanner.serializer())
            subclass(HomeSection.Database.serializer())
            subclass(HomeSection.Calendar.serializer())
            subclass(HomeSection.Notifications.serializer())
            subclass(Settings.serializer())
        }
    }
}
