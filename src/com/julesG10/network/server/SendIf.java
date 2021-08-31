package com.julesG10.network.server;

public interface SendIf<T extends ServerClient> {
    boolean sendIf(T t);
}
