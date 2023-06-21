package org.academiadecodigo.bootcamp;

import java.io.IOException;

import static org.academiadecodigo.bootcamp.Server.port;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(port);
    }
}
