package frc.lib.led.animations

import edu.wpi.first.wpilibj.AddressableLEDBuffer
import frc.lib.led.Animation

class Zip(
    private val color: Triple<Int, Int, Int> = Triple(255, 0, 0),
    private val length: Int = 10,
    private val speed: Int = 1
) : Animation {
    private var currentPosition = 0 // Starting position of the snake's head

    override fun onStart() {
        currentPosition = 0 // Reset the snake's position at the start
    }

    override fun onUpdate(buffer: AddressableLEDBuffer, startingIndex: Int, endingIndex: Int) {
        // Clear the strip before drawing the zip
        for (i in startingIndex..endingIndex) {
            buffer.setRGB(i, 0, 0, 0)
        }

        // Increment currentPosition, wrap around if it exceeds endingIndex
        currentPosition += speed
        if (currentPosition > endingIndex) {
            currentPosition = startingIndex + (currentPosition - endingIndex - 1) % (endingIndex - startingIndex + 1)
        }

        // Draw the zip, taking into account the wrap-around for both ends
        for (i in 0 until length) {
            val position = currentPosition - i
            if (position in startingIndex..endingIndex) {
                buffer.setRGB(position, color.first, color.second, color.third)
            } else if (position < startingIndex) {
                // Calculate wrap-around position for the start of the strip
                val wrapAroundPosition = endingIndex - (startingIndex - position - 1) % (endingIndex - startingIndex + 1)
                if (wrapAroundPosition >= startingIndex && wrapAroundPosition <= endingIndex) {
                    buffer.setRGB(wrapAroundPosition, color.first, color.second, color.third)
                }
            }
        }
    }

    override fun toString(): String {
        return "ZIP cls($color, $length, $speed)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Zip

        if (color != other.color) return false
        if (length != other.length) return false
        if (speed != other.speed) return false

        return true
    }
}