package com.streamliners.androidcommons.tasksInParallelEg;

import androidx.annotation.NonNull;

import com.streamliners.commons.tasksInParallel.Task;
import com.streamliners.commons.tasksInParallel.TasksInParallelHelper;

import java.util.Arrays;
import java.util.List;

public class ExampleInJava {

    private void example() {

        // Prepare a list of inputs of type A
        List<A> inputs = Arrays.asList(new A(1), new A(2), new A(3));

        // Implement Task class to transform A into B
        Task<A, B> task =
                new Task<A, B>() {
                    @Override
                    public void perform(A input, @NonNull Listener<B> listener) {

                        // Simple stupid task that converts int to String
                        B output = new B(String.valueOf(input.getX()));
                        listener.onSuccess(output);
                    }
                };

        // Implement listener to get outputs of type B
        TasksInParallelHelper.TasksListener<B> listener =
                new TasksInParallelHelper.TasksListener<B>() {
                    @Override
                    public void onSuccess(@NonNull List<? extends B> outputs) {
                        // Do something with the outputs
                    }

                    @Override
                    public void onFailure(@NonNull String error) {
                        // Show the error!
                    }
                };

        // Finally perform the tasks in parallel!
        new TasksInParallelHelper<A, B>()
                .doTasksInParallel(inputs, task, listener);
    }

}
