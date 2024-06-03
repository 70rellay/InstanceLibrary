package com.demo;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.util.HttpConnectionUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanmengfan
 * @date 2021/11/1 5:34 下午
 * @description
 */
public class CompensateHustTest {
    private static final Log log = Log.get(CompensateHustTest.class);


    @Test
    public void compensateByLSHXXXTJS() {
        List<String> entrys = getLSH();
        for (String entry : entrys) {
            JSONObject processDetails = getProcessDetails(entry);
            log.info("processDetails"+processDetails.toString());
            JSONObject compensateJSON = getCompensateJSON(processDetails);
            log.warn(compensateJSON.toString());
            JSONObject result = updateDate(entry,compensateJSON.toString());
            log.info(result.toString());
        }
    }

    /**
     * 修改流程数据
     * @param entry 流水号
     * @param data JSON数据
     * @return
     */
    private JSONObject updateDate(String entry,String data) {
        String token = getToken("sys_process_edit");
        String url = "https://ehall.hust.edu.cn/infoplus/apis/v2/process/" + entry + "/data";
        Map<String, Object> params = Dict.create()
                .set("access_token", token)
                .set("data", data);
        return JSONObject.parseObject(HttpConnectionUtils.doPost(url, params));
    }


    /**
     * 根据条件获取每个流程不一样的补偿数据
     * @param processDetails
     * @return
     */
    private JSONObject getCompensateJSON(JSONObject processDetails) {
        JSONArray tasks = processDetails.getJSONArray("tasks");
        log.info(tasks.toString());
        JSONObject result = new JSONObject();
        result.put("fieldBZ1", "基本信息填写");
        result.put("fieldBZ2", "测试环境及测试域名申请");
        result.put("fieldBZ3", "安全扫描与代码审计");
        result.put("fieldBZ4", "项目初验");
        result.put("fieldBZ5", "正式上线申请");
        result.put("fieldBZ6", "数据共享");
        result.put("fieldBZ7", "公共信息系统集成与对接");
        result.put("fieldBZ8", "域名备案申请");
        for (int i = 0; i < tasks.size(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            String code = task.getString("code");
            log.info("code :" + code + "---->" + task.toString());
            String status = task.getString("status");
            switch (code) {
                case "TXXX":
                    result.put("fieldURL1", task.getString("uri"));
                    if (!"已完成".equals(result.getString("fieldMS1"))) {
                        if ("2".equals(status)) {
                            result.put("fieldMS1", "审核中");
                        } else if ("1".equals(status)) {
                            result.put("fieldMS1", "申请人填写中");
                        }
                    }
                    break;
                case "WLFGFZR":
                    if ("2".equals(status)) {
                        result.put("fieldMS1", "已完成");
                    }
                    break;
                case "sub1":
                    String entry = task.getJSONObject("sub").getString("entry");
                    JSONObject sonData = getProcessDetails(entry);
                    JSONArray sonEntryData = sonData.getJSONArray("tasks");
                    String sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "Txsq":
                                result.put("fieldURL2", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS2", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS2", "已完成");
                    } else {
                        result.put("fieldMS2", "审核中");
                    }
                    break;
                case "sub5":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "Txsq":
                                result.put("fieldURL3", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS3", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS3", "已完成");
                    } else {
                        result.put("fieldMS3", "审核中");
                    }
                    break;
                case "sub3":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "Txsq":
                                result.put("fieldURL6", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS6", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS6", "已完成");
                    } else {
                        result.put("fieldMS6", "审核中");
                    }
                    break;
                case "sub4":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "TXXX":
                                result.put("fieldURL7", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS7", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS7", "已完成");
                    } else {
                        result.put("fieldMS7", "审核中");
                    }
                    break;
                case "sub51":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "TXSQ":
                                result.put("fieldURL4", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS4", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS4", "已完成");
                    } else {
                        result.put("fieldMS4", "审核中");
                    }
                    break;
                case "sub6":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "Txsq":
                                result.put("fieldURL5", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS5", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS5", "已完成");
                    } else {
                        result.put("fieldMS5", "审核中");
                    }
                    break;
                case "sub7":
                    entry = task.getJSONObject("sub").getString("entry");
                    sonData = getProcessDetails(entry);
                    sonEntryData = sonData.getJSONArray("tasks");
                    sonStatus = task.getString("status");
                    for (int j = 0; j < sonEntryData.size(); j++) {
                        JSONObject sonEntryDataJSONObject = sonEntryData.getJSONObject(j);
                        switch (sonEntryDataJSONObject.getString("code")) {
                            case "SQXZJD":
                                result.put("fieldURL8", sonEntryDataJSONObject.getString("uri"));
                                if ("1".equals(sonStatus)) {
                                    result.put("fieldMS8", "申请人填写中");
                                }
                                break;
                        }
                    }
                    if ("done".equals(sonData.getString("status"))) {
                        result.put("fieldMS8", "已完成");
                    } else {
                        result.put("fieldMS8", "审核中");
                    }
                    break;
            }
        }
        return result;
    }


    /**
     * 获取指定流水号的数据
     * @param entry
     * @return
     */
    private JSONObject getProcessDetails(String entry) {
        String token = getToken("sys_process");
        String url = "https://ehall.hust.edu.cn/infoplus/apis/v2/process/" + entry;
        Map<String, Object> params = Dict.create()
                .set("access_token", token);
        JSONObject jsonObject = JSONObject.parseObject(HttpConnectionUtils.doGet(url, params));
        if (jsonObject != null && StrUtil.isNotBlank(jsonObject.getString("errno")) && "0".equals(jsonObject.getString("errno"))) {
            JSONObject entities = jsonObject.getJSONArray("entities").getJSONObject(0);
            log.info(entry+"<-->"+entities.toString());
            return entities;
        }
        return null;
    }


    /**
     * 获取需要补偿的流水号
     *
     * @return
     */
    public List<String> getLSH() {
        return Arrays.asList("660242");
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken(String scope) {
        String url = "https://ehall.hust.edu.cn/infoplus/oauth2/token";
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/x-www-form-urlencoded");
        Map<String, Object> params = Dict.create()
                .set("grant_type", "client_credentials")
                .set("client_secret", "db064ae1a979c153083cd7fd5c0534b9")
                .set("client_id", "f3053fe2-007f-477a-b56e-dd4900e5fd71")
                .set("scope", scope);
        JSONObject jsonObject = JSONObject.parseObject(HttpConnectionUtils.doPost(url, headers, params));
        if (jsonObject != null) {
            return jsonObject.getString("access_token");
        }
        return null;
    }
}
