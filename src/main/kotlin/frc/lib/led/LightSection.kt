package frc.lib.led

import edu.wpi.first.wpilibj.AddressableLEDBuffer

class LightSection(
    private var startingIndex: Int,
    private var endingIndex: Int,
    startingAnimation: Animation
) {
    private var currentAnimation: Animation = startingAnimation

    var animation
        get() = currentAnimation
        set(value) {
            value.onStart()
            currentAnimation = value
        }

    fun update(buffer: AddressableLEDBuffer) {
        currentAnimation.onUpdate(buffer, startingIndex, endingIndex)
    }

    fun changeSection(startingIndex: Int, endingIndex: Int) {
        this.startingIndex = startingIndex
        this.endingIndex = endingIndex
    }
}