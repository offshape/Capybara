package frc.lib.led

import edu.wpi.first.wpilibj.AddressableLED
import edu.wpi.first.wpilibj.AddressableLEDBuffer
import edu.wpi.first.wpilibj.Notifier

class LightController(
    private val ledPin : Int,
    private val ledCount : Int,
    private val section: Array<LightSection>,
    private val updateFreqHz : Int = 10
) {
    private val ledChannel = AddressableLED(ledPin)
    private var ledBuffer = AddressableLEDBuffer(ledCount)
    private val notifier = Notifier(this::update)

    init {
        ledChannel.setLength(ledCount)
        ledChannel.start()

        notifier.setName("LightController")
        notifier.startPeriodic(1.0 / updateFreqHz)
    }

    private fun update() {
        for (i in section.indices) {
            section[i].update(ledBuffer)
        }

        syncBuffer()
    }

    private fun syncBuffer() {
        ledChannel.setData(ledBuffer)
    }
}