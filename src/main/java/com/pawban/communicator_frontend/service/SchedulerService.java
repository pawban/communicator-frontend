package com.pawban.communicator_frontend.service;

import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.pawban.communicator_frontend.type.ScheduledTaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);

    private final TaskScheduler taskScheduler;
    private final CommunicatorSession session;

    private Map<ScheduledTaskId, ScheduledFuture<?>> tasks = new EnumMap<>(ScheduledTaskId.class);

    @Autowired
    public SchedulerService(final TaskScheduler taskScheduler,
                            final CommunicatorSession session) {
        this.taskScheduler = taskScheduler;
        this.session = session;
    }

    public void addTask(final ScheduledTaskId taskId,
                        final long period,
                        final Runnable action) {
        tasks.put(taskId, taskScheduler.scheduleAtFixedRate(
                action,
                Date.from(Instant.now().plusMillis(period)),
                period
        ));
        LOGGER.info("SESSION_ID: " + session.getSessionId().toString() + "; " +
                taskId + " task has been scheduled with period " + period + ".");
    }

    public void removeTask(final ScheduledTaskId taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId).cancel(true);
            LOGGER.info("SESSION_ID: " + session.getSessionId().toString() + "; " +
                    taskId + " task has been stopped.");
        }
    }

    public void removeAll() {
        tasks.forEach((taskId, task) -> {
            task.cancel(true);
            LOGGER.info("SESSION_ID: " + session.getSessionId().toString() + "; " +
                    taskId + " task has been stopped.");
        });
        tasks.clear();
    }

    public boolean taskExists(final ScheduledTaskId taskId) {
        return tasks.containsKey(taskId);
    }

}
