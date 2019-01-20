// ページロード完了時の動作
$(document).ready(() => {
    reset_screen();
    login();
    home();
});

//---------------------------------------------------------------------------------------------------
// 画面遷移
//---------------------------------------------------------------------------------------------------
// 画面を完全リセット
function reset_screen(){
    $("#login_screen").hide();
    $("#student_screen").hide();
    $("#teacher_screen").hide();
    $("#admin_screen").hide();

    $("#student_menu_bar").hide();
    $("#teacher_menu_bar").hide();
    $("#admin_menu_bar").hide();
}

// ログイン画面
function login_screen(){
    reset_screen();
    $("#login_screen").show();
}

// 生徒の画面
function student_screen(){
    reset_screen();
    student_reset_screen();
    $("#student_menu_bar").show();
    $("#student_screen").show();
    student_home();
}

// 生徒の画面をリセット
function student_reset_screen(){
    $("#student_home_screen").hide();
    $("#student_school_test_list_screen").hide();
    $("#student_school_test_detail_screen").hide();
    $("#student_subject_graph_screen").hide();
}

function student_home(){
    student_reset_screen();
    $("#student_home_screen").show();
}

// 生徒の受けた定期試験のリスト
function student_school_test_list(){
    student_reset_screen();
    school_test_list_table.refresh();
    $("#student_school_test_list_screen").show();
}

function init_buttons(){

}

function student_school_test_detail(){
    student_reset_screen();
    $("#student_school_test_detail_screen").show();
}

function student_subject_graph_screen(){
    student_reset_screen();
    $("#student_subject_graph_screen").show();
}

// 先生の画面
function teacher_screen(){
    reset_screen();
    teacher_reset_screen();
    $("#teacher_menu_bar").show();
    $("#teacher_screen").show();
    teacher_home();
}

function teacher_reset_screen(){}

function teacher_home(){}

// 管理者の画面
function admin_screen(){
    reset_screen();
    admin_reset_screen();
    $("#admin_menu_bar").show();
    $("#admin_screen").show();
    admin_home();
}

function admin_reset_screen(){}

function admin_home(){}
//---------------------------------------------------------------------------------------------------

// ログイン画面
const login_form = new form("/login", null, "login", ["id", "password"], $("<p>").attr("style", "color:red").append("ユーザーID、又はパスワードが間違っています。"));

// 定期試験の一覧
const school_test_list_table = new table(
    "school_exam_list",
    "school_test_list",
    ["年", "学期", "中間・期末", "校内順位", "操作"],
    [2, 2, 2, 0, 4],
    [["2017", "2018", "2019"], ["１学期", "２学期", "３学期"], ["中間", "期末"], [], []],
    10);


// 定期試験の詳細
let school_test_detail_table = null;

function open_school_test_detail(year, semester, term){
    school_test_detail_table = new table(
        "exam_test_list/" + year + "/" + semester + "/" + term,
        "school_test_detail",
        ["科目", "点数", "平均", "偏差値", "順位", "赤点"],
        [2, 0, 0, 0, 0, 3],
        [["国語", "数学", "英語"], [], [], [], [], ["赤点", "合格", "不合格"]],
        10
    );
}

// 科目ごとの成績遷移

//---------------------------------------------------------------------------------------------------//
//***************************************************************************************************//
// 管理者
//***************************************************************************************************//
const admin_new_account_form = new form("signup", null, "signup", ["id", "password", "name", "sex", "address", "入力に誤りがあります。"]);