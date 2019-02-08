
package uk.ac.jisc.nsa.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This server keeps the connection alive. It also blocks for 10 seconds. Requests are queue on the socket.
 */
public class MultiThreadedWebServer implements WebServer {

    /**
     * * Class logger.
     */
    private static final Logger log = LoggerFactory.getLogger(MultiThreadedWebServer.class);

    @Override
    public void start(final int port) {

        // fixed pool, 1 will give the same affect as the ManyRequest server. Try it out then up it.
        final ExecutorService pool = Executors.newFixedThreadPool(1);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                pool.submit(new RequestWorker(serverSocket.accept()));
            }

        } catch (final IOException e) {
            log.error("Error listening on socket", e);
        }

    }

}

class RequestWorker implements Runnable {

    /**
     * * Class logger.
     */
    private static final Logger log = LoggerFactory.getLogger(RequestWorker.class);

    private final Socket connectionWithClient;

    public RequestWorker(final Socket sock) {
        connectionWithClient = Objects.requireNonNull(sock);

    }

    @Override
    public void run() {

        try {
            final PrintWriter out = new PrintWriter(connectionWithClient.getOutputStream(), true);
            final BufferedReader in = new BufferedReader(new InputStreamReader(connectionWithClient.getInputStream()));

            log.info("Connection established, using thread {}, from [{},{}]", Thread.currentThread().getName(),
                    connectionWithClient.getInetAddress(), connectionWithClient.getPort(), connectionWithClient);

            // use Content-Length to check end of message or an empty line for simplicity.

            Thread.sleep(10000);
            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
            out.write("Server: Apache/0.8.4\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Content-Length: 53\r\n");
            out.write("Expires: Sat, 01 Jan 2019 00:59:59 GMT\r\n");
            out.write("Last-modified: Fri, 09 Aug 2019 14:21:40 GMT\r\n");
            out.write("\r\n");
            out.write("<HTML><TITLE>Example</TITLE><P>GREAT! " + connectionWithClient.getPort() + "</P></HTML>");

            out.flush();

            out.close();
            in.close();
        } catch (final Exception e) {
            log.error("Error in request worker", e);
        } finally {
            try {
                connectionWithClient.close();
            } catch (final IOException e) {
                log.error("Error closing client connection");
            }
            log.info("Closed connection with client from thread [{}]", Thread.currentThread().getName());
        }

    }

}
