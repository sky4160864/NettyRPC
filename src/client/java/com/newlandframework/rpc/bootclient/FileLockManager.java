package com.newlandframework.rpc.bootclient;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * FileLockManager 文件锁管理
 *
 */
public class FileLockManager {
    private FileLock fileLock = null;
    private File file = null;
    private RandomAccessFile randomAccessFile = null;
    public FileLockManager(String fileName) {
        this.file = new File(fileName);
    }

    public FileLockManager(File file) {
        this.file = file;
    }

    /**
     * 文件加锁并创建文件
     *
     * @return
     * @throws IOException
     */
    public boolean Lock() throws IOException {
        if (!this.file.exists()) {
            this.file.createNewFile();
            this.randomAccessFile = new RandomAccessFile(this.file, "rw");
            this.fileLock = this.randomAccessFile.getChannel().tryLock();
            if (this.fileLock.isValid()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * 解锁并删除文件
     *
     * @return
     * @throws IOException
     */
    public boolean unLock() throws IOException {
        if (!this.file.exists()) {
            return true;
        } else {
            if (this.fileLock != null) {
                this.fileLock.release();
            }
            if (this.randomAccessFile != null) {
                this.randomAccessFile.close();
            }
            if (this.file.delete()) {
                return true;
            } else {
                return false;
            }

        }

    }

    /**
     * @return Returns the fileLock.
     */
    public FileLock getFileLock() {
        return this.fileLock;
    }

    /**
     * @param fileLock
     *            The fileLock to set.
     */
    public void setFileLock(FileLock fileLock) {
        this.fileLock = fileLock;
    }

}
