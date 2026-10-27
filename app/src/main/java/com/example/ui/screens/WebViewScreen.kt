package com.example.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class AndroidBridge(private val onDataReceived: (String) -> Unit) {
    @JavascriptInterface
    fun postMessage(data: String) {
        onDataReceived(data)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(url: String = "https://ais-dev-aotcacp2lpsk4r5vjbwdxk-462628439757.us-east1.run.app") {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var lastReceivedMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Control bar for evaluateJavascript demonstration
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (lastReceivedMessage != null) "Bridge: $lastReceivedMessage" else "WebView Synchronisée",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = {
                        // Requirement 2: evaluateJavascript execution handle
                        webViewRef?.evaluateJavascript("console.log('Hello from Native APK'); alert('Synchronisation Native réussie !');", null)
                    },
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Push JS", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // AndroidView wrapping WebView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true

                    // Requirement 3: CookieManager session synchronization
                    val cookieManager = CookieManager.getInstance()
                    cookieManager.setAcceptCookie(true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        cookieManager.setAcceptThirdPartyCookies(this, true)
                    }

                    webViewClient = WebViewClient()

                    // Requirement 1: addJavascriptInterface named AndroidBridge
                    addJavascriptInterface(AndroidBridge { data ->
                        lastReceivedMessage = data
                    }, "AndroidBridge")

                    loadUrl(url)
                    webViewRef = this
                }
            },
            update = { webView ->
                webViewRef = webView
            }
        )
    }
}
