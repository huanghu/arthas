package com.taobao.arthas.lite.command;

import com.taobao.arthas.core.shell.command.Command;
import com.taobao.arthas.core.shell.command.CommandResolver;
import com.taobao.arthas.lite.command.impl.ThreadCommand;
import com.taobao.arthas.lite.command.impl.TraceCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Command resolver for arthas-lite commands
 */
public class LiteCommandResolver implements CommandResolver {
    
    @Override
    public List<Command> commands() {
        List<Command> commands = new ArrayList<>();
        
        // Add thread command
        commands.add(new ThreadCommand());
        
        // Add trace command
        commands.add(new TraceCommand());
        
        return commands;
    }
}