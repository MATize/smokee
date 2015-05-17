package at.mse.walchhofer.smokee.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Dient als Wrapper zu {@link java.lang.Object}
 * <p>
 * <p>
 * Es werden nur die unter {@link SmokeValueType} angebotenen Datentypen
 * unterstuetzt.
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface SmokeValue {

    /**
     * 
     * Unterstuetzte Datentypen fuer {@link SmokeValue#type()}
     *
     */
    public enum SmokeValueType {
        LONG(Long.class),
        FLOAT(Float.class),
        STRING(String.class),
        INTEGER(Integer.class),
        BOOLEAN(Boolean.class),
        VOID(Void.class);

        private Class<?> clazz;

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        SmokeValueType(Class<?> clazz) {
            this.clazz = clazz;
        }
    }

    /**
     * 
     * Datentyp fuer Parameter anhand von {@link SmokeValueType}
     * 
     */
    SmokeValueType type() default SmokeValueType.VOID;

    /**
     * 
     * Der Wert des Parameters als String.<br>
     * {@link SmokeValueType#FLOAT} erwartet . als Dezimaltrennzeichen. z.B.:
     * "13.37"<br />
     * {@link SmokeValueType#BOOLEAN} erwartet "true" oder "false"<br />
     * 
     * 
     */
    String value() default "";
}
