# AnnotatedException

Generate custom Exception classes with a factory for classes annotated with `@Exception`.

## Usage

```java
@Exception
public class Class1 {}

// Anywhere in your code
Exception e = ExceptionFactory.create(Class1.class); // returns Class1Exception
```

You can also generate a specific category of exceptions:

```java
@Exception(category="InvalidContent")
public class Class1 {}

// Anywhere in your code
Exception e = InvalidContentExceptionFactory.create(Class1.class); // returns Class1InvalidContentException
```
