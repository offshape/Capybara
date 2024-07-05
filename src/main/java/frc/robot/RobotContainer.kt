// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot

import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain.SwerveDriveState
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.button.CommandXboxController

// Swerve Stuff
import frc.robot.subsystems.drivetrain.Swerve.MaxSpeed
import frc.robot.subsystems.drivetrain.Swerve.MaxAngularRate
import frc.robot.subsystems.drivetrain.Swerve.brake
import frc.robot.subsystems.drivetrain.Swerve.drive
import frc.robot.subsystems.drivetrain.Swerve.drivetrain
import frc.robot.subsystems.drivetrain.Swerve.seedField

// Logging
import monologue.Logged

class RobotContainer : Logged {
    private val joystick = CommandXboxController(0)

    val autonomousCommand: Command = drivetrain.getAutoPath("Example")

    private val logger = Telemetry(MaxSpeed)

    private fun configureSwerve() {
        drivetrain.defaultCommand = drivetrain.applyRequest {
            drive.withVelocityX(-joystick.leftY * MaxSpeed)
                .withVelocityY(-joystick.leftX * MaxSpeed)
                .withRotationalRate(-joystick.rightX * MaxAngularRate)
        }

        drivetrain.registerTelemetry { state: SwerveDriveState? -> logger.telemeterize(state!!) }

        joystick.a().whileTrue(brake)
        joystick.leftBumper().onTrue(seedField)
    }

    init {
        configureSwerve()
    }
}
