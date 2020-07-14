package com.bailbots.fb.quartersbot.bpp;

import com.github.messenger4j.send.message.Message;
import com.github.messenger4j.webhook.Event;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Optional;

public class BotApiMethodController {
    private Object bean;
    private Method method;

    public BotApiMethodController(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

/*     public boolean successUpdatePredicate(Event event) {
        return Optional.of(event).map(Event::isTextMessageEvent).map(Event::asTextMessageEvent:).orElse(false);
    }*/

    @SneakyThrows
    public void process(Event event) {
    //    if(!successUpdatePredicate(event)) return;

        method.invoke(bean, event);
    }

}
