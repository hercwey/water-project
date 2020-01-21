package com.learnbind.ai.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


public abstract class AbstractDO
        implements Serializable {
    private static final long serialVersionUID = -3942149913171834745L;

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
