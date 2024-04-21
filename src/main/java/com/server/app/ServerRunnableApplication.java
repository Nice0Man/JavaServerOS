package com.server.app;

import java.io.IOException;
import java.net.Socket;

public class ServerRunnableApplication extends RunnableApplication {
    public static Socket cloneSocket(Socket socket) throws IOException, ClassNotFoundException {
        return new Socket(socket.getInetAddress().getHostName(), socket.getLocalPort());
    }
}
