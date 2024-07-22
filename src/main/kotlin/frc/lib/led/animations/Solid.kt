package frc.lib.led.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import frc.lib.led.Animation

class Solid(
    private val color: Triple<Int, Int, Int> = Triple(255, 255, 255)
) : Animation {
    override fun onStart() {} // Not needed

    override fun onUpdate(buffer: AddressableLEDBuffer, startingIndex: Int, endingIndex: Int) {
        for (i in startingIndex..endingIndex) {
            buffer.setRGB(i, color.first, color.second, color.third)
        }
    }

    override fun toString(): String {
        return "SOLID rgb(${color.first}, ${color.second}, ${color.third})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Solid

        if (color != other.color) return false

        return true
    }
}