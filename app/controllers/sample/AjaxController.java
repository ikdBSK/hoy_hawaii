package controllers.sample;

import java.util.List;
import java.util.Map;

import io.ebean.Ebean;
import play.libs.Json;
import play.mvc.*;

import models.sample.Enquete;
import views.html.sample.ajax.*;


/**
 * Ajaxのサンプル
 */
public class AjaxController extends Controller {

    /**
     * トップページを返す
     * @return トップページ
     */
    public Result index() {
        return ok(index.render());
    }

    /**
     * 全てのエントリーを返す
     * @return JSON形式のエントリーリスト
     */
    public Result list() {
        List<Enquete> entries = Ebean.find(Enquete.class).findList();
        return ok(Json.toJson(entries));
    }

    /**
     * フォームを受け取り，新たなエントリを保存する
     * @return 新たなエントリを保存した後の投稿リスト
     */
    public Result create() {
        try {
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            final String name = form.get("name")[0];
            final Enquete.SexTag sex = Enquete.SexTag.valueOf(form.get("sex")[0]);
            final String impressions = form.get("impressions")[0];
            final Enquete entry = new Enquete(name, sex, impressions);
            entry.save();
        } catch(Exception e) {
            return badRequest();
        }
        return list();
    }

    /**
     * 特定のエントリのみを返す
     * @param id 返して欲しいエントリのID
     * @return JSON形式でエントリ（あるいは空のJSON）を返す
     */
    public Result fetch(Long id) {
        Enquete entry = Ebean.find(Enquete.class).setId(id).findOne();
        if(entry == null) {
            return notFound();
        }
        return ok(Json.toJson(entry));
    }

    /**
     * 特定のエントリを削除する
     * @param id 削除するエントリのID
     * @return 削除後の投稿リスト
     */
    public Result delete(Long id) {
        Ebean.find(Enquete.class).setId(id).findOneOrEmpty().map(entry -> entry.delete());
        return list();
    }

}
