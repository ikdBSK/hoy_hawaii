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