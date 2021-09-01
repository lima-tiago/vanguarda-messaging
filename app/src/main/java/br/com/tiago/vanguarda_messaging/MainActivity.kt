package br.com.tiago.vanguarda_messaging

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.tiago.vanguarda_messaging.chatScreen.ChatActivity
import br.com.tiago.vanguarda_messaging.composable.Center
import br.com.tiago.vanguarda_messaging.ui.theme.VanguardamessagingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            VanguardamessagingTheme {
                val composableScope = rememberCoroutineScope()
                Surface() {
                    SplashView()

                    composableScope.launch {
                        delay(500)
                        startActivity(Intent(this@MainActivity, ChatActivity::class.java))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun SplashView() {
    Center(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_vanguarda),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
    }
}

