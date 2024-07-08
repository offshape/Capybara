package frc.lib.led.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import frc.lib.led.Animation

class Flash(private val periodLength: Int, private val color: Triple<Int, Int, Int> = Triple(255, 255, 255)) :
    Animation {
    private var ticks = 0

    override fun onStart() {
        ticks = 0
    }

    override fun onUpdate(buffer: AddressableLEDBuffer, startingIndex: Int, endingIndex: Int) {
        val isOn = (ticks / periodLength) % 2 == 0
        for (i in startingIndex..endingIndex) {
            if (isOn) {
                buffer.setRGB(i, color.first, color.second, color.third) // Set to the specified color when on
            } else {
                buffer.setRGB(i, 0, 0, 0) // Set to black color when off
            }
        }
        ticks++
    }

    override fun toString(): String {
        return "FLASH rgbf(${color.first}, ${color.second}, ${color.third}, $periodLength)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Flash

        if (periodLength != other.periodLength) return false
        if (color != other.color) return false

        return true
    }
}