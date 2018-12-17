package controllers;

import play.mvc.*;

import views.html.*;


/**
 * TODOリストのコントローラ
 */
public class TodoListController extends Controller {

    /**
     * トップページを表示する
     * @return トップページ
     */
    public Result index() {
        return ok(index.render());  // `200 OK`でトップページをクライアントへ返す
    }

}
