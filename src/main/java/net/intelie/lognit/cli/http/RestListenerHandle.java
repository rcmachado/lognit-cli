package net.intelie.lognit.cli.http;

public interface RestListenerHandle {
    void waitDisconnected();
    void close();
}
