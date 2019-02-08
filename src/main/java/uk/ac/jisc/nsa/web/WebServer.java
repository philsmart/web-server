
package uk.ac.jisc.nsa.web;

public interface WebServer {

    /**
     * Start the webserver awaiting connections on <code>port</code>.
     * 
     * @param port the port to listen on. Will be bound with <code>localhost</code> IP to form a socket.
     */
    void start(int port);

}
