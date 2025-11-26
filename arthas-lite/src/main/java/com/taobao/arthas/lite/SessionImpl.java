package com.taobao.arthas.lite;

import com.taobao.arthas.core.shell.session.Session;
import com.taobao.arthas.core.shell.term.Term;

/**
 * Lightweight implementation of Session
 */
public class SessionImpl implements Session {
    private final Term term;
    private final ShellServerImpl shellServer;
    
    public SessionImpl(Term term, ShellServerImpl shellServer) {
        this.term = term;
        this.shellServer = shellServer;
    }
    
    @Override
    public Term getTerm() {
        return term;
    }
    
    @Override
    public ShellServerImpl getServer() {
        return shellServer;
    }
    
    @Override
    public void close() {
        // Simple session cleanup
        if (term != null) {
            term.close();
        }
    }
}