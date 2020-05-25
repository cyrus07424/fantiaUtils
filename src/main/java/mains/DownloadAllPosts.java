package mains;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.FileHelper;
import utils.SeleniumHelper;

/**
 * ファンクラブの全ての投稿をダウンロード.
 *
 * @author cyrus
 */
public class DownloadAllPosts extends AbstractCrawler {

	/**
	 * ダウンロードするファンクラブのID.
	 */
	private static final int FANCLUB_ID = 0;

	/**
	 * main.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// クロール処理
		crawlMain((WebDriver webDriver) -> {
			// 処理済みの投稿数
			int processedPostCount = 0;
			try {
				// 投稿一覧画面を表示
				webDriver.get(String.format("https://fantia.jp/fanclubs/%d/posts", FANCLUB_ID));

				// 先頭の投稿を取得して遷移
				WebElement firstPost = webDriver.findElement(By.cssSelector(".post a"));
				webDriver.get(firstPost.getAttribute("href"));
				SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);

				while (true) {
					processedPostCount++;

					// FIXME スリープ
					Thread.sleep(5000);

					// 投稿内の全ての画像に対して実行
					for (WebElement img : webDriver.findElements(By.cssSelector(".the-post .post-thumbnail img"))) {
						// 画像のURLを取得
						String src = img.getAttribute("src");
						System.out.println("src:" + src);

						// 保存
						FileHelper.saveContent(String.valueOf(FANCLUB_ID), src);
					}

					// サムネイルのリンクを取得
					for (WebElement thumbnail : webDriver
							.findElements(By.cssSelector(".image-thumbnails.full-xs a"))) {
						try {
							// リンクをクリック
							thumbnail.click();
							SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);

							// スライドショー内の画像を取得
							WebElement img = webDriver.findElement(By.cssSelector("#image-slideshow img"));

							// 画像のURLを取得
							String src = img.getAttribute("src");
							System.out.println("src:" + src);

							// 保存
							FileHelper.saveContent(String.valueOf(FANCLUB_ID), src);

							// 閉じるボタンを取得してクリック
							webDriver.findElement(By.cssSelector("#image-slideshow .btn-dark")).click();
							SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// 前の投稿リンクを取得して遷移
					WebElement postPrev = webDriver.findElement(By.cssSelector("a.post-prev.post-pager"));
					webDriver.get(postPrev.getAttribute("href"));
					SeleniumHelper.waitForBrowserToLoadCompletely(webDriver);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("processedPostCount:" + processedPostCount);
			}
		});
	}
}