// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.auto.Paths
import frc.robot.generated.TunerConstants
import frc.robot.subsystems.drivetrain.Swerve
import monologue.Logged
import monologue.Monologue

object Robot : TimedRobot(), Logged {
    private var m_autonomousCommand: Command? = null
    private var m_robotContainer: RobotContainer? = null

    object Logging {
        const val fileOnly = false
        const val lazyLogging = false
    }

    object Info {
        const val name = "Vipa"
        const val codeRevision = "1.0.0"
        const val buildRevision = "1.0.0"

        fun publishMetadata() {
            val ntInstance = NetworkTableInstance.getDefault()

            ntInstance.getStringTopic("/Metadata/Robot Name").publish().set(name)
            ntInstance.getStringTopic("/Metadata/Code Revision").publish().set(codeRevision)
            ntInstance.getStringTopic("/Metadata/Build Revision").publish().set(buildRevision)
        }
    }

    private fun loadResources() {
        TunerConstants
        Constants
        Interfaces
        Paths
        State
    }

    override fun robotInit() {
        loadResources()

        Swerve // Make drivetrain independent of other robot functions

        m_robotContainer = RobotContainer()

        Monologue.setupMonologue(m_robotContainer, "Robot", Logging.fileOnly, Logging.lazyLogging)

        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, FRCNetComm.tInstances.kLanguage_Kotlin) // Flex on the FTAs
        HAL.report(FRCNetComm.tResourceType.kResourceType_Kinematics, FRCNetComm.tInstances.kKinematics_SwerveDrive) // Might be useful i dunno

        Swerve.drivetrain.daqThread.setThreadPriority(99) // CTRE Sauce for the Swerve Thread

        Info.publishMetadata()
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()

        Monologue.setFileOnly(DriverStation.isFMSAttached()) // Turn off NT4 Publishing when at a competition
        Monologue.updateAll()
    }

    override fun disabledInit() {}

    override fun disabledPeriodic() {}

    override fun disabledExit() {}

    override fun autonomousInit() {
        m_autonomousCommand = m_robotContainer!!.autonomousCommand

        m_autonomousCommand?.schedule()
    }

    override fun autonomousPeriodic() {}

    override fun autonomousExit() {}

    override fun teleopInit() {
        if (m_autonomousCommand != null) {
            m_autonomousCommand!!.cancel()
        }
    }

    override fun teleopPeriodic() {}

    override fun teleopExit() {
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}

    override fun testExit() {}

    override fun simulationPeriodic() {}
}
