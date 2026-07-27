package com.nicklam.hktaxinavi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nicklam.hktaxinavi.ui.theme.HKTaxiNaviTheme
import com.nicklam.hktaxinavi.ui.screen.HomeScreen

/**
 * 主入口 Activity
 *
 * 使用 Jetpack Compose 渲染主頁 (導航啟動頁)
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HKTaxiNaviTheme {
                HomeScreen(
                    onStartNavi = {
                        // 啟動導航頁面
                        NaviActivity.start(this)
                    }
                )
            }
        }
    }
}
