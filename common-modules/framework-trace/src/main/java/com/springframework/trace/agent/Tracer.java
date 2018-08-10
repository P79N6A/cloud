package com.springframework.trace.agent;

import com.springframework.trace.Span;

/**
 * @author summer
 * 2018/8/10
 */
public class Tracer {
    /**
     *
     */
    private Span span;

    public static Tracer getTracer() {
        return new Tracer();
    }

    public Span getParentSpan() {
        return span;
    }
}
