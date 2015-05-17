package at.mse.walchhofer.smokee.cdi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import at.mse.walchhofer.smokee.TestSuite;
import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.smokee.interception.AlternativeLiteral;
import at.mse.walchhofer.smokee.interception.SmokEEMockingInterceptor;
import at.mse.walchhofer.smokee.interception.SmokEEMockingInterceptorLiteral;
import at.mse.walchhofer.smokee.interception.SmokEETransactionInterceptor;
import at.mse.walchhofer.smokee.interception.SmokEETransactionInterceptorLiteral;
import at.mse.walchhofer.smokee.tests.control.ConfigurationException;
import at.mse.walchhofer.smokee.utils.PropertyUtils;
import at.mse.walchhofer.utilities.reflections.AnnotationScanner;

public class SmokEEDynamicInterceptorsExtension implements Extension {

    private final Logger logger = Logger
            .getLogger(SmokEEDynamicInterceptorsExtension.class.getName());

    private List<Class<?>> alternatives = new ArrayList<>();
    private AnnotationScanner annotationScanner = null;

    public void addSmokEEMockingInterceptorBindings(@Observes BeforeBeanDiscovery bbd,
            BeanManager beanManager) {
        try {
            String pkg2scan = new PropertyUtils().getPackage2Scan();
            annotationScanner = new AnnotationScanner(pkg2scan);
            List<String> indexedClasses = annotationScanner
                    .getClassesAnnotatedWith(Produces.class);
            for (String clazzName : indexedClasses) {
                Class<?> clazz = Class.forName(clazzName);
                AnnotatedType<?> newAnnotatedType = beanManager
                        .createAnnotatedType(clazz);
                AnnotatedTypeWrapper<?> wrapper = new AnnotatedTypeWrapper<>(
                        newAnnotatedType, newAnnotatedType.getAnnotations());
                wrapper.addAnnotation(new AlternativeLiteral());
                wrapper.addAnnotation(new SmokEEMockingInterceptorLiteral());
                bbd.addAnnotatedType(wrapper);
                alternatives.add(clazz);
            }
        } catch (ConfigurationException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    /**
     * Stateless,Stateful und Singleton fuer SessionBeans, MessageDriven für
     * Message-Driven Beans
     */
    public <T> void processEJBs(
            @Observes @WithAnnotations({ Stateless.class, Stateful.class,
                    Singleton.class, MessageDriven.class }) ProcessAnnotatedType<T> processAnnotatedType) {
        addSmokeeTransactionInterceptorAnnotation(processAnnotatedType);
    }

    public void afterTypeDiscovery(
            @Observes AfterTypeDiscovery afterTypeDiscovery) {
        afterTypeDiscovery.getInterceptors().add(SmokEETransactionInterceptor.class);
        afterTypeDiscovery.getInterceptors().add(SmokEEMockingInterceptor.class);
        afterTypeDiscovery.getAlternatives().addAll(alternatives);
    }

    // https://issues.jboss.org/browse/WELD-1453
    // BeanManager.getReference() funktioniert erst in AfterDeploymentValidation
    //
    /**
     * 
     * @param abv
     * @param beanManager
     */
    public void afterDeploymentValidation(
            @Observes AfterDeploymentValidation abv, BeanManager beanManager) {
        if (annotationScanner != null) {
            System.out.println("afterBeanDiscovery called!");
            Set<Bean<?>> beans = beanManager.getBeans(TestSuite.class);
            Bean<?> bean = beans.iterator().next();
            CreationalContext<?> ctx = beanManager
                    .createCreationalContext(bean);
            TestSuite testSuite = (TestSuite) beanManager.getReference(bean,
                    TestSuite.class, ctx);

            testSuite.setTestCases(annotationScanner
                    .getMethodInstancesByAnnotation(SmokeTest.class));
        }
    }

    private <T> void addSmokeeTransactionInterceptorAnnotation(
            ProcessAnnotatedType<T> processAnnotatedType) {
        AnnotatedType<T> annotatedType = processAnnotatedType
                .getAnnotatedType();
        AnnotatedTypeWrapper<T> wrapper = null;
        if (processAnnotatedType.getAnnotatedType() instanceof AnnotatedTypeWrapper) {
            wrapper = (AnnotatedTypeWrapper<T>) processAnnotatedType
                    .getAnnotatedType();
        } else {
            wrapper = new AnnotatedTypeWrapper<>(annotatedType,
                    annotatedType.getAnnotations());
        }
        wrapper.addAnnotation(new SmokEETransactionInterceptorLiteral());
        processAnnotatedType.setAnnotatedType(wrapper);
    }
}
