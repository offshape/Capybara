package frc.robot.subsystems.drivetrain

import com.ctre.phoenix6.Utils
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrainConstants
import com.ctre.phoenix6.mechanisms.swerve.SwerveModuleConstants
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest.*
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.util.HolonomicPathFollowerConfig
import com.pathplanner.lib.util.PIDConstants
import com.pathplanner.lib.util.ReplanningConfig
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.RobotController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Subsystem
import frc.robot.generated.TunerConstants
import java.util.function.Supplier
import kotlin.math.max

/**
 * Class that extends the Phoenix SwerveDrivetrain class and implements
 * subsystem, so it can be used in command-based projects easily.
 */
class CommandSwerveDrivetrain : SwerveDrivetrain, Subsystem {
    private var m_simNotifier: Notifier? = null
    private var m_lastSimTime = 0.0

    /* Blue alliance sees forward as 0 degrees (toward red alliance wall) */
    private val BlueAlliancePerspectiveRotation: Rotation2d = Rotation2d.fromDegrees(0.0)

    /* Red alliance sees forward as 180 degrees (toward blue alliance wall) */
    private val RedAlliancePerspectiveRotation: Rotation2d = Rotation2d.fromDegrees(180.0)

    /* Keep track if we've ever applied the operator perspective before or not */
    private var hasAppliedOperatorPerspective = false

    private val AutoRequest = ApplyChassisSpeeds()

    constructor(
        driveTrainConstants: SwerveDrivetrainConstants?,
        OdometryUpdateFrequency: Double,
        vararg modules: SwerveModuleConstants?
    ) : super(driveTrainConstants, OdometryUpdateFrequency, *modules) {
        configurePathPlanner()
        if (Utils.isSimulation()) {
            startSimThread()
        }
    }

    constructor(driveTrainConstants: SwerveDrivetrainConstants?, vararg modules: SwerveModuleConstants?) : super(
        driveTrainConstants,
        *modules
    ) {
        configurePathPlanner()
        if (Utils.isSimulation()) {
            startSimThread()
        }
    }

    private fun configurePathPlanner() {
        var driveBaseRadius = 0.0
        for (moduleLocation in m_moduleLocations) {
            driveBaseRadius = max(driveBaseRadius, moduleLocation.norm)
        }

        AutoBuilder.configureHolonomic(
            { this.state.Pose },  // Supplier of current robot pose
            { location: Pose2d? -> this.seedFieldRelative(location) },  // Consumer for seeding pose against auto
            { this.currentRobotChassisSpeeds },
            { speeds: ChassisSpeeds? -> this.setControl(AutoRequest.withSpeeds(speeds)) },  // Consumer of ChassisSpeeds to drive the robot
            HolonomicPathFollowerConfig(
                PIDConstants(10.0, 0.0, 0.0),
                PIDConstants(10.0, 0.0, 0.0),
                TunerConstants.kSpeedAt12VoltsMps,
                driveBaseRadius,
                ReplanningConfig()
            ),
            {
                DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red
            },  // Assume the path needs to be flipped for Red vs Blue, this is normally the case
            this
        ) // Subsystem for requirements
    }

    fun applyRequest(requestSupplier: Supplier<SwerveRequest?>): Command {
        return run { this.setControl(requestSupplier.get()) }
    }

    fun getAutoPath(pathName: String?): Command {
        return PathPlannerAuto(pathName)
    }

    private val currentRobotChassisSpeeds: ChassisSpeeds
        get() = m_kinematics.toChassisSpeeds(*state.ModuleStates)

    private fun startSimThread() {
        m_lastSimTime = Utils.getCurrentTimeSeconds()

        /* Run simulation at a faster rate so PID gains behave more reasonably */
        m_simNotifier = Notifier {
            val currentTime = Utils.getCurrentTimeSeconds()
            val deltaTime = currentTime - m_lastSimTime
            m_lastSimTime = currentTime

            /* use the measured time delta, get battery voltage from WPILib */
            updateSimState(deltaTime, RobotController.getBatteryVoltage())
        }

        m_simNotifier!!.setName("Swerve Simulation")
        m_simNotifier!!.startPeriodic(kSimLoopPeriod)
    }

    override fun periodic() {
        /* Periodically try to apply the operator perspective */
        /* If we haven't applied the operator perspective before, then we should apply it regardless of DS state */
        /* This allows us to correct the perspective in case the robot code restarts mid-match */
        /* Otherwise, only check and apply the operator perspective if the DS is disabled */
        /* This ensures driving behavior doesn't change until an explicit disable event occurs during testing*/
        if (!hasAppliedOperatorPerspective || DriverStation.isDisabled()) {
            DriverStation.getAlliance().ifPresent { allianceColor: Alliance ->
                this.setOperatorPerspectiveForward(
                    if (allianceColor == Alliance.Red) RedAlliancePerspectiveRotation
                    else BlueAlliancePerspectiveRotation
                )
                hasAppliedOperatorPerspective = true
            }
        }
    }

    companion object {
        private const val kSimLoopPeriod = 0.005 // 5 ms
    }
}
