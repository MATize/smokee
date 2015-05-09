package at.mse.walchhofer.smokee.tests.boundary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.beanutils.ConvertUtils;

import at.mse.walchhofer.smokee.TestSuite;
import at.mse.walchhofer.smokee.api.SmokeCache;
import at.mse.walchhofer.smokee.api.SmokeParam;
import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.smokee.api.SmokerStatus;
import at.mse.walchhofer.smokee.api.caching.ISmokEEJCache;
import at.mse.walchhofer.smokee.api.caching.ISmokeTestResult;
import at.mse.walchhofer.smokee.tests.control.ConfigurationException;
import at.mse.walchhofer.smokee.tests.control.NotEnabledException;
import at.mse.walchhofer.smokee.tests.control.TestContext;
import at.mse.walchhofer.smokee.tests.entity.SmokeTestResult;
import at.mse.walchhofer.smokee.utils.PropertyUtils;
import at.mse.walchhofer.utilities.logging.Log;
import at.mse.walchhofer.utilities.timing.StopWatch;

@RequestScoped
public class SmokeService {

	@Inject
	@SmokeCache
	ISmokEEJCache testResultCache;

	@Inject
	@Log
	Logger log;

	@Inject
	PropertyUtils propertyUtils;

	@Inject
	TestSuite testSuite;

	@Inject
	TestContext testContext;

	@Inject
	@SmokerStatus
	Boolean enabled;

	@Inject
	@Any
	Instance<Object> smokeInstances;

	public List<ISmokeTestResult> executeTests() throws ConfigurationException,
			NotEnabledException {

		validateSystemConfiguration();

		List<ISmokeTestResult> testResults = new ArrayList<>();

		testContext.setSmokeTest(true);

		//
		// get all tests from test suite
		// test scanning happened in cdi extension at deploy time
		//
		for (Method testMethod : testSuite.getTestCases()) {
			ISmokeTestResult smokeTestResult = testResultCache.get(testMethod);
			if (smokeTestResult == null) {
				smokeTestResult = new SmokeTestResult();
				smokeTestResult.setApplicationContext(getJndiPrefix());
				smokeTestResult = executeTest(smokeTestResult, testMethod);
			} else {
				// only if test failed during last test run
				if (!smokeTestResult.getPassed()) {
					smokeTestResult = executeTest(smokeTestResult, testMethod);
				}
			}
			testResults.add(smokeTestResult);
			testResultCache.put(testMethod, smokeTestResult);
		}
		return testResults;
	}

	private ISmokeTestResult executeTest(ISmokeTestResult result, Method method) {

		if (result == null) {
			return null;
		}

		SmokeTest smokeAnnotation = method.getAnnotation(SmokeTest.class);

		// wenn kein extra name gesetzt wurde für SmokeTest, verwende Name von
		// Methode
		if (SmokeTest.DEF_NAME.equals(smokeAnnotation.name())) {
			result.setTestName(method.getName() + "_Test");
		} else {
			result.setTestName(smokeAnnotation.name());
		}

		// Parameter Liste erzeugen
		SmokeParam[] parameterList = smokeAnnotation.parameters();
		Object[] params = new Object[parameterList.length];
		int i = 0;
		for (SmokeParam smokeParam : parameterList) {
			log.info("Value: " + smokeParam.value());
			log.info("Type: " + smokeParam.type().getClazz());
			Object p = ConvertUtils.convert(smokeParam.value(), smokeParam
					.type().getClazz());
			params[i++] = p;
		}

		// Instanz holen von DI
		Object instance = smokeInstances.select(method.getDeclaringClass())
				.get();

		// Ausfuehren von Methode mit Parametern
		try {
			Object ret = null;
			method.setAccessible(true); // falls modifier private oder
			// protected ist, ignorieren
			// wenn parameter-list
			// vorhanden, invoke Method mit
			// Parametern
			StopWatch timer = StopWatch.getInstance();
			timer.start();
			if (params.length > 0) {
				ret = method.invoke(instance, params);
			} else {
				ret = method.invoke(instance);
			}
			timer.stop();
			result.setLastRun(Calendar.getInstance().getTime());
			result.setElapsedTime(timer.getElapsedTime());
			if (!smokeAnnotation.expectedReturn().type().getClazz()
					.equals(Void.class)) {
				if (ret != null) {
					Object expected = ConvertUtils.convert(smokeAnnotation
							.expectedReturn().value(), smokeAnnotation
							.expectedReturn().type().getClazz());
					result.setPassed(expected != null && expected.equals(ret));
				} else {
					result.setPassed(false);
				}
			} else {
				result.setPassed(true);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			result.setPassed(false);
		}
		return result;
	}

	private String getJndiPrefix() throws ConfigurationException {
		return propertyUtils.getJndiPrefixFromProperties();
	}

	private void validateSystemConfiguration() throws NotEnabledException,
			ConfigurationException {
		if (!enabled) {
			throw new NotEnabledException();
		}

		if (testResultCache == null) {
			throw new ConfigurationException(
					"Smoketest Result Cache has not been correctly initialized!");
		}

		if (testSuite == null) {
			throw new ConfigurationException(
					"Test Suite has not been correctly initialized!");
		}
		getJndiPrefix();
	}

}
