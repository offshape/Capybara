package frc.robot.subsystems.drivetrain

import com.ctre.phoenix6.Orchestra
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest.FieldCentric
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest.SwerveDriveBrake
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.generated.TunerConstants

object Swerve {
    const val MaxSpeed = TunerConstants.kSpeedAt12VoltsMps
    const val MaxAngularRate = 1.5 * Math.PI

    val drivetrain: CommandSwerveDrivetrain = TunerConstants.DriveTrain

    val drive: FieldCentric = FieldCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage)

    val seedField: Command = drivetrain.runOnce { drivetrain.seedFieldRelative() }
    val brake = drivetrain.applyRequest { SwerveDriveBrake() }

    val orchestra = Orchestra()

    init {
        // TODO: Make Swerve configure AudioConfigs and better current limiting rather than just a stator limit
        for (i in 0 until 4) {
            val module = drivetrain.getModule(i)

            orchestra.addInstrument(module.driveMotor)
            orchestra.addInstrument(module.steerMotor)
        }
    }
}