package com.coolerfall.daemon;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Daemon: service daemon.
 *
 * @author Vincent Cheung
 * @since Jan. 19, 2015
 */
public class Daemon {
    private static final String TAG = Daemon.class.getSimpleName();

    private static final String BIN_DIR_NAME = "bin";
    private static final String DAEMON_BIN_NAME = "daemon";

    public static final int INTERVAL_FIVE_SECONDS = 5;
    public static final int INTERVAL_ONE_MINUTE = 60;
    public static final int INTERVAL_ONE_HOUR = 60 * 60;

    /**
     * start daemon
     */
    private static void start(Context context, Class<?> daemonClazzName, int interval, int sleepInterval) {
        String cmd = context.getDir(BIN_DIR_NAME, Context.MODE_PRIVATE)
                .getAbsolutePath() + File.separator + DAEMON_BIN_NAME;

		/* create the command string */
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(cmd);
        cmdBuilder.append(" -p ");
        cmdBuilder.append(context.getPackageName());
        cmdBuilder.append(" -s ");
        cmdBuilder.append(daemonClazzName.getName());
        cmdBuilder.append(" -t ");
        cmdBuilder.append(interval);

        if (sleepInterval > 0) {
            cmdBuilder.append(" -st ");
            cmdBuilder.append(sleepInterval);
        }

        try {
            Runtime.getRuntime().exec(cmdBuilder.toString()).waitFor();
            System.out.println(cmdBuilder.toString());
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "start daemon error: " + e.getMessage());
        }
    }

    /**
     * Run daemon process.
     *
     * @param context            context
     * @param daemonServiceClazz the name of daemon service class
     * @param interval           the interval to check
     */
    public static void run(Context context, Class<?> daemonServiceClazz, int interval) {
        run(context, daemonServiceClazz, interval, -1);
    }

    /**
     * Run daemon process.
     *
     * @param context            context
     * @param daemonServiceClazz the name of daemon service class
     * @param interval           the interval to check
     */
    public static void run(final Context context, final Class<?> daemonServiceClazz,
                           final int interval, final int sleepInterval) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Command.install(context, BIN_DIR_NAME, DAEMON_BIN_NAME);
                start(context, daemonServiceClazz, interval, sleepInterval);
            }
        }).start();
    }
}
