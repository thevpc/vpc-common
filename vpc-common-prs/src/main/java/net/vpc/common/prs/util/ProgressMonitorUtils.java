package net.vpc.common.prs.util;

public class ProgressMonitorUtils {
    private ProgressMonitorUtils() {
    }

    public static StepProgressMonitor steps(ProgressMonitor monitor, int count) {
        return new StepProgressMonitor(monitor, count);
    }

    public static ProgressMonitor[] split(ProgressMonitor monitor, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Illegal count " + count);
        }
        float w = 1f / count;
        float start = 0;
        PartialProgressMonitor[] sub = new PartialProgressMonitor[count];
        for (int i = 0; i < sub.length; i++) {
            sub[i] = new PartialProgressMonitor(monitor, start, w);
            start += w;
        }
        return sub;
    }

    public static ProgressMonitor[] splitByWeights(ProgressMonitor monitor, float... percents) {
        if (percents.length == 0) {
            percents = new float[]{0.5f, 0.5f};
        } else if (percents.length == 1) {
            if (percents[0] < 1.0f && percents[0] > 0f) {
                percents = new float[]{percents[0], 1f - percents[0]};
            } else {
                throw new IllegalArgumentException("Illegal percent " + percents[0]);
            }
        }
        PartialProgressMonitor[] sub = new PartialProgressMonitor[percents.length];
        float max = 0;
        for (float percent : percents) {
            if (percent <= 0) {
                throw new IllegalArgumentException("Illegal percent " + percent);
            }
            max += percent;
        }
        for (int i = 0; i < percents.length; i++) {
            percents[i] = percents[i] / max;
        }
        float start = 0;
        for (int i = 0; i < sub.length; i++) {
            sub[i] = new PartialProgressMonitor(monitor, start, percents[i]);
            start += percents[i];
        }
        return sub;
    }
}
