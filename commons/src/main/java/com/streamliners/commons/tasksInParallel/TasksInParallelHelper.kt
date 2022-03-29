package com.streamliners.commons.tasksInParallel

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Task class that takes input of type I & generates output of type O
abstract class Task<I, O> {

    // Function to actually perform the task & call the listener in the end
    abstract fun perform(input: I, listener: Listener<O>)

    // Listener for callbacks
    interface Listener<O> {
        fun onSuccess(output: O)
        fun onFailure(error: String?)
    }
}

/**
 * TasksInParallelHelper is a helper class that performs task in parallel.
 * Any input type (I) & output type (O) is supported.
 * By Implementing task class, you can perform ny task on I to generate O.
 * Whether it be a long IO operation or a network call.
 */
class TasksInParallelHelper<I, O> {

    // Main function that takes inputs (listOf<I>), task & TasksListener to get outputs (listOf<O>)
    fun doTasksInParallel(
        inputs: List<I>,
        task: Task<I, O>,
        listener: TasksListener<O>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Tasks on inputs are performed in parallel using async-await
                val outputs = inputs
                    .map { input ->
                        async {
                            suspendTask(task, input)
                        }
                    }
                    .awaitAll()
                listener.onSuccess(outputs)
            } catch (e: Exception) {
                listener.onFailure(e.message.toString())
            }
        }
    }

    // Converts callback function into suspending function
    private suspend fun suspendTask(task: Task<I, O>, input: I) =
        suspendCoroutine<O> { continuation ->
            task.perform(input, object : Task.Listener<O> {
                override fun onSuccess(output: O) {
                    continuation.resume(output)
                }

                override fun onFailure(error: String?) {
                    throw Exception(error)
                }
            })
        }

    // TasksListener to get outputs
    interface TasksListener<O> {
        fun onSuccess(outputs: List<O>)
        fun onFailure(error: String)
    }

}