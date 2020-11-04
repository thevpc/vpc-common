package net.thevpc.common.diff.jar;

public interface DiffItemCreateContext {
    DiffEvalContext getEvalContext();

    DiffCommand getDiff();

    DiffKey getKey();

    DiffStatus getStatus();

    String getSourceValue();

    String getTargetValue();
}
