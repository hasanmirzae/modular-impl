package com.github.hasanmirzae.modul.impl;

import com.github.hasanmirzae.module.AbstractModule;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Switch<I,O> extends AbstractModule<I,O> {

    private Map<I,Callable<O>> conditions;

    public Switch(Map<I,Callable<O>> conditions){
        this.conditions = conditions;
    }

    @Override
    protected Function<I, O> getLogic() {
        return input -> {
            if (conditions != null && conditions.containsKey(input)) {
                try {
                    return conditions.get(input).call();
                } catch (Throwable e) {
                    if (exceptionHandler != null)
                        exceptionHandler.accept(e);
                }
            }
            return null;
        };
    }

}
