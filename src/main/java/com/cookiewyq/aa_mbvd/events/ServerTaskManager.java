package com.cookiewyq.aa_mbvd.events;


import com.cookiewyq.aa_mbvd.common.ServerTask;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerTaskManager {
    private static final Queue<ServerTask> taskQueue = new ConcurrentLinkedQueue<>();
    
    public static void addTask(ServerTask task) {
        taskQueue.offer(task);
    }
    
    public static void executeTasks() {
        ServerTask task;
        while ((task = taskQueue.poll()) != null) {
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean hasTasks() {
        return !taskQueue.isEmpty();
    }
}
