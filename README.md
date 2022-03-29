# AndroidCommons

Android library with common utilities required when building Android apps.

---

## Usage

Step 1: Add JitPack in your **root build.gradle** :

```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Step 2: Add AndroidCommons dependency to your **app build.gradle**:

```groovy
    dependencies {
            implementation 'com.github.The-Streamliners:AndroidCommons:1.0'
    }
```

---

## Utilities

### TasksInParallelHelper

TasksInParallelHelper is a helper class that performs task in parallel. Any input type (I) & output type (O) is supported. By Implementing task class, you can perform any task on I to generate O, whether it be a long IO operation or a network call.

[View examples](docs/TasksInParallelHelper.md)

---

### ResultOf<T>

ResultOf is a resource wrapper class. While performing an IO / Network operation, there are 2 possibilities:

- Task will be successful & we will get some data T

- Task may fail producing some exception


ResultOf<T> class makes it easier to wrap these 2 possibilities in one.

[View examples & features](docs/ResultOf.md)