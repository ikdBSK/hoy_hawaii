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
    school_test_list_table.refresh();
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
