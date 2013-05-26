package com.vast.util;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.net.URL;
import java.security.ProtectionDomain;

/**
 * @author David Pratt (dpratt@vast.com)
 */
public class EmbeddedJettyServer {

    public static void main(String[] args) {

        //probably want to find a better place to put this.
        // Optionally remove existing handlers attached to j.u.l root logger
        SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

        // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
        // the initialization phase of your application
        SLF4JBridgeHandler.install();

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);

        // Set some timeout options to make debugging easier.
        connector.setIdleTimeout(1000 * 60 * 60);
        connector.setPort(8081);
        server.setConnectors(new Connector[]{connector});

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");

        //prevent webapps from loading any logging classes
        context.addSystemClass("org.apache.log4j.");
        context.addSystemClass("org.slf4j.");
        context.addSystemClass("org.apache.commons.logging.");


        ProtectionDomain protectionDomain = EmbeddedJettyServer.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());

        server.setHandler(context);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
