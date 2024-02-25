package uk.co.accsoft.util

import android.util.Log
import uk.co.accsoft.util.extensions.TAG
import uk.co.accsoft.util.extensions.toReadableString

/**
 * Custom logger class, which only posts logs to [Log] if the current build variant is DEBUG.
 */
@Suppress("unused")
object AzLog {

    private const val DEFAULT_MESSAGE_SEPARATOR = ", "
    private const val EMPTY_MESSAGE_SUBSTITUTE = "_"
    private const val ERROR_CODE = -1

    private val LOG_TAG = TAG

    var isBuildConfigDebug = false


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////  CUSTOMISATIONS SECTION  ////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var includeFullStackTrace = false
    private var separator = DEFAULT_MESSAGE_SEPARATOR
    private var title = ""

    /**
     * Print the full stack trace for the next log message only.
     */
    @JvmStatic
    fun fullStackTrace(): AzLog {
        if (isBuildConfigDebug) {
            includeFullStackTrace = true
        }
        return this
    }

    /**
     * Print the next log message (only) using a custom separator between the objects.
     */
    @JvmStatic
    fun separator(separator: String): AzLog {
        if (isBuildConfigDebug && this.separator != separator) {
            this.separator = separator
        }
        return this
    }

    /**
     * Print the next log message (only) with a title at the beginning.
     */
    @JvmStatic
    fun title(title: String): AzLog {
        if (isBuildConfigDebug) {
            this.title = title
        }
        return this
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////  LOGS SECTION  ///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * VERBOSE level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun verbose(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.v(LOG_TAG, buildMessage(objects))
    }

    /**
     * DEBUG level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun debug(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.d(LOG_TAG, buildMessage(objects))
    }

    /**
     * INFO level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun info(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.i(LOG_TAG, buildMessage(objects))
    }

    /**
     * WARNING level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun warning(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.w(LOG_TAG, buildMessage(objects))
    }

    /**
     * ERROR level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun error(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.e(LOG_TAG, buildMessage(objects))
    }

    /**
     * ASSERT level log.
     *
     * @param objects Objects used to build the log message.
     */
    @JvmStatic
    fun assert(vararg objects: Any?) {
        if (isBuildConfigDebug)
            Log.wtf(LOG_TAG, buildMessage(objects))
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////  MESSAGE BUILD SECTION  ////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param messageParts Objects to be fused into a readable log message.
     * @return The message to display in the log.
     */
    private fun buildMessage(messageParts: Array<out Any?>?): String =
        StringBuilder()
            .appendMessageParts(messageParts)
            .appendStackTrace()
            .toString()

    private fun StringBuilder.appendMessageParts(messageParts: Array<out Any?>?): StringBuilder {
        if (title.isNotEmpty()) {
            append(title)
            title = ""
        }

        if (messageParts.isNullOrEmpty()) {
            append(EMPTY_MESSAGE_SUBSTITUTE)
        } else {
            append(messageParts[0].toReadableString())
            if (messageParts.size > 1)
                for (i in 1 until messageParts.size)
                    append(separator).append(messageParts[i].toReadableString())
        }

        if (separator != DEFAULT_MESSAGE_SEPARATOR)
            separator = DEFAULT_MESSAGE_SEPARATOR

        return this@appendMessageParts
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////  STACK TRACE SECTION  /////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var callingFunctionStackIndex = ERROR_CODE

    private fun StringBuilder.appendStackTrace(): StringBuilder {
        val elements = Thread.currentThread().stackTrace

        when {
            findCallingFunctionStackIndex(elements).not() -> {} // Do nothing.
            includeFullStackTrace -> {
                for (i in callingFunctionStackIndex until elements.size)
                    append("\n").append(stackTraceName(elements[i]))
            }
            else -> append("\n").append(stackTraceName(elements[callingFunctionStackIndex]))
        }

        if (includeFullStackTrace)
            includeFullStackTrace = false

        return this@appendStackTrace
    }

    private fun findCallingFunctionStackIndex(elements: Array<StackTraceElement>): Boolean {
        if (callingFunctionStackIndex != ERROR_CODE)
            return true

        val thisClassName = this@AzLog.javaClass.name
        elements.forEachIndexed { index, stackTraceElement ->
            if (thisClassName == stackTraceElement.className) {
                callingFunctionStackIndex = index + 1
                return true
            }
        }

        return false
    }

    private fun stackTraceName(ste: StackTraceElement): String =
        "at ${ste.className}.${ste.methodName}()"

}