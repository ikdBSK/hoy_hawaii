package controllers.sample;

import java.util.List;
import java.util.Map;

import io.ebean.Ebean;
import play.mvc.*;

import models.sample.Enquete;
import views.html.sample.enquete.*;


/**
 * Play版アンケートフォーム
 */
public class EnqueteController extends Controller {

    private static final String sexLabel = "sex";
    private static final String nameLabel = "name";
    private static final String impressionsLabel = "impressions";

    /* ***** 課題1の内容（セッション方式）***** */
    /**
     * アンケートの開始
     * @return 初期画面
     */
    public Result startRegistration() {
        // `200 OK`ステータスで初期画面を返す（リクエストヘッダなどは`ok`メソッドが自動で記述してくれる）
        return ok(start.render());
    }

    /**
     * 性別入力画面の表示
     * @return 性別入力画面
     */
    public Result sexForm() {
        // 性別入力画面を返す
        return ok(sexForm.render());
    }

    /**
     * 性別入力を受け取り，名前入力に処理をすすめる
     * @return 名前入力画面
     */
    public Result handleSexForm() {
        // リクエストBodyから性別を取得
        final String sex = request().body().asFormUrlEncoded().get(sexLabel)[0];
        if(sex == null) {
            return notAcceptable("性別入力に誤りがあります");
        }
        // 送信された性別情報をセッション内に保存
        session(sexLabel, sex);
        // 名前入力画面へ進む
        return ok(nameForm.render());
    }

    /**
     * 名前入力を受け取り，感想入力に処理をすすめる
     * @return 感想入力画面
     */
    public Result handleNameForm() {
        final String name = request().body().asFormUrlEncoded().get(nameLabel)[0];
        if(name == null) {
            return notAcceptable("名前入力に誤りがあります");
        }
        session(nameLabel, name);
        return ok(impressionsForm.render());
    }

    /**
     * 感想入力を受け取り，確認画面を表示する
     * @return 確認画面
     */
    public Result handleImpressionsForm() {
        final String impressions = request().body().asFormUrlEncoded().get(impressionsLabel)[0];
        if(impressions == null) {
            return notAcceptable("感想入力に誤りがあります");
        }
        session(impressionsLabel, impressions);
        // セッションから情報を取り出し，確認画面を表示
        final String name = session(nameLabel);
        final String sex = session(sexLabel);
        return ok(confirm.render(name, sex, impressions));
    }

    /**
     * 投稿内容を保存し完了画面を表示
     * @return 完了画面
     */
    public Result save() {
        // セッションから情報を取り出す
        final String name = session(nameLabel);
        final Enquete.SexTag sex = Enquete.SexTag.valueOf(session(sexLabel));
        final String impressions = session(impressionsLabel);
        // モデルを構築し，DBに保存
        final Enquete newEntry = new Enquete(name, sex, impressions);
        newEntry.save();
        // セッション情報をクリアし，完了画面を返す
        session().clear();
        return ok(complete.render(newEntry));
    }

    /* ***** おまけ：Playサンプル ***** */
    /**
     * トップページにDBに保存されているアンケートを一括表示
     * @return 投稿済みアンケートのリスト
     */
    public Result index() {
        final List<Enquete> entries = Ebean.find(Enquete.class).findList();
        return ok(index.render(entries));
    }

    /**
     * 指定されたIDのアンケートを表示
     * @param id 表示するアンケートのID
     * @return アンケート表示画面
     */
    public Result entry(Long id) {
        final Enquete targetEntry = Ebean.find(Enquete.class).setId(id).findOne();
        if(targetEntry == null) {
            return notFound("Not found: entry #" + id);
        }
        return ok(entry.render(targetEntry));
    }

    /**
     * 指定されたIDのアンケートの編集画面を表示
     * @param id 編集するアンケートのID
     * @return アンケート編集画面
     */
    public Result edit(Long id) {
        final Enquete targetEntry = Ebean.find(Enquete.class).setId(id).findOne();
        if(targetEntry == null) {
            return notFound("Not found: entry #" + id);
        }
        session("id", Long.toString(targetEntry.id));
        return ok(edit.render(targetEntry));
    }

    /**
     * アンケートのアップデート
     * @return 投稿済みアンケートのリスト
     */
    public Result update() {
        // モデル更新にはトランザクションを利用する
        Ebean.beginTransaction();
        try {
            // 更新するモデルの取得
            final Long id = Long.parseLong(session("id"));
            final Enquete targetEntry = Ebean.find(Enquete.class).setId(id).findOne();
            // 編集されたパラメータの取得
            final Map<String, String[]> request = request().body().asFormUrlEncoded();
            // モデル更新
            targetEntry.name = request.get("name")[0];
            targetEntry.sex = Enquete.SexTag.valueOf(request.get("sex")[0]);
            targetEntry.impressions = request.get("impressions")[0];
            targetEntry.save();
            // トランザクションをコミット
            Ebean.commitTransaction();
        } catch(NullPointerException npe) {
            return notAcceptable();
        } finally {
            // トランザクションを終了
            Ebean.endTransaction();
        }
        session().clear();
        return redirect("/sample/enquete");
    }

    /**
     * 指定されたIDのアンケートを削除
     * @param id 削除するアンケートのID
     * @return 一覧表示画面
     */
    public Result delete(Long id) {
        // `findOne`だとnullに対しdelete()を呼ぶ場合があるため，`findOneOrEmpty`でOptionalとして操作する
        Ebean.find(Enquete.class).setId(id).findOneOrEmpty().map(entry -> entry.delete());
        return redirect("/sample/enquete");
    }

}
