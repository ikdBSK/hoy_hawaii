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
        this.error_message = error_message;
    }

    init(){
        if(this.init_uri === "NULL") return;
        fetch_json(this.init_uri).then(json => {
            for(const input of this.inputs){
                const type = $("#", + this.prefix + "_" + input).attr("type");
                if(type === "text"|| type === "number"){
                    $("#" + this.prefix + "_" + input).val(json[input]);
                }else if(type === "radio"){
                    $("input[name=" + this.prefix + "_" + input + "][value=" + json[input] + "]").prop("checked", true);
                }
            }
        }, error => {
            this.display(ERROR);
            console.log(error);
        });
    }

    post(){
        if (!$("#" + this.prefix + "_form")[0].checkValidity()) {
            $("#" + this.prefix + "_submit").trigger("click");
            this.display($("<p>").attr("style", "color:red").append("入力に間違いがあります。"));
            return false;
        }

        return fetch(this.post_uri, {
            method: 'post',
            body: get_form("#" + this.prefix + "_form")
        }).then(response => {
            if(!response.ok){
                this.display(this.error_message);
                console.log(response.statusText);
                throw new Error("Failed to post form: \"" + this.prefix + "\"");
            }
            return response.json();
        });
    }

    display(content){
        $(this.display_field).empty().append(content);
    }
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