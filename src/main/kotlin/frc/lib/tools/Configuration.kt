package frc.lib.tools

import edu.wpi.first.networktables.NetworkTableInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

interface Configuration {
    private fun buildMap(scanObject : Any, directory: String = ""): Map<String, String> {
        val map = mutableMapOf<String, String>()
        for (property in scanObject::class.memberProperties) {
            property.isAccessible = true
            if (property.isConst) {
                map[directory + property.name] = property.getter.call().toString()
            }
        }

        for (property in scanObject::class.nestedClasses) {
            val newDirectory = directory + property.simpleName + "/"
            map.putAll(buildMap(property.objectInstance!!, newDirectory))
        }

        return map
    }

    fun toMap() : Map<String, String> {
        return buildMap(this)
    }

    fun publishNT() {
        val ntInstance = NetworkTableInstance.getDefault()

        for ((key, value) in toMap()) {
            ntInstance.getEntry("/Metadata/Config/$key").setString(value)
        }
    }

    fun publishNTHash() {
        // TODO: Add string for entry customization (ex multiple configs would be /Metadata/ConfigHash/Swerve, /Metadata/ConfigHash/Manipulators)
        val ntInstance = NetworkTableInstance.getDefault()

        ntInstance.getEntry("/Metadata/ConfigHash").setString(toMap().hashCode().toString())
    }
}