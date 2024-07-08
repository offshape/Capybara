package frc.lib.led.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import frc.lib.led.Animation

class Rainbow(
    private val brightness: Int = 240,
    private val hueChange: Int = 3
) : Animation {
    private var hue = 0

    override fun onStart() {
        hue = 0
    }

    override fun onUpdate(buffer: AddressableLEDBuffer, startingIndex: Int, endingIndex: Int) {
        for (i in startingIndex..endingIndex) {
            val hue = (this.hue + (i * 180 / (endingIndex - startingIndex))) % 180
            buffer.setHSV(i, hue, 255, brightness)
        }

        hue += hueChange
        hue %= 180
    }

    override fun toString(): String {
        return "RAINBOW bh($brightness, $hueChange)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Rainbow

        if (brightness != other.brightness) return false
        if (hueChange != other.hueChange) return false

        return true
    }
}