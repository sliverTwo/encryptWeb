package com.sliver.config;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import com.sliver.controller.IndexController;

public class WebConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);

    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
    }

    @Override
    public void configEngine(Engine me) {
        me.setDevMode(true);
    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
