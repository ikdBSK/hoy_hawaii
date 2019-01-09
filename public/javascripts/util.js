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