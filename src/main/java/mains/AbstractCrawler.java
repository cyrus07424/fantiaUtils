package mains;

import java.util.function.Consumer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import constants.Configurations;
import utils.SeleniumHelper;

/**
 * ベースクラス.
 *
 * @author cyrus
 */
public abstract class AbstractCrawler {

	/**
	 * クロール共通処理.
	 *
	 * @param consumer
	 */
	public static void crawlMain(Consumer<WebDriver> consumer) {
		// WebDriver
		WebDriver webDriver = null;
		try {
			// WebDriverを取得
			webDriver = SeleniumHelper.getWebDriver();

			// ログイン画面を開く
			webDriver.get("https://fantia.jp/sessions/signin");
			SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);

			// メールアドレスを入力
			webDriver.findElement(By.cssSelector("#user_email")).sendKeys(Configurations.FANTIA_EMAIL);

			// パスワードを入力
			webDriver.findElement(By.cssSelector("#user_password")).sendKeys(Configurations.FANTIA_PASSWORD);

			// ログインボタンをクリック
			webDriver.findElement(By.cssSelector("button.btn")).click();

			// ダッシュボードが表示されるまで待機
			SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);

			// クロール処理メイン
			consumer.accept(webDriver);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// WebDriverを終了
			if (webDriver != null) {
				webDriver.quit();
			}

			// 終了
			System.exit(0);
		}
	}
}