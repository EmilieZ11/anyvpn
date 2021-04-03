package org.ifaco.anyvpn

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout

class Panel : AppCompatActivity() {
    lateinit var body: ConstraintLayout
    lateinit var connect: SwitchCompat

    companion object {
        lateinit var c: Context

        fun intentVPN(action: String) = Intent(c, Amin::class.java).setAction(action)
        fun startVPN() = c.startService(intentVPN(Amin.ACTION_CONNECT))
        fun stopVPN() = c.startService(intentVPN(Amin.ACTION_DISCONNECT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.panel)

        body = findViewById(R.id.body)
        connect = findViewById(R.id.connect)

        c = applicationContext

        // Connect VPN
        connect.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                var vpnState = VpnService.prepare(c)
                if (vpnState == null) onActivityResult(0, RESULT_OK, null)
                else startActivityForResult(vpnState, 0)
            } else Amin.me?.stopSelf()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) startVPN()
            else failed(0)
        }
    }

    fun failed(i: Int) {
        Toast.makeText(c, "FAILED $i", Toast.LENGTH_LONG).show()
    }
}