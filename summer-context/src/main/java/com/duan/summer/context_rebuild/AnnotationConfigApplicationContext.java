package com.duan.summer.context_rebuild;


/**
 * @author 白日
 * @create 2023/12/12 20:02
 * @description
 */

public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry{
    AnnotatedBeanDefinitionReader reader;
    public AnnotationConfigApplicationContext(){
        super();
        this.reader = new AnnotatedBeanDefinitionReader(this);
    }
    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        super();
        this.reader = new AnnotatedBeanDefinitionReader(this);
        register(componentClasses);
        createBean();
    }

    private void refresh() {
        createBean();
    }

    @Override
    public void register(Class<?>... componentClasses) {
        if(componentClasses == null || componentClasses.length == 0){
            throw new IllegalArgumentException("至少传入一个Class");
        }
        this.reader.registry(componentClasses);
    }
}
