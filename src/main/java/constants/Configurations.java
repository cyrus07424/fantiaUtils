package constants;

import java.io.File;
import java.nio.file.Path;

/**
 * 環境設定.
 *
 * @author cyrus
 */
public interface Configurations {

	/**
	 * ブラウザをヘッドレスモードで使用するか.
	 */
	boolean USE_HEADLESS_MODE = false;

	/**
	 * Fantiaアカウントのメールアドレス.
	 */
	final String FANTIA_EMAIL = "CHANGEME";

	/**
	 * Fantiaアカウントのパスワード.
	 */
	final String FANTIA_PASSWORD = "CHANGEME";

	/**
	 * クッキーCSVファイルの出力先.
	 */
	Path STATE_PATH = new File("data/state.json").toPath();
}