package com.duan.summer.builder;


import com.duan.summer.session.Configuration;
import com.duan.summer.type.TypeAliasRegistry;

/**
 * @author 小傅哥，微信：fustack
 * @description 构建器的基类，建造者模式
 * @github https://github.com/fuzhengwei
 * @copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public abstract class ConfigBuilder {

    protected Configuration configuration;
    protected TypeAliasRegistry typeAliasRegistry;
    public abstract Configuration parse();

    public ConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
