!function($) {
    // ページロード完了時に行う操作
    $(document).ready(() => {
        // ページロード完了時に投稿済みアンケート一覧表をアップデートする
        update_enquete_list();
    });
}(jQuery);

// 登録フォームを開く
function open_registration_form() {
    const $ = jQuery;
    // 詳細表示が開いていれば閉じる
    close_detail_form();
    // 登録フォームをリストの横に開く
    shrink_enquete_list();
    $("#ajax-register-enquete").show();
}

// 登録フォームを閉じる
function close_registration_form() {
    const $ = jQuery;
    // 登録フォームをクリアして不可視化
    $("#error-field").text("");
    $("#registration-form").trigger("reset");
    $("#ajax-register-enquete").hide();
    // アンケートリストの大きさを戻す
    expand_enquete_list();
}

// 詳細表示を開く
function open_detail_form(id) {
    // 詳細情報をサーバに問い合わせる
    fetch("/sample/ajax/entry/" + id).then(res => {
        if(!res.ok) {
            throw new Error("アンケート #" + id + " は存在しません");
        } else {
            return res.json();
        }
    }).then(json => {
        const $ = jQuery;
        // 登録フォームが開いていれば閉じる
        close_registration_form();
        // 詳細表示の項目を埋める
        $("#detail-id").text(json["id"]);
        $("#detail-name").text(json["name"]);
        $("#detail-sex").text(sexText(json["sex"]));
        $("#detail-registered-at").text(fdate(json["registeredAt"]));
        $("#detail-updated-at").text(fdate(json["updatedAt"]));
        // アンケートリストの横に詳細情報を表示する
        shrink_enquete_list();
        $("#ajax-enquete-detail").show();
    }, error => {
        alert(error.message);
    });
}

// 詳細表示を閉じる
function close_detail_form() {
    const $ = jQuery;
    // 詳細表示を不可視化し，アンケートリストを元の大きさに戻す
    $("#ajax-enquete-detail").hide();
    expand_enquete_list();
}

// アンケートリストの更新
function update_enquete_list() {
    // サーバにアンケートリストを問い合わせる
    fetch("/sample/ajax/entries").then(response => {
        // 結果をJSONとして受け取る
        return response.json();
    }).then(entries => {
        update_table(entries);
    });
}

// アンケートリストのテーブル表示を更新する
function update_table(entries) {
    const $ = jQuery;
    if(entries.length === 0) {
        // アンケートが一件もないときはフィールドをクリアしてメッセージを表示
        $("#display-field")
            .empty()
            .append("<p>投稿は一件もありません</p>");
    } else {
        // アンケートが存在するときは，フィールドをクリアしてテーブルを再構築
        $("#display-field")
            .empty()
            .append(create_table(entries));
    }
}

// アンケートリストのテーブルを構築する
function create_table(entries) {
    const $ = jQuery;
    const table = $("<table>");
    const header = $("<tr>");
    for(const label of ["ID", "投稿者", "最終更新日時"]) {
        header.append($("<th>").text(label));
    }
    table.append(header);
    for(const entry of entries) {
        const id = $("<td>").append($("<a>").attr("href", "#")
                                            .on('click', () => {
                                                open_detail_form(entry["id"]);
                                            }).text(entry["id"]))
                                            .addClass("id");
        const name = $("<td>").text(entry["name"]).addClass("name");
        const upd = $("<td>").text(fdate(entry["updatedAt"])).addClass("date");
        const row = $("<tr>").append(id)
                             .append(name)
                             .append(upd);
        table.append(row);
    }
    return table;
}

// 投稿する
function register_new() {
    // サーバにフォーム情報を送信する
    fetch("/sample/ajax/create", {
        method: 'post',
        body: get_form("#registration-form"),
    }).then(res => {
        if(!res.ok) {
            throw new Error("入力に誤りがあります");
        } else {
            return res.json();
        }
    }).then(json => {
        // 正常な応答が返ってきたら，テーブルを更新する
        update_table(json);
        close_registration_form();
    }, error => {
        const $ = jQuery;
        $("#error-field").text("入力に誤りがあります");
    });
}

// 削除
function delete_entry() {
    const $ = jQuery;
    const id = $("#detail-id").text();
    // 確認画面を表示
    if(id.length && confirm("#" + id + " を削除してよろしいですか？")) {
        // サーバに削除を問い合わせ
        fetch("/sample/ajax/delete/" + id).then(res => {
            if(!res.ok) {
                throw new Error("削除できませんでした");
            } else {
                return res.json();
            }
        }).then(json => {
            // 正常な応答が返ってきたら，テーブルを更新する
            update_enquete_list(json);
            close_detail_form();
        }, error => {
            close_detail_form();
            alert(error.message);
        });
    }
}

/* Helper functions */
// アンケートリストの幅を1/2に縮める
function shrink_enquete_list() {
    const $ = jQuery;
    $("#ajax-enquete-list").removeClass("col-full").addClass("col-half");
}

// アンケートリストのサイズを元に戻す
function expand_enquete_list() {
    const $ = jQuery;
    $("#ajax-enquete-list").removeClass("col-half").addClass("col-full");
}

// フォーム情報を取得する
function get_form(tag) {
    const $ = jQuery;
    const data = new URLSearchParams();
    for (const pair of new FormData($(tag)[0])) {
        data.append(pair[0], pair[1]);
    }
    return data;
}

// 日時の表示をフォーマットする
function fdate(date) {
    return new Date(date).toLocaleString('en-GB', {timeZone: 'JST'});
}

// 性別の表示を日本語化
function sexText(sex) {
    if(sex === "male") {
        return "男性";
    }
    if(sex === "female") {
        return "女性";
    }
    return "不正値";
}
