
package uk.ac.jisc.nsa.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This server keeps the connection alive. It also blocks for 10 seconds. Requests are queue on the socket.
 */
public class ManyRequestWebServer implements WebServer {

    /**
     * * Class logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ManyRequestWebServer.class);

    @Override
    public void start(final int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket connectionWithClient = serverSocket.accept();
                final PrintWriter out = new PrintWriter(connectionWithClient.getOutputStream(), true);
                final BufferedReader in =
                        new BufferedReader(new InputStreamReader(connectionWithClient.getInputStream()));

                String input;

                log.info("Accepted connection from [{},{}]", connectionWithClient.getInetAddress(),
                        connectionWithClient.getPort());

                // use Content-Length to check end of message or an empty line for simplicity.
                while ((input = in.readLine()) != null) {
                    log.info("ACK [{}]", input);
                    if (input.isEmpty()) {
                        break;
                    }

                }
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

                connectionWithClient.close();
                out.close();
                in.close();
            }

        } catch (final IOException | InterruptedException e) {

            e.printStackTrace();
        } finally {

        }

    }

}
