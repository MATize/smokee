package at.mse.walchhofer.example.services;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class TestBean {

    @Inject
    OtherBeanStateless stateless;

    @Inject
    OtherBeanSingleton singleton;

    @Inject
    OtherBeanStateful stateful;

    public boolean loescheBenutzerById(Long id) {
        return Boolean.TRUE;
    }

    public void testStatelessNested() {
        stateless.createApplBenutzer();
        stateless.getBenutzerByVornameJPQL("@Stateless");
    }

    public void testStatelessNestedNew() {
        stateless.createApplBenutzerNewTransaction();
        stateless.getBenutzerByVornameJPQL("@Stateless");
    }

    public void testStatefulNested() {
        stateful.createApplBenutzer();
        stateful.getBenutzerByVornameJPQL("@Stateful");
    }

    public void testStatefulNestedNew() {
        stateful.createApplBenutzerNewTransaction();
        stateful.getBenutzerByVornameJPQL("@Stateful");
    }

    public void testSingletonNested() {
        singleton.createApplBenutzer();
        singleton.getBenutzerByVornameJPQL("@Singleton");
    }

    public void testSingletonNestedNew() {
        singleton.createApplBenutzerNewTransaction();
        singleton.getBenutzerByVornameJPQL("@Singleton");
    }

}
