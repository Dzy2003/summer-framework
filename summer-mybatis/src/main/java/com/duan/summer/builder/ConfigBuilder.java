package com.duan.summer.builder;


import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeAliasRegistry;



public abstract class ConfigBuilder {

    protected Configuration configuration;
    protected TypeAliasRegistry typeAliasRegistry;
    public abstract Configuration parse() throws ClassNotFoundException;

    public ConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
