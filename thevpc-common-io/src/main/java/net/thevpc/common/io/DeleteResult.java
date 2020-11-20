package net.thevpc.common.io;

public class DeleteResult {
    private int deletedFilesCount;
    private int deletedFolfersCount;
    private int failedDeletionCount;

    public DeleteResult(int deletedFilesCount, int deletedFolfersCount, int failedDeletionCount) {
        this.deletedFilesCount = deletedFilesCount;
        this.deletedFolfersCount = deletedFolfersCount;
        this.failedDeletionCount = failedDeletionCount;
    }

    public int getDeletedFilesCount() {
        return deletedFilesCount;
    }

    public int getDeletedFolfersCount() {
        return deletedFolfersCount;
    }

    public int getFailedDeletionCount() {
        return failedDeletionCount;
    }
}
