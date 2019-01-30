class form{
    constructor(post_uri, init_uri, prefix, inputs, error_message){
        this.post_uri = post_uri; // POSTの宛先
        this.init_uri = init_uri; // 初期値の取得先。なければ、NULL

        this.prefix = prefix; // HTMLタグのプレフィックス
        this.inputs = inputs; // 入力項目のHTMLタグ
        // 例えば、tag_prefix = "test", inputs = ["name", "password"]
        // で初期化すれば、HTMLファイルの"test_name"と"test_password"が
        // <input>のidとなる。

        this.display_field = "#" + this.prefix + "_display";
        this.error_message = $("<p>").attr("style", "color: red").append(error_message);
    }

    init(){
        this.display(LOADING);
        if(this.init_uri === null) return;
        fetch_json(this.init_uri).then(json => {
            for(const input of this.inputs){
                const form = $("#" + this.prefix + "_form");
                const element = form.find("[name=" + input + "]");
                if(element.attr("type") === "radio"){
                    form.find("[name=" + input + "][value=" + json[input] + "]")
                        .attr("checked", true);
                } else {
                    element.val(json[input]);
                }
            }
            this.display("");
        }, error => {
            this.display(ERROR);
            console.log(error);
        });
    }

    custom_init(uri){
        const temp = this.init_uri;
        this.init_uri = uri;
        this.init();
        this.init_uri = temp;
    }

    post(){
        if (!$("#" + this.prefix + "_form")[0].checkValidity()) {
            $("#" + this.prefix + "_submit").trigger("click");
            this.display($("<p>").attr("style", "color:red").append("入力に間違いがあります。"));
            return false;
        }

        this.display(LOADING);

        return fetch(this.post_uri, {
            method: 'post',
            body: get_form("#" + this.prefix + "_form")
        }).then(response => {
            if(!response.ok){
                this.display(this.error_message);
                // console.log(response.statusText);
            }else{
                this.display("");
            }
            return response;
        }, error => {
            this.display(ERROR);
            console.log(error);
        });
    }

    custom_post(uri){
        const tmp = this.post_uri;
        this.post_uri = uri;
        this.post();
        this.post_uri = tmp;
    }

    display(content){
        $(this.display_field).empty().append(content);
    }

    // reset(){
    //     $("#" + this.prefix + "_form").reset();
    // }
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

// 定期試験のリスト内の詳細ボタン生成
function init_school_test_list_buttons() {
    $("#school_exam_list_table tr").each(() => {
        const year = $(this).find("<td>").eq(0).text();
        const semester = $(this).find("<td>").eq(1).text();
        const term = ($(this).find("<td>").eq(2).text() === "中間") ? 0 : 1;
        $(this).find("<td>").eq(school_test_list_table.label_count - 1).append(
           $("<button>")
               .attr("onclick", "open_school_test_detail(" + year + ", " + semester + ", " + term + "); return false;")
               .text("詳細")
       );
    });
}