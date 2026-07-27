package com.nicklam.hktaxinavi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewOptions
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.*
import com.nicklam.hktaxinavi.overlay.TollOverlayView

/**
 * 導航 Activity
 *
 * 使用 AMapNaviView 進行即時導航，包含:
 * - 車道引導 (showLaneInfo)
 * - 路口放大圖 (showCross)
 * - 導航資訊回調 (距離、路名、路況)
 * - 的士專屬功能 (隧道費 overlay)
 */
class NaviActivity : ComponentActivity(), AMapNaviListener {

    private lateinit var naviView: AMapNaviView
    private lateinit var amapNavi: AMapNavi
    private lateinit var tollOverlay: TollOverlayView

    // 導航狀態
    private var isNavigating = false

    companion object {
        private const val TAG = "NaviActivity"

        fun start(context: Context) {
            context.startActivity(Intent(context, NaviActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 全屏佈局
        val container = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(container)

        amapNavi = HKTaxiNaviApp.instance.amapNavi

        // === 初始化導航地圖視圖 ===
        initNaviView(container)

        // === 隧道費 overlay (的士專屬) ===
        tollOverlay = TollOverlayView(this).apply {
            visibility = View.GONE
        }
        container.addView(tollOverlay)
    }

    private fun initNaviView(container: FrameLayout) {
        naviView = AMapNaviView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

            // === UI 設定 ===
            viewOptions = AMapNaviViewOptions().apply {
                isSettingMenuEnabled = false   // 隱藏設置按鈕
                isTiltGesturesEnabled = true   // 傾斜手勢
                isRotateGesturesEnabled = true // 旋轉手勢
                isAutoChangeZoom = true        // 自動縮放
                isTrafficLine = true           // 顯示路況
                isLeaderLineEnabled = true     // 車道引導線
                layoutVisible = true           // 顯示導航面板
                isAutoDrawRoute = true         // 自動繪製路線
                isCrossDisplayShow = true      // 路口放大圖
                isMonitorCameraEnabled = true  // 電子眼
            }

            onCreate(null)
            onResume()
        }

        // 添加到容器第一個 (底層)
        container.addView(naviView, 0)
    }

    // ============================================================
    // AMapNaviListener 回調
    // ============================================================

    override fun onCalculateRouteSuccess(routes: IntArray?) {
        Log.d(TAG, "路線規劃成功, 共 ${routes?.size ?: 0} 條路線")

        // 開始即時導航
        amapNavi.startNavi(NaviType.GPS)
        isNavigating = true
    }

    override fun onCalculateRouteFailure(errorInfo: AMapCalcRouteResult?) {
        val errorCode = errorInfo?.errorCode ?: -1
        Log.e(TAG, "路線規劃失敗, errorCode=$errorCode")

        runOnUiThread {
            when (errorCode) {
                2 -> Toast.makeText(this, "無可用路線，請檢查起終點", Toast.LENGTH_LONG).show()
                4 -> Toast.makeText(this, "API Key 無效或配額不足", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this, "路線規劃失敗 (code=$errorCode)", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onInitNaviSuccess() {
        Log.d(TAG, "導航引擎初始化成功")
    }

    override fun onInitNaviFailure() {
        Log.e(TAG, "導航引擎初始化失敗")
    }

    override fun onStartNavi(naviType: Int) {
        Log.d(TAG, "導航開始, type=$naviType")
    }

    /** 導航資訊更新 (路名、距離、路況) */
    override fun onNaviInfoUpdate(naviInfo: AMapNaviInfo?) {
        naviInfo ?: return
        // 可在這裡更新自定義 UI 面板
        Log.v(TAG, "導航: ${naviInfo.currentRoadName}, " +
                "剩餘 ${naviInfo.pathRetainDistance}m, " +
                "下個路口 ${naviInfo.nextRoadName}")
    }

    /** 🚗 車道引導 — 顯示當前路口的車道箭頭 */
    override fun showLaneInfo(laneInfo: AMapLaneInfo?) {
        laneInfo ?: return
        Log.d(TAG, "車道資訊: ${laneInfo.laneCount} 條車道")

        // laneInfo.laneItems 包含每條車道的資訊:
        // - laneType: 車道類型 (直行/左轉/右轉/掉頭)
        // - laneAction: 建議走哪條
        // SDK 會自動渲染車道箭頭，我們也可以在自定義面板顯示
    }

    override fun hideLaneInfo() {
        Log.d(TAG, "隱藏車道資訊")
    }

    /** 🏙️ 路口放大圖 */
    override fun showCross(cross: AMapNaviCross?) {
        cross ?: return
        // SDK 自動顯示路口放大圖
        Log.d(TAG, "路口放大圖: ${cross.bitmap?.width}x${cross.bitmap?.height}")
    }

    override fun hideCross() {
        Log.d(TAG, "隱藏路口放大圖")
    }

    /** 🛣️ 路況顏色條 */
    override fun updateTrafficStatus(statuses: Array<out AMapNaviTrafficStatus>?) {
        // SDK 自動更新路況條
    }

    /** 📷 電子眼 */
    override fun updateCameraInfo(cameraInfos: Array<out AMapNaviCameraInfo>?) {
        // SDK 自動顯示電子眼
    }

    /** 到達目的地 */
    override fun onArriveDestination(isLastNavi: Boolean) {
        Log.d(TAG, "到達目的地!")
        runOnUiThread {
            Toast.makeText(this, "已到達目的地 🎉", Toast.LENGTH_LONG).show()
        }
        // TODO: 觸發行程記錄 → taxi-ledger
    }

    // ============================================================
    // 其他必需回調 (空實現)
    // ============================================================

    override fun onLocationChange(location: AMapNaviLocation?) {}
    override fun onReCalculateRoute(trafficStatus: Int) {}
    override fun onReCalculateRouteForYaw(trafficStatus: Int) {}
    override fun onReCalculateRouteForTrafficJam(trafficStatus: Int) {}
    override fun onArrivedWayPoint(wayId: Int) {}
    override fun onGpsSignalState(state: Int) {}
    override fun onGpsOpenStatus(status: Boolean) {}
    override fun onNaviSetting() {}
    override fun onUpdateTrafficStatus(statuses: Array<out AMapNaviTrafficStatus>?) {}
    override fun onShowNormalCross(cross: AMapNaviCross?) {}
    override fun onHideCross() {}
    override fun onShowModeCross(cross: AMapModelCross?) {}
    override fun onHideModeCross() {}
    override fun onUpdateLaneInfo(laneInfo: Array<out AMapLaneInfo>?) {}
    override fun onUpdateTrafficFacility(facilities: Array<out AMapNaviTrafficFacilityInfo>?) {}
    override fun onUpdateTrafficFacility(facility: AMapNaviTrafficFacilityInfo?) {}
    override fun onTrafficEventUpdate(event: Array<out AMapTrafficEvent>?) {}
    override fun onEndEmulatorNavi() {}
    override fun onArriveDestination() {}
    override fun onPlayRing(ringType: Int) {}
    override fun onGetNavigationText(text: String?) {}
    override fun onGetNavigationText(i: Int, s: String?) {}
    override fun onStopSpeaking() {}
    override fun onNaviInfoUpdated(naviInfo: AMapNaviInfo?) {}
    override fun onCalculateRouteSuccess(result: AMapCalcRouteResult?) {}
    override fun onCalculateRouteFailure(result: AMapCalcRouteResult?) {}
    override fun onCalculateMultipleRoutesSuccess(routes: Array<out AMapNaviPath>?) {}
    override fun onCalculateMultipleRoutesFailure(result: AMapCalcRouteResult?) {}
    override fun onServiceAreaUpdate(serviceAreas: Array<out AMapServiceAreaInfo>?) {}
    override fun onBroadcastMode(broadcastMode: Int) {}
    override fun onShowLaneInfo(laneInfo: Array<out AMapLaneInfo>?) {}

    // ============================================================
    // Lifecycle
    // ============================================================

    override fun onResume() {
        super.onResume()
        if (::naviView.isInitialized) naviView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::naviView.isInitialized) naviView.onPause()
    }

    override fun onDestroy() {
        if (::naviView.isInitialized) {
            naviView.onDestroy()
        }
        if (isNavigating) {
            amapNavi.stopNavi()
        }
        super.onDestroy()
    }
}
