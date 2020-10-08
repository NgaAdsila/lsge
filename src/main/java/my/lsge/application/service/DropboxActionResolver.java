package my.lsge.application.service;

@FunctionalInterface
public interface DropboxActionResolver<T> {
    T perform() throws Exception;
}
