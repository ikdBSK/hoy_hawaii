const $ = jQuery;

class table{
    constructor(update_uri, tag_prefix, labels, names, types, options, items_per_page){
        // テーブル更新の問い合わせ先
        this.uri = update_uri;

        // htmlタグのプレフィックス
        this.prefix = tag_prefix;

        // テーブルのラベルの配列
        this.labels = labels;
        this.names = names;

        // テーブルの要素の種類を表す配列
        this.types = types;
        // types:
        // 0: 文字列（文字列検索対象）
        // 1: 文字列（文字列検索対象外）
        // 2: 文字列でない、選択肢検索
        // 3: 文字列でない、チェックボックス検索
        // 4: 検索対象外

        // 検索が選択肢の場合、その選択肢の配列
        // チェックボックス検索の場合は、初めから順に、ラベル、真のときの値、偽の時の値
        this.options = options;

        // 一ページ当たりのアイテム数
        this.items_per_page = items_per_page;

        this.display_field = "#"+ this.prefix + "_display";
        this.label_count = labels.length;
        this.current_page = 0;

        // 検索バーの生成
        const temp = $("<td>").attr("colspan", this.label_count);
        let text_search = false;
        let count = 0;
        for(const type of this.types){
            if(type === 0 && !text_search){
                // 文字列検索
                text_search = true;
                temp.append(
                    $("<div>").append(
                        $("<input>")
                            .attr("type", "text")
                            .attr("id", this.prefix + "_search_text")
                            .attr("onkeyup", "search_table(\"" + this.prefix + "\"); return false;")
                            .attr("placeholder", "検索")));
            }else if(type === 2){
                // 選択肢検索
                const temp1 = $("<select>")
                                    .attr("id", this.prefix + "_search_select_" + count)
                                    .attr("onchange", "search_table(\"" + this.prefix + "\"); return false;");

                temp1.append($("<option>").attr("value", "all").text("全て"));
                let count1 = 0;
                for(const option of this.options[count]){
                    temp1.append($("<option>").attr("value", count1).text(option));
                    count1++;
                }

                temp.append(
                    $("<div>").append(
                        $("<label>").text(labels[count] + "：")
                    ).append(temp1)
                );
            }else if(type === 3){
                // チェックボックス検索
                temp.append($("<input>").attr("type", "checkbox").attr("id", this.prefix + "_search_checkbox_" + count)
                    .attr("onchange", "search_table(\"" + this.prefix + "\"); return false;").prop("checked", true))
                    .append($("<label>").text(this.options[0]));
            }
            count++;
        }
        temp.append($("<div>").attr("id", this.prefix + "_pagination").addClass("pagination"));
        this.search_bar = $("<tr>").addClass("search_ignore").append(temp);

        // テーブルのヘッダーを生成
        this.header = $("<tr>").addClass("search_ignore");
        count = 0;
        for(const label of this.labels){
            this.header.append(
                $("<th>").append(
                    $("<a>")
                        .text(label)
                        .attr("onclick", "toggle_sort(\"" + this.prefix + "_header_" + count + "\",\"" + this.prefix + "\"); return false;")
                        .attr("id", this.prefix + "_header_" + count)
                        .attr("sort", "none")
                )
            );
            count++;
        }
        this.refresh();
    }

    // サーバからデータを取得し、テーブルの表示を更新
    update(){
        const uri = "/" + this.uri;
        this.display(LOADING);
        return fetch_json(uri)
            .then(items => {
                this.items = items;
                return items;
            });
    }

    // テーブルの表示を更新
    refresh(){
        this.update().then(items => {
            if (this.items.length === 0) {
                this.display(NO_ITEM);
            } else {
                this.display(this.init(items));
                this.search();
                this.paginate(this.current_page);
            }
        }, error => {
            this.display(ERROR);
            console.log(error);
        });
    }

    // テーブルの初期化
    init(items){
        const table = $("<table>").attr("id", this.prefix + "_table");
        table.append(this.search_bar).append(this.header);

        for(const item of items){
            const row = $("<tr>");
            for(const label of this.names){
                row.append($("<td>").text(item[label]));
            }
            table.append(row);
        }

        return table;
    }

    display(content){
        $(this.display_field).empty().append(content);
    }

    // テーブル内検索
    search(){
        const search_text = $("#" + this.prefix + "_search_text").val().toLowerCase();

        let item_count = 0;
        const types = this.types;
        const options = this.options;
        const prefix = this.prefix;
        $("#" + this.prefix +"_table tr").each(function () {
            let hits = new Array();
            if (!$(this).hasClass("search_ignore")) {
                let count = 0;
                for(const type of types){
                    let hit = false;
                    const temp = $(this).find("td").eq(count).text().toLowerCase();
                    if(type === 0){
                        // 文字列検索
                        hit = temp.includes(search_text) || temp === search_text;
                    }else if(type === 2){
                        // 選択肢検索
                        const val = $("#" + prefix + "_search_select_" + count).val();
                        if(val === "all"){
                            hit = true;
                        } else {
                            hit = (options[count][val] === temp);
                        }
                    }else if(type === 3){
                        // チェックボックス検索
                        const val = $("#" + prefix + "_search_checkbox_" + count).is(":checked");
                        hit = (val === (temp === options[count][1]));
                    }
                    count++;
                    hits.push(hit);
                }
                
                let show = true;
                count = 0;
                let text_hit = false;
                for(let hit of hits){
                    if(types[count] === 0){
                        if(hit === true && !text_hit){
                            text_hit = true;
                        }
                    } else if(types[count] !== 4){
                        show = show && hit;
                    }
                    count++;
                }
                show = show && text_hit;

                if(show){
                    $(this).show().attr("id", prefix + "_" + item_count);
                    item_count++;
                } else {
                    $(this).hide().removeAttr("id");
                }
            }
        });
        this.visible_count = item_count;
        this.paginate(this.current_page);
    }

    // ページネーション
    paginate(i){
        this.current_page = i;
        const page_count = Math.floor((this.visible_count - 1) / this.items_per_page);
        const pagination_buttons = $("#" + this.prefix + "_pagination");
        const max_buttons = 5;

        pagination_buttons.empty();
        if(this.visible_count === 0){
            pagination_buttons.append($("<p>").attr("style", "color: red;").append("検索条件に該当するデータはありません。"));
            return;
        }
        pagination_buttons.append($("<button>").attr("onclick", "update_pagination(0, \"" + this.prefix + "\"); return false;").text("<<"));
        if (this.current_page > 0)
            pagination_buttons.append($("<button>").attr("onclick", "update_pagination(" + (this.current_page - 1) + ", \"" + this.prefix + "\"); return false;").text("<"));
        const button_start = (this.current_page - 2 < 0) ? 0 : this.current_page - 2;
        if (button_start !== 0) pagination_buttons.append("...");
        for (let i = button_start; i < button_start + max_buttons; i++) {
            const new_btn = $("<button>").attr("onclick", "update_pagination(" + i + ", \"" + this.prefix + "\"); return false;").text(i + 1);
            if (i === this.current_page) new_btn.addClass("active");
            pagination_buttons.append(new_btn);
            if (i === page_count) break;
            if ((i === (button_start + max_buttons - 1)) && page_count > i) {
                pagination_buttons.append("...");
            }
        }
        if (this.current_page < page_count)
            pagination_buttons.append($("<button>").attr("onclick", "update_pagination(" + (this.current_page + 1) + ", \"" + this.prefix + "\"); return false;").text(">"));
        pagination_buttons.append($("<button>").attr("onclick", "update_pagination(" + page_count + ", \"" + this.prefix + "\"); return false;").text(">>"));

        let counter = 0;
        for (counter; counter < this.visible_count; counter++) {
            if (Math.floor(counter / this.items_per_page) === this.current_page) {
                $("#" + this.prefix + "_" + counter).show();
            }else {
                $("#" + this.prefix + "_" + counter).hide();
            }
        }
    }

    // セルの内容を取得
    // get_cell_text(row, column){
    //
    // }

    // セルのセレクターを返す
}

// ページにあるテーブルのリスト
let tables;
function find_table(s) {
    for(const table of tables){
        if(table.prefix === s){
            return table;
        }
    }
    return null;
}

// ソートのトグル
function toggle_sort(s, table) {
    const label = "#" + s;
    const sort = $(label).attr("sort");
    const text = $(label).text();
    switch (sort) {
        case "none":
        default:
            $(label).text(text + "↓").attr("sort", "descending");
            break;
        case "descending":
            $(label).text(text.substring(0, text.length - 1) + "↑").attr("sort", "ascending");
            break;
        case "ascending":
            $(label).text(text.substring(0, text.length - 1)).attr("sort", "none");
            break;
    }
    find_table(table).search();
}

// テーブル内検索
function search_table(table){
    find_table(table).search();
}

// ページネーション
function update_pagination(i, table_tag){
    find_table(table_tag).paginate(i);
}