logging().logToOutput("Unedited custom action");

var urlencode_all = (Function<String,String>)(data -> {
byte[] bytes = data.getBytes();
StringBuilder sb = new StringBuilder();

for (byte b : bytes) {
            sb.append(String.format("%%%02X", b));
        }
return sb.toString();
});


var all_path = requestResponse.request().pathWithoutQuery().split("/");

//URL编码绕过
for (int i = 0; i < all_path.length; i++) {
    if(!(all_path[i].matches("\\d+") || all_path[i].equals(""))){
        api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(all_path[i], urlencode_all.apply(all_path[i]))));
api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(all_path[i], all_path[i]+"/")));
        api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(all_path[i], all_path[i]+"/./")));
};
};


api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(requestResponse.request().pathWithoutQuery(), requestResponse.request().pathWithoutQuery()+"/1.js/../")));
api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(requestResponse.request().pathWithoutQuery(), requestResponse.request().pathWithoutQuery()+".json")));
api().http().sendRequest(requestResponse.request().withPath(requestResponse.request().path().replace(requestResponse.request().pathWithoutQuery(), requestResponse.request().pathWithoutQuery()+";1.js")));