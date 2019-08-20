package net.ntworld.foundation.generator.setting

import kotlinx.serialization.Serializable
import net.ntworld.foundation.generator.type.ClassInfo

// ----------------------------------------------------------------------------------
// There are 3 kinds of source code based on the settings:
//   1. Simple abstract class
//      - enum: Type.ABSTRACT_FACTORY
//      - aggregate: required
//      - state: required
//      - implementation: required
//      - isAbstract = true
//      - isEventSourced = false
//
//   2. EventSourcing: Wrapper of existing factory perform event sourced aggregate
//      - enum: Type.WRAPPER_FACTORY_WITH_EVENT_SOURCED
//      - aggregate: required
//      - state: required
//      - implementation: required
//      - isAbstract = false
//      - isEventSourced = true
//
//   3. EventSourcing: Abstract class perform event sourced aggregate
//      - enum: Type.ABSTRACT_FACTORY_WITH_EVENT_SOURCED
//      - aggregate: required
//      - state: required
//      - implementation: required
//      - isAbstract = true
//      - isEventSourced = true
// ----------------------------------------------------------------------------------
// How to detect and create settings in processor?
//   type 1: @Implementation
//   type 2: @EventSourced
//   type 3: @Implementation + @EventSourced
// ----------------------------------------------------------------------------------
// Register to AutoGeneratedInfrastructureProvider:
//   type 1: not register
//   type 3: not register
//   type 2: register under "wrapper" style automatically -> because of this point,
//           AutoGeneratedInfrastructureProvider should always on top of chain
// ----------------------------------------------------------------------------------

@Serializable
data class AggregateFactorySettings(
    val aggregate: ClassInfo,
    val state: ClassInfo,
    val implementation: ClassInfo,
    val isAbstract: Boolean,
    val isEventSourced: Boolean
) {
    enum class Type {
        ABSTRACT_FACTORY,
        WRAPPER_FACTORY_WITH_EVENT_SOURCED,
        ABSTRACT_FACTORY_WITH_EVENT_SOURCED,
    }
}
