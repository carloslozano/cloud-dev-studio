/*
 * Copyright (c) 2014, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.codenvy.ui.integration.test.utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.openqa.selenium.*;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Using the modified customized version of seleium web driver from
 * https://github.com/wso2/product-es/blob/master/modules/integration/tests-ui-integration/tests-ui-extension/src/test/
 * java/org.wso2.es.ui.integration.extension.util/ESWebDriver.java
 *
 * to enable snapshots on test failure with descriptive test reports
 */
public class DevSWebDriver implements org.openqa.selenium.WebDriver {

	protected static final Logger log = Logger.getLogger(DevSWebDriver.class);
	private static final String IMAGE_FILE_EXT = ".png";
	private static final String SUREFIRE_REPORTS = "surefire-reports";
	private static final String SCREEN_SHOT = "screen-shot";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String RGEX_TO_REPLACE_CHARACTERS = "\\p{Punct}";
	public static final String REPLACEMENT = "_";
	private final int maxWaitTime;
	private EventFiringWebDriver driver;
	private int errorCount = 0;

	private WebDriverEventListener errorListener = new AbstractWebDriverEventListener() {
		@Override
		public void onException(Throwable throwable, WebDriver driver) {
			errorCount++;
			String timeStamp = new SimpleDateFormat(DATE_TIME_FORMAT).format(Calendar.getInstance().getTime());
			String snapshotName;
			if (null != throwable) {// because the throwable could be null.
				snapshotName =
						timeStamp + " : " + "Error SnapShot" + errorCount + " : " + throwable.getLocalizedMessage();
			} else {
				snapshotName =
						timeStamp + " : " + "Error SnapShot" + errorCount;
			}
			captureScreenShot(snapshotName);
		}
	};

	public DevSWebDriver() throws Exception {
		driver = new EventFiringWebDriver(BrowserManager.getWebDriver());//firefox web driver
		maxWaitTime = UITestConstants.WAITING_TIME_CONSTANT;
		driver.register(errorListener);
	}

	/**
	 * This method takes a screen-shot of current web-driver instance     *
	 *
	 * @param snapShotName String indicating name of the screen-shot
	 */
	public void captureScreenShot(String snapShotName) {
		try {
			String filename = snapShotName + IMAGE_FILE_EXT;
			String pathName = UITestConstants.SUREFIRE_IMAGE_SAVE_LOCATION +
			                  SUREFIRE_REPORTS + File.separator + SCREEN_SHOT;
			log.error("OnException - Saving Screen-shot : " + filename + " to location " + pathName);
			File screenShot = this.driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenShot, new File(pathName + File.separator + filename));
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * This method will keep refreshing/reloading the current url for a given number of poll-count
	 * until a given element is available
	 *
	 * @param by        Element that is expected to be present
	 * @param pollCount Number of time page need to be reloaded into webdriver
	 */
	public void findElementPoll(By by, int pollCount) {
		int count = 0;
		while (!isElementPresent(by) && count < pollCount) {
			String url = driver.getCurrentUrl();
			driver.get(url);
			count++;
		}
	}

	/**
	 * This method checks whether a given element is present in the page
	 *
	 * @param by Element to be present in the page
	 * @return true if element is present false otherwise
	 */
	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * This method will wait untill a given element is present in the page for a given amount of time
	 *
	 * @param by          Element to be present in the current page
	 * @param waitTimeSec Time to wait in seconds
	 */
	private void waitTillElementPresent(By by, int waitTimeSec) {
		WebDriverWait wait;
		wait = new WebDriverWait(driver, waitTimeSec);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	@Override
	public void get(String s) {
		driver.get(s);
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	/**
	 * This method has override the findElement method in a way it will wait for maximum of 30 seconds
	 *
	 * @param by By element for findElement method
	 * @return return the result of default WebDriver.findElement(By by) subjected to 30sec of max wait time
	 */
	@Override
	public WebElement findElement(By by) {
		waitTillElementPresent(by, this.maxWaitTime);
		return driver.findElement(by);
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public void quit() {
		driver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}
}