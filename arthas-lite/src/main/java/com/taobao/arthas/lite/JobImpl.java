package com.taobao.arthas.lite;

import com.taobao.arthas.core.shell.command.Command;
import com.taobao.arthas.core.shell.command.CommandResolver;
import com.taobao.arthas.core.shell.session.Session;
import com.taobao.arthas.core.shell.system.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Lightweight implementation of Job
 */
public class JobImpl implements Job {
    private static final Logger logger = LoggerFactory.getLogger(JobImpl.class);
    
    private final String line;
    private final Session session;
    private final List<CommandResolver> resolvers;
    private Future<?> future;
    
    public JobImpl(String line, Session session, List<CommandResolver> resolvers) {
        this.line = line;
        this.session = session;
        this.resolvers = resolvers;
    }
    
    @Override
    public String line() {
        return line;
    }
    
    @Override
    public Session session() {
        return session;
    }
    
    @Override
    public void run() {
        try {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) {
                return;
            }
            
            String commandName = parts[0];
            
            // Find command in resolvers
            for (CommandResolver resolver : resolvers) {
                List<Command> commands = resolver.commands();
                for (Command command : commands) {
                    if (command.name().equals(commandName)) {
                        // Execute command
                        command.processHandler().handle(session.getTerm());
                        return;
                    }
                }
            }
            
            session.getTerm().write("Unknown command: " + commandName + "\\n");
        } catch (Exception e) {
            logger.error("Error executing job: " + line, e);
            try {
                session.getTerm().write("Error: " + e.getMessage() + "\\n");
            } catch (Exception ex) {
                logger.error("Error writing to terminal", ex);
            }
        }
    }
    
    @Override
    public void terminate() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }
    
    @Override
    public Future<?> future() {
        return future;
    }
    
    @Override
    public void setFuture(Future<?> future) {
        this.future = future;
    }
}