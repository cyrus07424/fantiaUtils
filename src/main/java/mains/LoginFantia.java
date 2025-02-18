package mains;

import java.util.Scanner;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import utils.PlaywrightHelper;

/**
 * Fantiaにログイン.
 *
 * @author cyrus
 */
public class LoginFantia {

	/**
	 * メイン.
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

						// 読み込み完了まで待機
						page.waitForLoadState(LoadState.NETWORKIDLE);

						// Scanner
						try (Scanner scanner = new Scanner(System.in)) {
							System.out.print("メールアドレスを入力してください: ");
							String email = scanner.nextLine();

							// メールアドレスを入力
							page.locator("#user_email").fill(email);

							System.out.print("パスワードを入力してください: ");
							String password = scanner.nextLine();

							// パスワードを入力
							page.locator("#user_password").fill(password);
						}

						// ログインボタンをクリック
						page.locator("#new_user button.btn").click();

						// FIXME
						Thread.sleep(10000);

						// 読み込み完了まで待機
						page.waitForLoadState(LoadState.NETWORKIDLE);
					} finally {
						// コンテキストのステートを出力
						PlaywrightHelper.storageState(context);
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("■done.");
		}
	}
}