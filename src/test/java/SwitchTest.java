package com.github.hasanmirzae.modul.impl;

import com.github.hasanmirzae.module.AbstractModule;
import com.github.hasanmirzae.module.Module;
import org.junit.Test;

import java.util.function.Function;

public class SwitchTest {

    class Module1 extends AbstractModule<String, Integer>{

        @Override protected Function<String, Integer> getLogic() {
            System.out.println("Module 1 called!");
            return str -> Integer.valueOf(str);
        }
    }

    class Module2 extends AbstractModule<Integer,String>{

        @Override protected Function<Integer, String> getLogic() {
            System.out.println("Module 2 called!");
            return i -> String.valueOf(i);
        }
    }

    class Module3 extends AbstractModule<String,String>{

        Module<String, Integer> mod1 = new Module1();
        Module<Integer, String> mod2 = new Module2();

        @Override protected Function<String, String> getLogic() {
            System.out.println("Module 3 called!");
            return input -> mod2.process(mod1.process(input));
        }
    }


    @Test
    public void test(){

        Module<String,String> m = new Module3();

        System.out.println(m.process("123"));

    }

}
