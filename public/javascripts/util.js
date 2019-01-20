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
function login(){
    get_username().then(response => {
        //if(!response.ok) $("#username_display").append($("<p>").attr("style", "color:red").append("通信に失敗しました。"));
        return response.json();
    }, error => {
        //$("#username_display").append($("<p>").attr("style", "color:red").append("通信に失敗しました。"));
        console.log(error);
    }).then(name => {
        username = name;
        display_name();
        home();
    });
}

// 適切なホーム画面に画面遷移
function home(){
    const status = $("#login_status").text();
    if(status === "NONE") login_screen();
    if(status === "STUDENT") student_screen();
    if(status === "TEACHER") teacher_screen();
    if(status === "ADMIN")   admin_screen();
    if(status !== "NONE") {
        $("#logout_button").show();
    }else{
        login_screen();
    }
    console.log(status);
}

// ログアウト
function logout(){
    username = null;
    display_name();
    fetch("/logout").then(response =>{
        if(!response.ok){
            $("#username_display").append($("<p>").attr("style", "color:red").append("通信に失敗しました。"));
        }
        $("#logout_button").hide();
        reset_screen();
        login_screen();
    }, error => {
        $("#username_display").append($("<p>").attr("style", "color:red").append("通信に失敗しました。"));
        console.log(error);
    });
}

function get_username() {
    return fetch("/username");
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