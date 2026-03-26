package mai_onsyn.trisona.core

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val Any.log: Logger
    get() = LogManager.getLogger(this.javaClass)