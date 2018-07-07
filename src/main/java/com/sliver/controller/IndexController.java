package com.sliver.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
    public void index(){
//        renderText("hello JFinal World.");
        render("/index.html");
    }
}
