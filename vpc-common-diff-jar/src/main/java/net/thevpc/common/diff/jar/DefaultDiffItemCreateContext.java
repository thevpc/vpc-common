package net.thevpc.common.diff.jar;

public class DefaultDiffItemCreateContext extends AbstractDiffItemCreateContext{
    public DefaultDiffItemCreateContext(DiffKey name, DiffStatus status, String sourceValue, String targetValue, DiffCommand diffCommand, DiffEvalContext diffEvalContext) {
        super(name, status, sourceValue, targetValue, diffCommand,diffEvalContext);
    }
}
