// 画面遷移
function reset_screen(){
    $("#login_screen").hide();
    $("#home_screen").hide();
    $("#school_test_list_screen").hide();
    $("#school_test_detail_screen").hide();
    $("#subject_graph_screen").hide();
}

function home(){
    reset_screen();
    $("#home_screen").show();
}

function school_test_list(){
    reset_screen();
    $("#school_test_list_screen").show();
}

function school_test_detail(){
    reset_screen();
    $("#school_test_detail_screen").show();
}

function subject_graph_screen(){
    reset_screen();
    $("#subject_graph_screen").show();
}


// ログイン画面
const login_form = new form("/login", "NULL", "login", ["id", "password"], $("<p>").attr("style", "color:red").append("ユーザーID、又はパスワードが間違っています。"));
