package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MergedConvertersSupplier implements Function<JTypePattern, JConverter[]> {
    private final Function<JTypePattern, JConverter[]> convertersSupplier;
    private final JContext context;

    public MergedConvertersSupplier(JContext context, Function<JTypePattern, JConverter[]> convertersSupplier) {
        this.context = context;
        this.convertersSupplier = convertersSupplier;
    }

    @Override
    public JConverter[] apply(JTypePattern jType) {
        JConverter[] converters = context.resolvers().getConverters(jType);
        if(convertersSupplier ==null){
            return converters;
        }
        JConverter[] extraConverters = convertersSupplier.apply(jType);
        if(extraConverters==null||extraConverters.length==0){
            return converters;
        }
        List<JConverter> all=new ArrayList<>(converters.length+extraConverters.length);
        all.addAll(Arrays.asList(converters));
        all.addAll(Arrays.asList(extraConverters));
        return all.toArray(new JConverter[0]);
    }
}
