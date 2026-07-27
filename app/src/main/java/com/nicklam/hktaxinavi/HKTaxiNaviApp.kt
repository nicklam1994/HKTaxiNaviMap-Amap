package com.nicklam.hktaxinavi

import android.util.Log
import com.amap.api.navi.AMapNavi
import com.amap.api.location.AMapLocationClient

/**
 * 香港的士導航 App Application
 *
 * 初始化高德 SDK: 導航 + 定位 + 地圖
 */
class HKTaxiNaviApp : Application() {

    lateinit var amapNavi: AMapNavi
        private set

    lateinit var locationClient: AMapLocationClient
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        initAmapSDK()
    }

    private fun initAmapSDK() {
        val apiKey = BuildConfig.AMAP_API_KEY

        // === 1. 隱私合規 (必須最先調用) ===
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)

        try {
            // === 2. 初始化導航 SDK ===
            amapNavi = AMapNavi.getInstance(this).apply {
                // 開啟平行路檢測
                setUseInnerVoice(true) // 使用內建 TTS
                setEmulatorNaviSpeed(60) // 模擬導航速度
            }

            // === 3. 初始化定位 SDK ===
            locationClient = AMapLocationClient(this).apply {
                // 配置將在 Activity 中設置
            }

            Log.d(TAG, "高德 SDK 初始化完成, API Key: ${apiKey.take(6)}...")
        } catch (e: Exception) {
            Log.e(TAG, "高德 SDK 初始化失敗", e)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        amapNavi.destroy()
        locationClient.onDestroy()
    }

    companion object {
        private const val TAG = "HKTaxiNaviApp"

        lateinit var instance: HKTaxiNaviApp
            private set
    }
}
