# ResultOf<T>

ResultOf is a resource wrapper class. While performing an IO / Network operation, there are 2 possibilities:

- Task will be successful & we will get some data T

- Task may fail producing some exception


ResultOf class makes it easier to wrap these 2 possibilities in one.

---

## Code without ResultOf class

You'll have to use try catch whenever calling a suspending function

```kotlin
try {
    val user = userRepository.getUser()
} catch(e: Exception) {
    // Handle error
}


OR 


coroutineScope.launch(exceptionHandler) {
    val user = userRepository.getUser()
}
```

## Using ResultOf class with when block

```kotlin
when(
    val result = userRepository.getUser()
) {
    is Success -> {
        val user = result.data
        // Consume result
    }
    is Error -> {
        val error = result.message
        // Show error
    }
}
```

The code might look longer, but there are 2 functions at rescue :

## Using ResultOf with onSuccess & onFailure

```kotlin
userRepository.getUser()
    .onSuccess { user ->

    }.onFailure { error ->

    }
```

But these functions lead to callback hell when calling multiple suspending functions in a sequence:

## Callback hell

```kotlin
userRepository.getUser()
    .onSuccess { user ->
        paymentsRepository.getPayments(user)
            .onSuccess { payments ->

            }.onFailure { error ->

            }
    }.onFailure { error ->

    }
```

Is there a way out? Of course, ResultOf class offers await() method :

## Using ResultOf class with await()

```kotlin
try {
    val user = userRepository.getUser().await()
    val payments = paymentsRepository.getPayments(user).await()
} catch(e: Exception) {
    // Handle error
}

OR 

coroutineScope.launch(exceptionHandler) {
    val user = userRepository.getUser().await()
    val payments = paymentsRepository.getPayments(user).await()
}
```