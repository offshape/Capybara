package frc.robot.subsystems.indicators

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.RobotState
import frc.lib.led.Animation
import frc.lib.led.LightController
import frc.lib.led.LightSection
import frc.lib.led.animations.*
import frc.lib.queue.ExecutionLadder
import frc.robot.Interfaces
import monologue.Logged

class Neopixels : ExecutionLadder<Animation>(arrayOf(
    {
        if (DriverStation.isEStopped()) Flash(1) else null
    },
    {
        if (RobotState.isDisabled()) {
            when {
                Interfaces.power.voltage < 12.5 && RobotBase.isReal() -> Pulse(1, 1.0)
                !DriverStation.isDSAttached() -> Pulse(15, 1.0)
                DriverStation.isFMSAttached() -> Zip(Triple(0, 200, 255), 25, 3)
                else -> Pulse(120, 0.5)
            }
        } else null
    },
    {
        if (RobotState.isAutonomous()) Rainbow() else null
    },
    {
        Flash(2, Triple(0, 255, 0))
    }
)), Logged {
    private val fullSection = LightSection(0, 58, Solid())
    private val controller = LightController(0, 59, arrayOf(fullSection), 20)

    init {
        name = "Neopixel Ladder Controller"

        setOnChange { animation ->
            fullSection.animation = animation
            log("currentAnimation", animation.toString())
        }
    }

    override fun getPath(): String = "Neopixels"
}