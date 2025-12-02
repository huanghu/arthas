package com.taobao.arthas.core.command.monitor200;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.arthas.core.command.Constants;
import com.taobao.arthas.core.command.model.BlockingLockInfo;
import com.taobao.arthas.core.command.model.BusyThreadInfo;
import com.taobao.arthas.core.command.model.ThreadModel;
import com.taobao.arthas.core.command.model.ThreadVO;
import com.taobao.arthas.core.shell.command.AnnotatedCommand;
import com.taobao.arthas.core.shell.command.CommandProcess;
import com.taobao.arthas.core.shell.command.ExitStatus;
import com.taobao.arthas.core.util.ArrayUtils;
import com.taobao.arthas.core.util.CommandUtils;
import com.taobao.arthas.core.util.StringUtils;
import com.taobao.arthas.core.util.ThreadUtil;
import com.taobao.middleware.cli.annotations.Argument;
import com.taobao.middleware.cli.annotations.Description;
import com.taobao.middleware.cli.annotations.Name;
import com.taobao.middleware.cli.annotations.Option;
import com.taobao.middleware.cli.annotations.Summary;

/**
 * @author hengyunabc 2015年12月7日 下午2:06:21
 */
@Name("thread")
@Summary("Display thread info, thread stack")
@Description(Constants.EXAMPLE +
        "  thread\n" +
        "  thread 51\n" +
        "  thread -n -1\n" +
        "  thread -n 5\n" +
        "  thread -b\n" +
        "  thread -i 2000\n" +
        "  thread --state BLOCKED\n" +
        "  thread -top\n" +
        "  thread -top 10\n" +
        "  thread -top 5 -i 3000\n" +
        Constants.WIKI + Constants.WIKI_HOME + "thread")
public class ThreadCommand extends AnnotatedCommand {
    private static Set<String> states = null;
    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    private long id = -1;
    private Integer topNBusy = null;
    private boolean findMostBlockingThread = false;
    private int sampleInterval = 200;
    private String state;
    private Integer topN = null;
    private int cpuTopInterval = 2000;

    private boolean lockedMonitors = false;
    private boolean lockedSynchronizers = false;
    private boolean all = false;

    static {
        states = new HashSet<String>(State.values().length);
        for (State state : State.values()) {
            states.add(state.name());
        }
    }

    @Argument(index = 0, required = false, argName = "id")
    @Description("Show thread stack")
    public void setId(long id) {
        this.id = id;
    }

    @Option(longName = "all", flag = true)
    @Description("Display all thread results instead of the first page")
    public void setAll(boolean all) {
        this.all = all;
    }

    @Option(shortName = "n", longName = "top-n-threads")
    @Description("The number of thread(s) to show, ordered by cpu utilization, -1 to show all.")
    public void setTopNBusy(Integer topNBusy) {
        this.topNBusy = topNBusy;
    }

    @Option(shortName = "b", longName = "include-blocking-thread", flag = true)
    @Description("Find the thread who is holding a lock that blocks the most number of threads.")
    public void setFindMostBlockingThread(boolean findMostBlockingThread) {
        this.findMostBlockingThread = findMostBlockingThread;
    }

    @Option(shortName = "i", longName = "sample-interval")
    @Description("Specify the sampling interval (in ms) when calculating cpu usage.")
    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
    }

    @Option(shortName = "top", longName = "top-n-threads")
    @Description("Display the top n threads with the highest CPU usage (default 5).")
    public void setTopN(Integer topN) {
        this.topN = topN;
    }

    @Option(shortName = "i", longName = "interval")
    @Description("Specify the sampling interval (in ms) for CPU Top (default 2000ms, minimum 500ms).")
    public void setCpuTopInterval(int cpuTopInterval) {
        this.cpuTopInterval = cpuTopInterval;
    }

    @Option(longName = "state")
    @Description("Display the thread filter by the state. NEW, RUNNABLE, TIMED_WAITING, WAITING, BLOCKED, TERMINATED is optional.")
    public void setState(String state) {
        this.state = state;
    }

    @Option(longName = "lockedMonitors", flag = true)
    @Description("Find the thread info with lockedMonitors flag, default value is false.")
    public void setLockedMonitors(boolean lockedMonitors) {
        this.lockedMonitors = lockedMonitors;
    }

    @Option(longName = "lockedSynchronizers", flag = true)
    @Description("Find the thread info with lockedSynchronizers flag, default value is false.")
    public void setLockedSynchronizers(boolean lockedSynchronizers) {
        this.lockedSynchronizers = lockedSynchronizers;
    }

    @Override
    public void process(CommandProcess process) {
        ExitStatus exitStatus;
        if (id > 0) {
            exitStatus = processThread(process);
        } else if (topNBusy != null) {
            exitStatus = processTopBusyThreads(process);
        } else if (findMostBlockingThread) {
            exitStatus = processBlockingThread(process);
        } else if (topN != null) {
            exitStatus = processCpuTopThreads(process);
        } else {
            exitStatus = processAllThreads(process);
        }
        CommandUtils.end(process, exitStatus);
    }

    private ExitStatus processAllThreads(CommandProcess process) {
        List<ThreadVO> threads = ThreadUtil.getThreads();

        // 统计各种线程状态
        Map<State, Integer> stateCountMap = new LinkedHashMap<State, Integer>();
        for (State s : State.values()) {
            stateCountMap.put(s, 0);
        }

        for (ThreadVO thread : threads) {
            State threadState = thread.getState();
            Integer count = stateCountMap.get(threadState);
            stateCountMap.put(threadState, count + 1);
        }

        boolean includeInternalThreads = true;
        Collection<ThreadVO> resultThreads = new ArrayList<ThreadVO>();
        if (!StringUtils.isEmpty(this.state)) {
            this.state = this.state.toUpperCase();
            if (states.contains(this.state)) {
                includeInternalThreads = false;
                for (ThreadVO thread : threads) {
                    if (thread.getState() != null && state.equals(thread.getState().name())) {
                        resultThreads.add(thread);
                    }
                }
            } else {
                return ExitStatus.failure(1, "Illegal argument, state should be one of " + states);
            }
        } else {
            resultThreads = threads;
        }

        // thread stats
        ThreadSampler threadSampler = new ThreadSampler();
        threadSampler.setIncludeInternalThreads(includeInternalThreads);
        threadSampler.sample(resultThreads);
        threadSampler.pause(sampleInterval);
        List<ThreadVO> threadStats = threadSampler.sample(resultThreads);

        process.appendResult(new ThreadModel(threadStats, stateCountMap, all));
        return ExitStatus.success();
    }

    private ExitStatus processBlockingThread(CommandProcess process) {
        BlockingLockInfo blockingLockInfo = ThreadUtil.findMostBlockingLock();
        if (blockingLockInfo.getThreadInfo() == null) {
            return ExitStatus.failure(1, "No most blocking thread found!");
        }
        process.appendResult(new ThreadModel(blockingLockInfo));
        return ExitStatus.success();
    }

    private ExitStatus processTopBusyThreads(CommandProcess process) {
        ThreadSampler threadSampler = new ThreadSampler();
        threadSampler.sample(ThreadUtil.getThreads());
        threadSampler.pause(sampleInterval);
        List<ThreadVO> threadStats = threadSampler.sample(ThreadUtil.getThreads());

        int limit = Math.min(threadStats.size(), topNBusy);

        List<ThreadVO> topNThreads = null;
        if (limit > 0) {
            topNThreads = threadStats.subList(0, limit);
        } else { // -1 for all threads
            topNThreads = threadStats;
        }

        List<Long> tids = new ArrayList<Long>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            if (thread.getId() > 0) {
                tids.add(thread.getId());
            }
        }

        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ArrayUtils.toPrimitive(tids.toArray(new Long[0])),
                lockedMonitors, lockedSynchronizers);
        if (tids.size() > 0 && threadInfos == null) {
            return ExitStatus.failure(1, "get top busy threads failed");
        }

        // threadInfo with cpuUsage
        List<BusyThreadInfo> busyThreadInfos = new ArrayList<BusyThreadInfo>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            ThreadInfo threadInfo = findThreadInfoById(threadInfos, thread.getId());
            if (threadInfo != null) {
                BusyThreadInfo busyThread = new BusyThreadInfo(thread, threadInfo);
                busyThreadInfos.add(busyThread);
            }
        }
        process.appendResult(new ThreadModel(busyThreadInfos));
        return ExitStatus.success();
    }

    private ThreadInfo findThreadInfoById(ThreadInfo[] threadInfos, long id) {
        for (int i = 0; i < threadInfos.length; i++) {
            ThreadInfo threadInfo = threadInfos[i];
            if (threadInfo != null && threadInfo.getThreadId() == id) {
                return threadInfo;
            }
        }
        return null;
    }

    private ExitStatus processThread(CommandProcess process) {
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(new long[] { id }, lockedMonitors, lockedSynchronizers);
        if (threadInfos == null || threadInfos.length < 1 || threadInfos[0] == null) {
            return ExitStatus.failure(1, "thread do not exist! id: " + id);
        }

        process.appendResult(new ThreadModel(threadInfos[0]));
        return ExitStatus.success();
    }

    private ExitStatus processCpuTopThreads(CommandProcess process) {
        // Validate interval
        if (cpuTopInterval < 500) {
            return ExitStatus.failure(1, "Illegal argument, interval must be at least 500ms.");
        }

        // Validate topN
        int n = topN != null ? topN : 5;
        if (n < 1) {
            return ExitStatus.failure(1, "Illegal argument, top n must be at least 1.");
        }

        ThreadSampler threadSampler = new ThreadSampler();
        threadSampler.sample(ThreadUtil.getThreads());
        threadSampler.pause(cpuTopInterval);
        List<ThreadVO> threadStats = threadSampler.sample(ThreadUtil.getThreads());

        int limit = Math.min(threadStats.size(), n);
        List<ThreadVO> topNThreads = threadStats.subList(0, limit);

        List<Long> tids = new ArrayList<Long>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            if (thread.getId() > 0) {
                tids.add(thread.getId());
            }
        }

        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ArrayUtils.toPrimitive(tids.toArray(new Long[0])),
                lockedMonitors, lockedSynchronizers);
        if (tids.size() > 0 && threadInfos == null) {
            return ExitStatus.failure(1, "get top CPU threads failed");
        }

        // threadInfo with cpuUsage
        List<BusyThreadInfo> busyThreadInfos = new ArrayList<BusyThreadInfo>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            ThreadInfo threadInfo = findThreadInfoById(threadInfos, thread.getId());
            if (threadInfo != null) {
                BusyThreadInfo busyThread = new BusyThreadInfo(thread, threadInfo);
                busyThreadInfos.add(busyThread);
            }
        }
        process.appendResult(new ThreadModel(busyThreadInfos));
        return ExitStatus.success();
    }
}
