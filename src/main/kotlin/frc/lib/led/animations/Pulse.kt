package frc.lib.led.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import frc.lib.led.Animation
import kotlin.math.sin

class Pulse(
    private val color: Int,
    private val speed: Double = 0.02
) : Animation {
    private var brightness = 0.0
    private var time = 0.0

    override fun onStart() {
        brightness = 0.0
        time = 0.0
    }

    override fun onUpdate(buffer: AddressableLEDBuffer, startingIndex: Int, endingIndex: Int) {
        brightness = (sin(time) + 1) / 2 * 255 // map sine wave (-1 to 1) to brightness (0 to 255)
        for (i in startingIndex..endingIndex) {
            buffer.setHSV(i, color, 255, brightness.toInt())
        }
        time += speed
    }

    override fun toString(): String {
        return "PULSE cs($color, $speed)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Pulse

        if (color != other.color) return false
        if (speed != other.speed) return false

        return true
    }
}