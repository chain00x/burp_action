
  HttpRequest req = requestResponse.request();
  String mimeType = req.contentType().toString();
  
  // 所有参数名
  String[] allParams = {
      "_sort", "asc", "desc", "ascs", "descs", "sortOrder", "order", "orderBy", "dir", "sortBy", "sidx", "sortName",
      "column", "sortField", "field", "sort", "order_column", "orderField", "orderByField", "orderByColumn",
      "orders[0].column", "orders[0].asc", "isAsc", "property", "ranking", "_sort[column]", "_sort[type]",
      "sorts[field]", "sorts[type]", "orderDir", "sortType", "sortFieldName", "sortByColumn", "order_by", "ascending",
      "filter", "asc_sort", "ascSort", "column_comment", "column_first", "column_index", "column_key", "column_name",
      "column_type", "column_types", "columnBy", "columnComment", "columnFirst", "columnIndex", "columnKey",
      "columnName", "columns", "columns_by", "columns_comment", "columns_first", "columns_index", "columns_key",
      "columns_name", "columns_type", "columns_types", "columnsBy", "columnsComment", "columnsFirst", "columnsIndex",
      "columnsKey", "columnsName", "columnType", "columnTypes", "custom_column", "custom_columns", "customColumn",
      "customColumns", "desc_list", "descList", "has_column", "has_columns", "hasColumn", "hasColumns", "is_asc",
      "is_orderby", "isOrderBy", "order_asc", "order_desc", "order_field", "order_index", "order_key", "order_limit",
      "order_list", "order_name", "order_type", "order_types", "orderAsc", "orderByColumns", "orderby", "orderBySort",
      "orderDesc", "orderIndex", "orderKey", "orderLimit", "orderList", "orderName", "orderNum", "orders[0].by",
      "orders[0].columns", "orders[0].desc", "orders[0].filed", "orders[0].index", "orders[0].limit", "orders[0].list",
      "orders[0].name", "orders[0].sort", "orders[0].type", "orders[0].types", "orderType", "orderTypes", "sort_by",
      "sort_column", "sort_columns", "sort_field", "sort_id", "sort_index", "sort_key", "sort_list", "sort_name",
      "sort_no", "sort_order", "sort_type", "sort_types", "sortByNum", "sortByNumber", "sortColumn", "sortColumns",
      "sortId", "sortIndex", "sortKey", "sortList", "sortNo", "sortTypes", "sortOrders", "sql", "sqlOrder", "sqlOrderBy",
      "widgets[0].order", "widgets[0][order]", "orderbyName", "params[dataScope]", "orderByClause", "storeOrder",
      "orderByCaseInfo", "orders", "sorts"
  };
  
  // 构造参数注入批次
  List<String[]> paramBatches = new ArrayList<>();
  
  if ("NONE".equals(mimeType)) {
      // 将参数平均分成 5 段
      int total = allParams.length;
      int partSize = (int) Math.ceil((double) total / 5);
      for (int i = 0; i < total; i += partSize) {
          int end = Math.min(i + partSize, total);
          String[] part = Arrays.copyOfRange(allParams, i, end);
          paramBatches.add(part);
      }
  } else {
      // 其他类型（URL_ENCODED、JSON）不分段
      paramBatches.add(allParams);
  }
  
  // 针对每一批参数构造请求并发送
  for (String[] pxParams : paramBatches) {
      List<HttpParameter> parameters = new ArrayList<>();
  
      switch (mimeType) {
          case "NONE":
              for (String name : pxParams) {
                  String encodedName = name;
  
                  // 如果参数中包含方括号则进行 URL 编码
                  if (name.contains("[") || name.contains("]")) {
                      encodedName = api.utilities().urlUtils().encode(name, URLEncoding.JAVA_DEFAULT);
                  }
  
                  HttpParameter param = HttpParameter.parameter(encodedName, "0", HttpParameterType.URL);
                  parameters.add(param);
              }
              break;
  
          case "URL_ENCODED":
              for (String name : pxParams) {
                  HttpParameter param = HttpParameter.parameter(name, "0", HttpParameterType.BODY);
                  parameters.add(param);
              }
              break;
  
          case "JSON":
              for (String name : pxParams) {
                  HttpParameter param = HttpParameter.parameter(name, "0", HttpParameterType.JSON);
                  parameters.add(param);
              }
              break;
  
          default:
              logging().logToError("Unsupported MIME type: " + mimeType);
              return;
      }
  
      // 构造新请求并发送
      HttpRequest modifiedReq = req.withAddedParameters(parameters);
  api().repeater().sendToRepeater(modifiedReq);
  }
