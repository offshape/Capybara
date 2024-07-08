package frc.robot.subsystems.indicators

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotState
import frc.lib.led.Animation
import frc.lib.led.LightController
import frc.lib.led.LightSection
import frc.lib.led.animations.Flash
import frc.lib.led.animations.Pulse
import frc.lib.led.animations.Rainbow
import frc.lib.led.animations.Solid
import frc.lib.queue.ExecutionLadder
import frc.robot.Interfaces

class Neopixels : ExecutionLadder<Animation>(listOf(
    { // Check if E-Stop is pressed
        if (DriverStation.isEStopped()) Flash(1) else null
    },
    { // Neopixel Animations when Disabled
        if (RobotState.isDisabled()) {
            when {
                Interfaces.power.voltage < 12.5 -> Pulse(40)
                !DriverStation.isFMSAttached() -> Pulse(80)
                else -> Pulse(120)
            }
        } else null
    },
    {
        if (RobotState.isAutonomous()) Rainbow() else null
    },
    {
        Solid()
    }
)) {
    private val fullSection = LightSection(0, 59, Solid())
    private val controller = LightController(0, 59, arrayOf(fullSection))

    init {
        setOnChange { animation ->
            fullSection.animation = animation
        }
    }
}