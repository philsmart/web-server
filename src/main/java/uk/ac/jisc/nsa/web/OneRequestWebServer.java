
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
 * Simple 'WebServer' that listens for incoming connections, prints out the request and sends a simple, static, HTTP
 * message response.
 */
public class OneRequestWebServer implements WebServer {

    /**
     * Class logger.
     */
    private static final Logger log = LoggerFactory.getLogger(OneRequestWebServer.class);

    @Override
    public void start(final int port) {

        try {

            final ServerSocket serverSocket = new ServerSocket(port);
            final Socket connectionWithClient = serverSocket.accept();
            final PrintWriter out = new PrintWriter(connectionWithClient.getOutputStream(), true);
            final BufferedReader in = new BufferedReader(new InputStreamReader(connectionWithClient.getInputStream()));

            String input;

            // use Content-Length to check end of message or an empty line for simplicity.
            while ((input = in.readLine()) != null) {
                log.info("ACK [{}]", input);
                if (input.isEmpty()) {
                    break;
                }

            }

            /*
             * Below is a valid HTTP response. HTTP is a message in plain text that corresponds to a given protocol
             * (format of how the text looks).
             * <p>Consists of:</p>
             * <ol>
             * <li>A start line, Method, Target, Version
             * <li>A set of HTTP headers. <HeaderName>:<value></li>
             * <li>A newline</li>
             * <li>the body of the message</li>
             * <ol>
             */
            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
            out.write("Server: Apache/0.8.4\r\n");
            out.write("Content-Type: text/html\r\n");
            out.write("Content-Length: 48\r\n");
            out.write("Expires: Sat, 01 Jan 2019 00:59:59 GMT\r\n");
            out.write("Last-modified: Fri, 09 Aug 2019 14:21:40 GMT\r\n");
            out.write("\r\n");
            out.write("<HTML><TITLE>Example</TITLE><P>GREAT!</P></HTML>");

            out.flush();
            serverSocket.close();
            connectionWithClient.close();
            out.close();
            in.close();

        } catch (final IOException e) {

            e.printStackTrace();
        } finally {

        }
    }

}
