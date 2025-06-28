package com.example.logistics.service;

import java.util.List;
import java.util.Map;

public interface CacheService {
    /**
     * 保存常用地址
     */
    void saveCommonAddress(Long userId, String address);

    /**
     * 获取常用地址列表
     */
    List<String> getCommonAddresses(Long userId);

    /**
     * 保存路线规划结果
     */
    void saveRouteCache(String key, Map<String, Object> route);

    /**
     * 获取路线规划结果
     */
    Map<String, Object> getRouteCache(String key);

    /**
     * 保存地理编码结果
     */
    void saveGeocodeCache(String address, Map<String, Object> geocode);

    /**
     * 获取地理编码结果
     */
    Map<String, Object> getGeocodeCache(String address);

    /**
     * 删除缓存
     */
    void deleteCache(String key);

    /**
     * 设置缓存过期时间
     */
    void setExpire(String key, long timeout);
} 