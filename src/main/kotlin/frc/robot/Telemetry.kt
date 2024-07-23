package frc.robot

import com.ctre.phoenix6.SignalLogger
import com.ctre.phoenix6.Utils
import com.ctre.phoenix6.mechanisms.swerve.SwerveDrivetrain.SwerveDriveState
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.networktables.*
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import monologue.Annotations.Log
import monologue.LogLevel
import monologue.Logged

/**
 * Construct a telemetry object, with the specified max speed of the robot
 *
 * @param maxSpeed Maximum speed in meters per second
 */
class Telemetry(private val MaxSpeed: Double) : Logged {
    /* What to publish over networktables for telemetry */
    var lastPose = Pose2d()

    @Log(key = "Odometetry/Position", level = LogLevel.DEFAULT)
    private var robotPosition = Field2d()

    @Log(key = "Swerve/Velocity X", level = LogLevel.DEFAULT)
    private var velocityX: Double = 0.0

    @Log(key = "Swerve/Velocity Y", level = LogLevel.DEFAULT)
    private var velocityY: Double = 0.0

    @Log(key = "Swerve/Speed", level = LogLevel.DEFAULT)
    private var speed: Double = 0.0

    @Log(key = "Odometetry/Frequency", level = LogLevel.DEFAULT)
    private var odomFreq: Double = 0.0

    @Log(key = "Swerve/Module States", level = LogLevel.DEFAULT)
    private var swerveState: Array<SwerveModuleState> = arrayOf()

    @Log(key = "Swerve/Module Target States", level = LogLevel.DEFAULT)
    private var targetSwerve : Array<SwerveModuleState> = arrayOf()

    /* Keep a reference of the last pose to calculate the speeds */
    private var m_lastPose = Pose2d()
    private var lastTime = Utils.getCurrentTimeSeconds()

    init {
        SignalLogger.start()
    }

    /* Accept the swerve drive state and telemeterize it to smartdashboard */
    fun telemeterize(state: SwerveDriveState) {
        val pose = state.Pose
        lastPose = pose

        robotPosition.robotPose = pose

        /* Telemeterize the robot's general speeds */
        val currentTime = Utils.getCurrentTimeSeconds()
        val diffTime = currentTime - lastTime
        lastTime = currentTime
        val distanceDiff = pose.minus(m_lastPose).translation
        m_lastPose = pose

        val velocities = distanceDiff.div(diffTime)

        speed = (velocities.norm)
        velocityX = (velocities.x)
        velocityY = (velocities.y)
        odomFreq = (1.0 / state.OdometryPeriod)
        swerveState = (state.ModuleStates)
        targetSwerve = (state.ModuleTargets)

        SignalLogger.writeDoubleArray("odometry", doubleArrayOf(pose.x, pose.y, pose.rotation.degrees)) // Just in case
        SignalLogger.writeDouble("odom period", state.OdometryPeriod, "seconds")
    }

    override fun getPath(): String = "Telemetry"
}
