package com.github.hasanmirzae.modul.impl;

import com.github.hasanmirzae.modul.AbstractModule;

import java.util.Map;
import java.util.concurrent.Callable;

public class SwitchModule<I,O> extends AbstractModule<I,O> {

    private Map<I,Callable<O>> conditions;

    public SwitchModule(Map<I,Callable<O>> conditions){
        this.conditions = conditions;
    }

    public O process(I input){
        if (conditions != null && conditions.containsKey(input)) {
            try {
                return conditions.get(input).call();
            } catch (Throwable e) {
                if (exceptionHandler != null)
                    exceptionHandler.accept(e);
            }
        }

        return  null;
    }

}
