package com.ktnn.clients.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ktnn.consts.FrameConst;
import com.ktnn.report.ExtentReportManager;
import com.ktnn.report.ExtentTestManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

import static com.ktnn.consts.FrameConst.AppConfig.API_DOMAIN;
import static com.ktnn.consts.FrameConst.HTTPMethod.*;
import static com.ktnn.consts.FrameConst.*;

@Slf4j
public class APIBase {

    /**
     * Add more information for Report: Including Extent and Allure.
     * You can add more report at this function.
     *
     * @param extMsg : The message to show on report
     */
    public static void addReportInfo(FrameConst.LogType logType, String extMsg, boolean isResult, String responseCodeBlock) {
        ExtentTestManager.addReportInfo(logType, extMsg, isResult, responseCodeBlock);
    }



    //endregion
    //region Rest API
    /**
     * Create request default for RequestSpecification
     *
     * @param apiPath  : The path of api
     * @param isSecure : use secure or not
     */
    public RequestSpecification createDefaultRequest(String apiPath, boolean isSecure, String... uris) {
        String endPoint = uris.length > 0 ? uris[0] : API_DOMAIN;
        RequestSpecification requestSpec = RestAssured.given()
                .baseUri(endPoint)
                .basePath(apiPath);
        return isSecure ? requestSpec.relaxedHTTPSValidation() : requestSpec;
    }

    //endregion


    /**
     * Call Rest API
     *
     * @param method  : The method type
     * @param request : The request
     * @return API response
     */
    public Response callRestAPI(HTTPMethod method, RequestSpecification request) {
        Response response = null;
        switch (method) {
            case POST:
                ExtentReportManager.logRequest(request, POST);
                response = request.post();
                ExtentReportManager.logResponse(response);
                break;
            case PUT:
                ExtentReportManager.logRequest(request, PUT);
                response = request.put();
                ExtentReportManager.logResponse(response);
                break;
            case GET:
                ExtentReportManager.logRequest(request, GET);
                response = request.get();
                ExtentReportManager.logResponse(response);
                break;
            case DELETE:
                ExtentReportManager.logRequest(request, DELETE);
                response = request.delete();
                ExtentReportManager.logResponse(response);
                break;
            case PATCH:
                ExtentReportManager.logRequest(request, PATCH);
                response = request.patch();
                ExtentReportManager.logResponse(response);
                break;
        }
        return response;
    }

//region Extract data from Response

    /**
     * Convert json to Object
     *
     * @param jsonData : Json Data
     * @return : T Object
     */
    public <T> T convertJsonToObject(String jsonData, Type type) {
        return new Gson().fromJson(String.valueOf(jsonData), type);
    }

    public HashMap<String, Object> convertJsonToHashMap(String jsonData) {
        Type type = new TypeReference<Map<String, HashMap>>() {
        }.getType();
        return new Gson().fromJson(String.valueOf(jsonData), type);
    }


    /**
     * Convert a map to an object
     *
     * @return : T Object
     */
    public <T> T convertObjectToObject(Object mapData, Class<T> cls) {
        return (T) new ObjectMapper().convertValue(mapData, cls);
    }

    public <T> T extractDataFromResponse(Response response, String jsonPath) {
        return response.jsonPath().get(jsonPath);
    }

    public String extractResponseDataToString(Response response, String jsonPath) {
        var res = extractDataFromResponse(response, jsonPath);
        if (Objects.nonNull(res)) {
            return String.valueOf(res);
        }
        return "NOT_FOUND";
    }

    public JSONObject extractResponseDataToJSON(Response response, String jsonPath) {
        JSONObject jsonObject = new JSONObject(response.prettyPrint());
        String[] pathArr = jsonPath.split("\\.");
        for (String curPath : pathArr) {
            if (jsonObject != null && jsonObject.keySet().contains(curPath)) {
                jsonObject = jsonObject.getJSONObject(curPath);
            } else {
                return null;
            }
        }
        return jsonObject;
    }

    public HashMap<String, Object> responseToHashMap(Response response) {
        try {
            return new ObjectMapper().readValue(response.asString(), HashMap.class);
        } catch (JsonProcessingException e) {
            log.info("VDebug: Convert the response to HashMap: FAILED");
            return new HashMap<>();
        }

    }

    public HashMap<String, Object> extractDataFromResToHashMap(Response response) {
        HashMap<String, Object> resMap = responseToHashMap(response);
        if (resMap.containsKey("data")) {
            return (HashMap<String, Object>) resMap.get("data");
        }
        return new HashMap<>();
    }

    /**
     * Get default value
     */
    public String getStringValueOrDefault(Object object, String defVal) {
        return Objects.isNull(object) ? defVal : object.toString();
    }

}