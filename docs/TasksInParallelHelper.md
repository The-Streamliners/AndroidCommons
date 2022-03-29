# TasksInParallelHelper

TasksInParallelHelper is a helper class that performs task in parallel. Any input type (I) & output type (O) is supported. By Implementing task class, you can perform any task on I to generate O, whether it be a long IO operation or a network call.

---

## Kotlin example

Step 1: Prepare a list of inputs of any input type A

```kotlin
val inputs = listOf(A(1), A(2), A(3))
```

Step 2: Implement Task class to transform input type A into output type B

```kotlin
val task: Task<A, B> =
    object : Task<A, B>() {
        override fun perform(input: A, listener: Listener<B>) {
            // Simple stupid task that converts int to String
            val output = B(input.x.toString())
            listener.onSuccess(output)
        }
    }
```

Step 3: Implement listener to get outputs of output type B

```kotlin
val listener: TasksListener<B> =
    object : TasksListener<B> {
        override fun onSuccess(outputs: List<B>) {
            // Do something with the outputs
        }

        override fun onFailure(error: String) {
            // Show the error!
        }
    }
```

Step 4: Finally perform the tasks in parallel!

```kotlin
TasksInParallelHelper<A, B>()
        .doTasksInParallel(inputs, task, listener)
```

---

## Java Example

Step 1: Prepare a list of inputs of any input type A

```kotlin
List<A> inputs = Arrays.asList(new A(1), new A(2), new A(3));
```

Step 2: Implement Task class to transform input type A into output type B

```kotlin
Task<A, B> task =
    new Task<A, B>() {
        @Override
           public void perform(A input, @NonNull Listener<B> listener) {
                // Simple stupid task that converts int to String
                B output = new B(String.valueOf(input.getX()));
                listener.onSuccess(output);
            }
    };
```

Step 3: Implement listener to get outputs of output type B

```kotlin
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
```

Step 4: Finally perform the tasks in parallel!

```kotlin
new TasksInParallelHelper<A, B>()
    .doTasksInParallel(inputs, task, listener);
```