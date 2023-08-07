package constants;

/**
 * 環境設定.
 *
 * @author cyrus
 */
public interface Configurations {

	/**
	 * ChromeDriverの実行ファイルのパス.
	 */
	String CHROME_DRIVER_EXECUTABLE_PATH = "./drivers/chromedriver-windows-32bit/chromedriver.exe";

	/**
	 * ブラウザをヘッドレスモードで使用するか.
	 */
	boolean USE_HEADLESS_MODE = false;

	/**
	 * Fantiaアカウントのメールアドレス.
	 */
	final String FANTIA_EMAIL = "CHANGE ME";

	/**
	 * Fantiaアカウントのパスワード.
	 */
	final String FANTIA_PASSWORD = "CHANGE ME";
}