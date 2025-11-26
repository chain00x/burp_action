var all_path = requestResponse.request().pathWithoutQuery().split("/");
var actuator_path = new String[]{
    "trace", "health", "loggers", "logfile", "metrics", "autoconfig",
    "heapdump", "env", "info", "dump", "configprops",
    "cloudfoundryapplication", "features", "flyway", "liquibase",
    "mappings", "shutdown", "version", "auditevents", "beans",
    "actuator", "actuator/","swagger/","doc","docs","swagger/index.html","swagger.json","swagger-ui","swagger-ui.html","swagger-ui.json","swagger.yml","api/swagger","api/swagger/","api/swagger.json","api/swagger-ui","api/swagger-ui.html","api/swagger-ui.json","api/v1/swagger/","api/v1/swagger.json","api/v1/swagger-ui","api/v1/swagger-ui.html","api/v1/swagger-ui.json","api/v2/swagger/","api/v2/swagger.json","api/v2/swagger-ui","api/v2/swagger-ui.html","api/v2/swagger-ui.json","apis/swagger/index.html","api/swagger/index.html","swagger-ui/index.html","portal/swagger/index.html","swagger/ui/index.html","api/swagger/ui/index","v1/swagger-ui/index.html","v2/swagger-ui/index.html","api-swagger/index.html","api/doc","api/docs","api/apidocs","api/v1/apidocs","api/v2/apidocs","api/api-docs","api/v1/api-docs","api/v2/api-docs","api/v3/apidocs","api/v3/api-docs","api/v1/","api/v2","api/v3","developer/","developers/","api/admin","api/public"
};

// 获取原始路径（不含查询参数）
String originalPath = requestResponse.request().pathWithoutQuery();

// 遍历路径片段
for (int i = 0; i < all_path.length; i++) {
    // 跳过纯数字或空字符串的片段
    if (!(all_path[i].matches("\\d+") || all_path[i].equals(""))) {

        // --- 修改部分 ---
        // 1. 转义当前片段，以防它包含正则表达式特殊字符
        String escapedCurrentSegment = java.util.regex.Pattern.quote(all_path[i]);

        // 2. 构造正则表达式：
        //    - escapedCurrentSegment : 匹配当前片段字面量
        //    - (/.*):               匹配一个 '/' 和后面所有字符（可选，因为路径片段可能在末尾）
        //    - $ :                  确保匹配到路径字符串的结尾
        //    这个组合可以匹配从当前片段开始直到路径结尾的所有内容
        String regexPattern = escapedCurrentSegment + "(/.*)?$";

        // 3. 编译正则表达式
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexPattern);

        // 遍历要添加的 Actuator 路径
        for (int j = 0; j < actuator_path.length; j++) {
            // 4. 使用 Matcher 和 replaceFirst 进行替换：
            //    - pattern.matcher(originalPath): 在原始路径上应用正则
            //    - replaceFirst(...): 将匹配到的（从当前片段到结尾）部分，
            //      替换为我们想要的新片段 (当前片段 + "/" + actuator 片段)
            String newPath = pattern.matcher(originalPath).replaceFirst(all_path[i] + "/" + actuator_path[j]);

            // 5. 发送修改后路径的请求
            api().http().sendRequest(
                requestResponse.request().withMethod("GET").withPath(newPath)
            );
        }
        // --- 修改部分结束 ---
    }
} // 注意：原代码末尾的分号和括号不匹配，这里修正了