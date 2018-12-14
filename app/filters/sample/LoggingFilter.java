package filters.sample;

import play.Logger;
import play.mvc.EssentialAction;
import play.mvc.EssentialFilter;

import javax.inject.Singleton;


/**
 * Filterのサンプル（アクセスについてログをとる）
 *
 * RequestおよびResponseに介入して処理を追加することができる
 */
@Singleton
public class LoggingFilter extends EssentialFilter {

    /**
     * アクション実行前にアクセスについてログをとる
     *
     * @param next アクション
     * @return レスポンス
     */
    @Override
    public EssentialAction apply(EssentialAction next) {
        return EssentialAction.of(request -> {
            // アクセス元およびアクセス先についてログを出力する
            Logger.info("access from " + request.remoteAddress() + " to " + request.uri());
            return next.apply(request);
        });
    }

}
