import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import edu.moravian.mindscape360.AboutScreen
import edu.moravian.mindscape360.ActualScreen
import edu.moravian.mindscape360.VideosScreen
import edu.moravian.mindscape360.WelcomeScreen


var _navController: NavHostController? = null
val navController: NavHostController
    get() = _navController ?: error("NavController is not initialized")

//val kamelConfig = KamelConfig {
//    takeFrom(KamelConfig.Default)
//    fileFetcher()
//
//    httpFetcher {
//        defaultRequest {
//            url("https://mindscape-360.s3.us-east-1.amazonaws.com/")
//        }
//
//        install(HttpRequestRetry) {
//            maxRetries = 3
//            retryIf { httpRequest, httpResponse ->
//                !httpResponse.status.isSuccess()
//            }
//        }
//    }
//}

@Composable
@Preview
fun App() {
//    CompositionLocalProvider(LocalKamelConfig provides kamelConfig) {
        MaterialTheme {
            val welcomeScreen = "welcome"
            val aboutScreen = "about"
            val actualScreen = "actual"

            _navController = rememberNavController()

            NavHost(navController = navController, startDestination = welcomeScreen) {
                composable(welcomeScreen) { WelcomeScreen() }
                composable("videos") {
                    // VideosScreen now handles its own data loading
                    VideosScreen(navController)
                }
                composable(aboutScreen) { AboutScreen() }
                composable(actualScreen) { ActualScreen(navController) }
            }
        }
//    }
}
