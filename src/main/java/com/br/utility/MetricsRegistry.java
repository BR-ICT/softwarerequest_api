package com.br.utility;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * One instance per webapp classloader — each WAR on Tomcat gets its own copy
 * automatically (Servlet spec classloader isolation), so no cross-webapp
 * registry collision even with several apps sharing one Tomcat (see D5).
 */
public class MetricsRegistry {

	public static final PrometheusMeterRegistry REGISTRY = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

	static {
		new ClassLoaderMetrics().bindTo(REGISTRY);
		new JvmMemoryMetrics().bindTo(REGISTRY);
		new JvmGcMetrics().bindTo(REGISTRY);
		new JvmThreadMetrics().bindTo(REGISTRY);
		new ProcessorMetrics().bindTo(REGISTRY);
		new UptimeMetrics().bindTo(REGISTRY);
	}

	private MetricsRegistry() {
	}
}
