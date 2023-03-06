package com.avaya.outbound.lib.support;

import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import org.apiguardian.api.API;

@API(
        status = API.Status.STABLE
)
public class CustomPicoFactory implements ObjectFactory {
    private final PicoFactory factory;

    public CustomPicoFactory() {
        this.factory = new PicoFactory();
    }

    @Override
    public void start() {
        this.factory.start();
    }

    @Override
    public void stop() {
        this.factory.stop();
    }

    @Override
    public boolean addClass(Class<?> aClass) {
        return this.factory.addClass(aClass);
    }

    @Override
    public <T> T getInstance(Class<T> aClass) {
        T instance = this.factory.getInstance(aClass);
        System.out.println("DEBUG: " + instance);
        return instance;
    }
}
