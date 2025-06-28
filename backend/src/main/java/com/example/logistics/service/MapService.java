package com.example.logistics.service;

import java.util.Map;

public interface MapService {
    /**
     * 获取驾车路线规划
     * @param origin 起点坐标（格式：经度,纬度）
     * @param destination 终点坐标（格式：经度,纬度）
     * @return 路线规划结果
     */
    Map<String, Object> getDrivingRoute(String origin, String destination);

    /**
     * 获取地址解析结果
     * @param address 地址
     * @return 地理编码结果
     */
    Map<String, Object> getGeocode(String address);

    /**
     * 获取逆地理编码结果
     * @param location 坐标（格式：经度,纬度）
     * @return 逆地理编码结果
     */
    Map<String, Object> getRegeocode(String location);

    /**
     * 计算两点间距离
     * @param origin 起点坐标（格式：经度,纬度）
     * @param destination 终点坐标（格式：经度,纬度）
     * @return 距离（米）
     */
    double calculateDistance(String origin, String destination);
} 