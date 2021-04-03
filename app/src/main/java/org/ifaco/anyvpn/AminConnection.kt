package org.ifaco.anyvpn

import org.ifaco.anyvpn.Amin.Companion.builder
import org.ifaco.anyvpn.Amin.Companion.tunnel
import org.ifaco.anyvpn.Amin.Companion.vpnStop
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.channels.DatagramChannel

class AminConnection : Thread() {
    override fun run() {
        try {
            tunnel = builder
                // Required: IP Port User Pass
                // User: ALI ; Pass: MAN@+@1318
                .addAddress("176.9.200.99", 24)
                .addRoute("0.0.0.0", 0)
                //.addDnsServer("192.168.1.1")// For Special VPNs
                .establish()

            if (tunnel == null) Amin.me?.stopSelf()
            var inn = FileInputStream(tunnel!!.fileDescriptor)
            var out = FileOutputStream(tunnel!!.fileDescriptor)
            var channel = DatagramChannel.open()
            channel.connect(InetSocketAddress("127.0.0.1", 808))
            Amin.me?.protect(channel.socket())
            while (true) {
                sleep(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                vpnStop()
            } catch (e: Exception) {
            }
        }
    }
}