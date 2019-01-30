// ページロード完了時の動作
$(document).ready(() => {
    reset_screen();
    login();
    home();
});

tables = [];
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
//***************************************************************************************************//
// 生徒
//***************************************************************************************************//
// 定期試験の一覧
const student_school_test_list_table = new table(
    "school_exam_list",
    "school_test_list",
    ["年", "学期", "中間・期末", "校内順位", "操作"],
    ["year", "semester", "term", "rank", null],
    [2, 2, 2, 0, 4],
    [["2017", "2018", "2019"], ["１学期", "２学期", "３学期"], ["中間", "期末"], [], [student_school_test_list_mod]],
    10
);
tables.push(student_school_test_list_table);
function student_school_test_list_mod(tr){
    const year = tr.find("td").eq(0).text();
    const semester = tr.find("td").eq(1).text();
    const term = (tr.find("td").eq(2).text() === "中間") ? 0 : 1;
    const button = tr.find("td").eq(4);

    button.append($("<button>")
        .attr("onclick",
            "$('#student_school_test_detail').toggle();" +
            "student_school_test_detail_table.custom_refresh('school_exam_detail/" + year + "/" + semester + "/" + term + "');" +
            "return false;")
        .text("詳細を見る"));
}

// 定期試験の詳細
const student_school_test_detail_table = new table(
    "school_exam_detail",
    "school_test_detail",
    ["科目", "点数", "順位", "偏差値"],
    ["subject", "score", "rank", "d_value"],
    [0, 0, 0, 0],
    [[], [], [], []],
    10
);
tables.push(student_school_test_detail_table);

// 科目ごとの成績遷移
// const student_subject_history = new table(
//     "subject"
// );

//---------------------------------------------------------------------------------------------------//
//***************************************************************************************************//
// 先生
//***************************************************************************************************//
const teacher_subject_class_list = new table(
    "subject_class_list",
    "teacher_subject_class_list",
    ["科目名", "担当教師", "学年", "年", "学期", "詳細"],
    ["subject", "teacher", "grade", "year", "semester", null],
    [0, 0, 2, 0, 2, 4],
    [[], [], ["1", "2", "3"], [], ["1", "2", "3"], []],
    5
);
tables.push(teacher_subject_class_list);

const teacher_add_test_form = new form(
    "add_test",
    null,
    "teacher_add_test",
    ["year", "semester", "term", "name", "grade", "month", "day", "division"],
    "入力に間違いがあります。"
);

const teacher_add_result_form = new form(
    "add_result",
    null,
    "teacher_add_result",
    ["year", "semester", "term", "name", "grade", "score", "id"],
    "入力に間違いがあります。"
);

function teacher_subject_student_list_refresh(){
    const form = $("#teacher_subject_student_result_form");
    const year = form.find("[name=year]").val();
    const semester = form.find("[name=semester]").val();
    const term = form.find("[name=term]").val();
    const name = form.find("[name=name]").val();
    const grade = form.find("[name=grade]").val();

    teacher_subject_student_result_table.custom_refresh("students_results/" + year + "/" + semester + "/" + term + "/" + name + "/" + grade);
}

const teacher_subject_student_result_table = new table(
    "students_results",
    "teacher_subject_student_result",
    ["生徒ID", "点数", "偏差値", "順位"],
    ["student", "score", "d_value", "rank"],
    [0, 0, 0, 0],
    [[], [], [], []],
    5
);
tables.push(teacher_subject_student_result_table);

const teacher_external_exam_list_table = new table(
    "external_exam_list",
    "teacher_external_list",
    ["生徒名", "科目名", "点数", "偏差値", "順位"],
    ["student", "subject", "score", "d_value", "rank"],
    [0, 0, 0, 0, 0],
    [[], [], [], [], []],
    5
);
tables.push(teacher_external_exam_list_table);

const teacher_external_exam_new_form = new form(
    "add_ex_result",
    null,
    "external_exam_result",
    ["year", "month", "day", "type", "name", "id", "score", "d_value", "rank"],
    "わはははっははは"
);
//***************************************************************************************************//
//***************************************************************************************************//
// 管理者
//***************************************************************************************************//
const admin_new_account_form = new form("signup", null, "signup", ["id", "password", "name", "sex", "address"], "入力に誤りがあります。");
const admin_new_school_exam_form = new form("make_school_exam", null, "school_exam", ["year", "semester", "term"], "入力に誤りがあります。");
const admin_new_subject_form = new form("add_subject", null, "subject", ["name", "credits"], "入力に間違いがあります。");
const admin_new_subject_class_form = new form("make_subject_class", null, "subject_class", ["subject_name", "teacher_id", "students_id", "year", "semester", "number"], "入力に間違いがあります。");
const admin_new_class_form = new form("make_classrooom", null, "class", ["grade_num", "class_num", "teacher_id", "students_ids"], "入力に間違いがあります。");

const admin_account_list_table = new table(
    "account_list",
    "admin_account_list",
    ["ID", "名前", "性別", "住所", "操作"],
    ["id", "name", "sex", "address", "control"],
    [0, 0, 2, 0, 4],
    [[], [], ["男性", "女性"], [], [admin_account_list_table_mod]],
    5
);
tables.push(admin_account_list_table);
function admin_account_list_table_mod(tr){
    const id = tr.find("td").eq(0).text();
    const button = tr.find("td").eq(4);

    button.append($("<button>")
        .attr("onclick",
            "$('#admin_edit_account').toggle();" +
            "admin_edit_account_form.custom_init(\"fetch/" + id + "\");" +
            "return false;")
        .text("編集"));
}

const admin_edit_account_form = new form(
    "edit",
    "fetch",
    "account_edit",
    ["id", "password", "name", "sex", "address"],
    "入力に間違いがあります。"
);

const admin_subject_list_table = new table(
    "subject_list",
    "admin_subject_list",
    ["科目名", "単位数", "操作"],
    ["name", "credits", "control"],
    [0, 1, 4],
    [[], [], [admin_subject_list_table_mod]],
    5
);
tables.push(admin_subject_list_table);
function admin_subject_list_table_mod(tr){
    const name = tr.find("td").eq(0).text();
    const button = tr.find("td").eq(2);

    button.append($("<button>")
        .attr("onclick",
            "$('#admin_subject_detail').toggle();" +
            "admin_subject_detail_table.custom_refresh(\"subject_detail/" + name + "\");" +
            "return false;"
        )
        .text("詳細")
    );
}

const admin_subject_detail_table = new table(
    "subject_detail",
    "admin_subject_detail",
    ["科目名", "担当教師", "学年", "年", "学期"],
    ["subject", "teacher", "grade", "year", "semester"],
    [0, 0, 2, 0, 2],
    [[], [], ["1", "2", "3"], [], ["1", "2", "3"]],
    5
);
tables.push(admin_subject_detail_table);

const admin_new_external_exam_form = new form(
    "make_external_exam",
    null,
    "external_exam",
    ["year", "month", "day", "type"],
    "入力に黄色くて暖かいものが混じっています。"
);

const admin_class_list = new table(
    "classroom_list",
    "class_list",
    ["年", "学年", "先生ID", "生徒ID（コンマ区切り）"],
    ["year", "grade", "teacher", "students_id"],
    [1, 2, 0, 0],
    [[], ["1", "2", "3"], [], []],
    5
);
tables.push(admin_class_list);
//***************************************************************************************************//