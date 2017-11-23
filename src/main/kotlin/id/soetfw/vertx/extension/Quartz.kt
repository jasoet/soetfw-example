package id.soetfw.vertx.extension

import kotlinx.coroutines.experimental.launch
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.Job
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.TriggerBuilder
import kotlin.coroutines.experimental.CoroutineContext

/**
 * [Documentation Here]
 *
 * @author Deny Prasetyo.
 */


fun job(coroutineContext: CoroutineContext, executable: suspend (JobExecutionContext) -> Unit): Job {
    return Job { jobContext ->
        launch(coroutineContext) {
            executable(jobContext)
        }
    }
}

inline fun <reified T : Job> job(vararg data: Pair<String, Any>): JobBuilder {
    val jobDataMap = data.fold(JobDataMap()) { map, (key, value) ->
        map.put(key, value)
        map
    }
    return JobBuilder.newJob(T::class.java).setJobData(jobDataMap)
}

fun cronTrigger(cronExpression: String): TriggerBuilder<CronTrigger> {
    return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
}

inline fun <reified T : Any> JobExecutionContext?.getData(key: String, default: T? = null, missing: (String) -> Unit): T {
    val value = this?.jobDetail?.jobDataMap?.get(key) as T?
    return when {
        value != null -> value
        default != null -> default
        else -> {
            missing(key)
            throw IllegalStateException("$key object is missing from job data")
        }
    }
}