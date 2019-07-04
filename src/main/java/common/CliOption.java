package common;

public interface CliOption<T> {
    T execute() throws Exception;

    String description();
}
