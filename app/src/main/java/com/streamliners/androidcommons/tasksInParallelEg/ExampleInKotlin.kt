package com.streamliners.androidcommons.tasksInParallelEg

import com.streamliners.commons.tasksInParallel.Task
import com.streamliners.commons.tasksInParallel.TasksInParallelHelper
import com.streamliners.commons.tasksInParallel.TasksInParallelHelper.TasksListener

fun example() {

    // Prepare a list of inputs of type A
    val inputs = listOf(A(1), A(2), A(3))

    // Implement Task class to transform A into B
    val task: Task<A, B> =
        object : Task<A, B>() {
            override fun perform(input: A, listener: Listener<B>) {
                // Simple stupid task that converts int to String
                val output = B(input.x.toString())
                listener.onSuccess(output)
            }
        }

    // Implement listener to get outputs of type B
    val listener: TasksListener<B> =
        object : TasksListener<B> {
            override fun onSuccess(outputs: List<B>) {
                // Do something with the outputs
            }

            override fun onFailure(error: String) {
                // Show the error!
            }
        }

    // Finally perform the tasks in parallel!
    TasksInParallelHelper<A, B>()
        .doTasksInParallel(inputs, task, listener)
}