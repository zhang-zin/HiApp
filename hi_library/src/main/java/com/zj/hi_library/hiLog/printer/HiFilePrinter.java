package com.zj.hi_library.hiLog.printer;

import androidx.annotation.NonNull;

import com.zj.hi_library.hiLog.HiLogConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * 文件打印器，日志信息输出到文件中
 */
public class HiFilePrinter implements HiLogPrinter {
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private final long retentionTime;

    /**
     * 日志信息写入的文件夹
     */
    private final File hiLogFile;
    private final LogWriter logWriter;
    private final PrintWorker printWorker;
    private static HiFilePrinter instance;

    /**
     * 创建HiFilePrinter
     *
     * @param retentionTime log文件的有效时长，单位毫秒，<=0表示一直有效
     * @param hiLogFile     log保存路径，如果是外部路径需要确保已经有外部存储的读写权限
     */
    public static synchronized HiFilePrinter getInstance(long retentionTime, File hiLogFile) {
        if (instance == null) {
            instance = new HiFilePrinter(retentionTime, hiLogFile);
        }
        return instance;
    }

    private HiFilePrinter(long retentionTime, File hiLogFile) {
        this.retentionTime = retentionTime;
        this.hiLogFile = hiLogFile;
        logWriter = new LogWriter();
        printWorker = new PrintWorker();
        format.setTimeZone(TimeZone.getDefault());
        cleanExpiredLog();
    }

    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        if (!printWorker.isRunning()) {
            printWorker.start();
        }

        printWorker.put(new HiLogMo(level, tag, printString, format.format(System.currentTimeMillis())));
    }

    private String getFileName() {
        return format.format(System.currentTimeMillis());
    }

    private void doPrint(HiLogMo hiLogMo) {

        String lastFileName = logWriter.getPreFileName();
        if (lastFileName == null) {
            String fileName = getFileName();
            if (logWriter.isReady()) {
                logWriter.close();
            }
            if (!logWriter.ready(fileName)) {
                return;
            }
        }

        logWriter.append(hiLogMo.flattenedLog());
    }

    /**
     * 清除过期log
     */
    private void cleanExpiredLog() {
        if (retentionTime <= 0) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        File[] files = hiLogFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (currentTimeMillis - file.lastModified() > retentionTime) {
                file.delete();
            }
        }
    }


    /**
     * 开启子线程写入文件
     */
    private class PrintWorker implements Runnable {

        private BlockingDeque<HiLogMo> logs = new LinkedBlockingDeque<>();
        private boolean running;

        void put(HiLogMo hiLogMo) {
            try {
                logs.put(hiLogMo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void start() {
            synchronized (this) {
                EXECUTOR.execute(this);
                running = true;
            }
        }

        boolean isRunning() {
            synchronized (this) {
                return running;
            }
        }

        @Override
        public void run() {
            HiLogMo hiLogMo;
            try {
                while (true) {
                    hiLogMo = logs.take();
                    doPrint(hiLogMo);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                synchronized (this) {
                    running = false;
                }
            }
        }
    }

    /**
     * 使用BufferedWriter把日志写入文件
     */
    private class LogWriter {

        private BufferedWriter bufferedWriter;

        private String preFileName;
        private File logFile;

        boolean isReady() {
            return bufferedWriter != null;
        }

        public String getPreFileName() {
            return preFileName;
        }

        boolean ready(String newFileName) {
            this.preFileName = newFileName;
            logFile = new File(hiLogFile, preFileName);
            if (!logFile.exists()) {
                try {
                    File parentFile = logFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    preFileName = null;
                    logFile = null;
                    return false;
                }
            }

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException e) {
                e.printStackTrace();
                preFileName = null;
                logFile = null;
                return false;
            }

            return true;
        }

        boolean close() {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    preFileName = null;
                    logFile = null;
                    bufferedWriter = null;
                }
            }
            return true;
        }

        void append(String flattenedLog) {
            try {
                bufferedWriter.write(flattenedLog);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
