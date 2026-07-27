package com.nicklam.hktaxinavi.overlay

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.nicklam.hktaxinavi.data.toll.TunnelToll
import com.nicklam.hktaxinavi.data.toll.TollCalculator

/**
 * 隧道費 Overlay — 導航中浮動顯示經過的隧道費用
 *
 * 當 GPS 接近隧道入口時顯示，通過後自動累計
 */
class TollOverlayView(context: Context) : LinearLayout(context) {

    private val tollCalculator = TollCalculator()
    private val tollText: TextView
    private val totalText: TextView

    private var totalToll = 0.0

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        setBackgroundColor(Color.argb(200, 0, 0, 0))
        setPadding(24, 16, 24, 16)

        tollText = TextView(context).apply {
            text = ""
            textSize = 18f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        totalText = TextView(context).apply {
            text = "累計隧道費: $0.00"
            textSize = 14f
            setTextColor(Color.argb(255, 255, 200, 0)) // 金色
            gravity = Gravity.CENTER
        }

        addView(tollText)
        addView(totalText)
    }

    /**
     * 偵測到即將經過隧道時調用
     */
    fun onApproachingTunnel(tunnel: TunnelToll) {
        tollText.text = "🚇 ${tunnel.name}\n隧道費: $${tunnel.fee}"
        visibility = VISIBLE
        Log.d("TollOverlay", "接近隧道: ${tunnel.name}, 費用: $${tunnel.fee}")
    }

    /**
     * 確認通過隧道時調用
     */
    fun onPassTunnel(tunnel: TunnelToll) {
        totalToll += tunnel.fee
        totalText.text = "累計隧道費: $${String.format("%.1f", totalToll)}"
        Log.d("TollOverlay", "通過隧道: ${tunnel.name}, 累計: $${totalToll}")
    }

    /**
     * 離開隧道範圍時調用
     */
    fun onLeaveTunnel() {
        tollText.text = ""
        if (totalToll == 0.0) {
            visibility = GONE
        }
        // 保留累計顯示
    }

    /** 獲取總隧道費 */
    fun getTotalToll(): Double = totalToll

    /** 重置累計 */
    fun reset() {
        totalToll = 0.0
        totalText.text = "累計隧道費: $0.00"
        tollText.text = ""
        visibility = GONE
    }
}
