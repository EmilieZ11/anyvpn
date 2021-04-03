package org.ifaco.anyvpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor

class Amin : VpnService() {

    companion object {
        lateinit var builder: VpnService.Builder

        const val ACTION_CONNECT = "org.ifaco.anyvpn.START"
        const val ACTION_DISCONNECT = "org.ifaco.anyvpn.STOP"

        var me: Amin? = null
        var thread: AminConnection? = null
        var tunnel: ParcelFileDescriptor? = null

        fun vpnStop() {
            thread?.interrupt()
            thread = null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return if (intent != null && intent.action == ACTION_CONNECT) {
            me = this
            builder = Builder()
            thread = AminConnection()
            thread!!.start()
            START_STICKY
        } else {
            vpnStop()
            stopSelf()//stopForeground(true)
            START_NOT_STICKY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vpnStop()
    }
}
