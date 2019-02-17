
package uk.ac.jisc.nsa.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartWebServer {

    private static final Logger log = LoggerFactory.getLogger(StartWebServer.class);

    public static void main(final String args[]) {
        final WebServer server = new OneRequestWebServer();
        final int port = 8080;
        log.info("Starting web server on port {}", port);
        server.start(port);
    }

}
