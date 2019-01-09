// JSON形式のデータを取得
function fetch_json(uri){
    return fetch(uri)
        .then(response => {
            if(response.ok) {
                return response.json();
            } else {
                throw new Error(response.statusText);
            }
        });
}

// ディスプレイメッセージ
const ERROR = $("<p>").attr("style", "color:red").append("通信に失敗しました。");
const LOADING = "LOADING...";
const NO_ITEM = "データがありません。";

// アカウント管理
let username = null;

// ログイン
function login(name){
    username = name;
    display_name();
}

// ログアウト
function logout(){
    username = null;
    display_name();
}

// 名前を表示
function display_name(){
    let display;
    if(username === null){
        display = "";
    }else if(username === "管理者"){
        display = "<b class=\"header_name\" style='color: red;'>管理者モード</b>";
    }else{
        display = "ようこそ、<b class=\"header_name\">" + username + "</b>さん";
    }
    $("#username_display").empty().append(display);
}