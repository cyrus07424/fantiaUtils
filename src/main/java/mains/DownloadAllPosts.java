package mains;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;

import utils.FileHelper;
import utils.PlaywrightHelper;

/**
 * ファンクラブの全ての投稿をダウンロード.
 *
 * @author cyrus
 */
public class DownloadAllPosts {

	/**
	 * ダウンロードするファンクラブのID一覧.
	 */
	private static final int[] TARGET_FANCLUB_ID_ARRAY = { 485135 };

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
				Browser.NewContextOptions newContextOptions = PlaywrightHelper.getNewContextOptions(true);

				// ブラウザコンテキストを取得
				try (BrowserContext context = browser.newContext(newContextOptions)) {
					// FIXME 拡張子が画像のリクエストは中断
					context.route("**/*.{png,jpg,jpeg}", route -> {
						route.abort();
					});

					// ページを取得
					try (Page page = context.newPage()) {
						// 全てのファンクラブのIDに対して実行
						for (int fanclubId : TARGET_FANCLUB_ID_ARRAY) {

							// 処理済みの投稿数
							int processedPostCount = 0;
							try {
								// 投稿一覧画面を表示
								Response response = page
										.navigate(String.format("https://fantia.jp/fanclubs/%d/posts", fanclubId));

								// 先頭の投稿を取得して遷移
								page.locator(".post a").first().click();

								while (true) {
									processedPostCount++;

									// 読み込み完了まで待機
									page.waitForLoadState(LoadState.NETWORKIDLE);

									// FIXME 429 Too Many Requestsエラーを回避
									if (response.status() == 429) {
										page.wait(10000);
										page.reload();
									}

									// 投稿内の全ての画像に対して実行
									for (Locator img : page.locator(".the-post .post-thumbnail img").all()) {
										try {
											// 画像のURLを取得
											String src = img.getAttribute("src");
											System.out.println("src:" + src);

											// 保存
											FileHelper.saveContent(String.valueOf(fanclubId), src);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									// サムネイルのリンクを取得
									for (Locator thumbnail : page.locator(".image-thumbnails.full-xs a").all()) {
										try {
											// リンクをクリック
											thumbnail.click();

											// スライドショー内の画像を取得
											Locator img = page.locator("#image-slideshow img");

											// 画像のURLを取得
											String src = img.getAttribute("src");
											System.out.println("src:" + src);

											// 保存
											FileHelper.saveContent(String.valueOf(fanclubId), src);

											// 閉じるボタンを取得してクリック
											page.locator("#image-slideshow .btn-dark").click();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									// 前の投稿リンクを取得して遷移
									page.locator("a.post-prev.post-pager").first().click();
								}
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								System.out.println("processedPostCount:" + processedPostCount);
							}
						}
					}
				}
			}
		} finally {
			System.out.println("■done.");
		}
	}
}