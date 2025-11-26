package com.taobao.arthas.lite;

import com.taobao.arthas.core.shell.ShellServer;
import com.taobao.arthas.core.shell.ShellServerOptions;
import com.taobao.arthas.core.shell.command.CommandResolver;
import com.taobao.arthas.core.shell.session.Session;
import com.taobao.arthas.core.shell.system.Job;
import com.taobao.arthas.core.shell.term.Term;
import com.taobao.arthas.core.shell.term.TermServer;
import com.taobao.arthas.core.shell.term.impl.TelnetTermServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Lightweight implementation of ShellServer
 */
public class ShellServerImpl extends ShellServer {
    private static final Logger logger = LoggerFactory.getLogger(ShellServerImpl.class);
    
    private final ShellServerOptions options;
    private final List<CommandResolver> resolvers = new ArrayList<>();
    private final List<TermServer> termServers = new ArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private CountDownLatch terminationLatch;
    
    public ShellServerImpl(ShellServerOptions options) {
        this.options = options;
        this.terminationLatch = new CountDownLatch(1);
    }
    
    @Override
    public void registerCommandResolver(CommandResolver resolver) {
        resolvers.add(resolver);
    }
    
    @Override
    public void registerTermServer(TermServer termServer) {
        termServers.add(termServer);
    }
    
    @Override
    public void listen() throws Exception {
        // Create telnet term server
        TelnetTermServer telnetServer = new TelnetTermServer(options.getPort(), this);
        termServers.add(telnetServer);
        
        // Start all term servers
        for (TermServer termServer : termServers) {
            termServer.listen();
            logger.info("Term server started on port: {}", termServer.port());
        }
        
        logger.info("Shell server started successfully");
    }
    
    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            logger.info("Closing shell server...");
            
            // Close all term servers
            for (TermServer termServer : termServers) {
                try {
                    termServer.close();
                } catch (Exception e) {
                    logger.error("Error closing term server", e);
                }
            }
            
            // Release termination latch
            if (terminationLatch != null) {
                terminationLatch.countDown();
            }
            
            logger.info("Shell server closed");
        }
    }
    
    @Override
    public void awaitTermination() throws InterruptedException {
        if (terminationLatch != null) {
            terminationLatch.await();
        }
    }
    
    @Override
    public Session createSession(Term term) {
        return new SessionImpl(term, this);
    }
    
    @Override
    public Job createJob(String line, Session session) {
        return new JobImpl(line, session, resolvers);
    }
    
    @Override
    public boolean isClosed() {
        return closed.get();
    }
    
    public List<CommandResolver> getResolvers() {
        return resolvers;
    }
}