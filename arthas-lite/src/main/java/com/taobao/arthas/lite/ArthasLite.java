package com.taobao.arthas.lite;

import com.taobao.arthas.core.server.ArthasBootstrap;
import com.taobao.arthas.core.shell.ShellServer;
import com.taobao.arthas.core.shell.ShellServerOptions;
import com.taobao.arthas.core.shell.command.CommandResolver;
import com.taobao.arthas.core.shell.impl.GlobalJobControllerImpl;
import com.taobao.arthas.core.shell.system.JobController;
import com.taobao.arthas.lite.command.LiteCommandResolver;
import com.taobao.arthas.common.PidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight Arthas main entry point
 */
public class ArthasLite {
    private static final Logger logger = LoggerFactory.getLogger(ArthasLite.class);
    
    private ShellServer shellServer;
    private JobController jobController;
    
    public static void main(String[] args) {
        try {
            ArthasLite arthasLite = new ArthasLite();
            arthasLite.start();
            arthasLite.awaitTermination();
        } catch (Throwable e) {
            logger.error("Failed to start arthas-lite", e);
            System.exit(1);
        }
    }
    
    public void start() throws Exception {
        logger.info("Starting arthas-lite...");
        
        // Get instrumentation from ArthasBootstrap
        ArthasBootstrap bootstrap = ArthasBootstrap.getInstance();
        Instrumentation instrumentation = bootstrap.getInstrumentation();
        
        // Create shell server options
        ShellServerOptions options = new ShellServerOptions()
                .setInstrumentation(instrumentation)
                .setPid(PidUtils.currentPid())
                .setWelcomeMessage("Welcome to arthas-lite!");
        
        // Create shell server
        shellServer = new ShellServerImpl(options);
        
        // Register command resolver
        shellServer.registerCommandResolver(new LiteCommandResolver());
        
        // Start shell server
        shellServer.listen();
        
        logger.info("arthas-lite started successfully!");
    }
    
    public void awaitTermination() throws InterruptedException {
        if (shellServer != null) {
            shellServer.awaitTermination();
        }
    }
    
    public void stop() {
        logger.info("Stopping arthas-lite...");
        if (shellServer != null) {
            shellServer.close();
        }
        logger.info("arthas-lite stopped.");
    }
}