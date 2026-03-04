package io.kaoto.forage.core.common;

import java.util.function.Supplier;

/**
 * Describes an auxiliary bean that should be registered alongside a module's primary bean.
 *
 * <p>Examples include aggregation repositories and idempotent repositories for JDBC modules.
 *
 * @param name the bean name to register
 * @param type the bean class type for registration
 * @param factory a lazy supplier that creates the bean instance
 * @since 1.1
 */
public record AuxiliaryBeanDescriptor(String name, Class<?> type, Supplier<?> factory) {}
