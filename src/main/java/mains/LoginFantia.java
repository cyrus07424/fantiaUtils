package mains;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import constants.Configurations;
import utils.PlaywrightHelper;

/**
 * Fantiaにログイン.
 *
 * @author cyrus
 */
public class LoginFantia {

	/**
	 * main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("■start.");

		// Playwrightを作成
		try (Playwright playwright = Playwright.create()) {
			// ブラウザ起動オプションを取得
			BrowserType.LaunchOptions launchOptions = PlaywrightHelper.getLaunchOptions();

			// ブラウザを起動
			try (Browser browser = playwright.chromium().launch(launchOptions)) {
				System.out.println("■launched");

				// ブラウザコンテキストオプションを取得
				Browser.NewContextOptions newContextOptions = PlaywrightHelper.getNewContextOptions(false);

				// ブラウザコンテキストを取得
				try (BrowserContext context = browser.newContext(newContextOptions)) {
					// ページを取得
					try (Page page = context.newPage()) {
						// ログイン画面を表示
						page.navigate("https://fantia.jp/sessions/signin");

						// メールアドレスを入力
						page.locator("#user_email").fill(Configurations.FANTIA_EMAIL);

						// パスワードを入力
						page.locator("#user_password").fill(Configurations.FANTIA_PASSWORD);

						// ログインボタンをクリック
						page.locator("#new_user button.btn").click();

						// 読み込み完了まで待機
						page.waitForLoadState(LoadState.NETWORKIDLE);

						// ステートを出力
						context.storageState(
								new BrowserContext.StorageStateOptions().setPath(Configurations.STATE_PATH));
					}
				}
			}
		} finally {
			System.out.println("■done.");
		}
	}
}