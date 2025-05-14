import { internalGet, internalPost, internalPut, internalDelete } from "@/net/index.js";
import { takeAccessToken } from "@/net/index.js";

// 获取活动列表
export function apiActivityList(success) {
    internalGet('/api/activities', {
        'Authorization': "Bearer " + takeAccessToken()
    }, data => success({ code: 200, data: data }));
}

// 获取活动详情
export function apiActivityDetail(id, success) {
    internalGet(`/api/activities/${id}`, {
        'Authorization': "Bearer " + takeAccessToken()
    }, data => success({ code: 200, data: data }));
}

// 创建活动
export function apiActivityCreate(activity, success) {
    internalPost('/api/activities', activity, {
        'Authorization': "Bearer " + takeAccessToken()
    }, success);
}

// 更新活动
export function apiActivityUpdate(id, activity, success) {
    internalPut(`/api/activities/${id}`, activity, {
        'Authorization': "Bearer " + takeAccessToken()
    }, success);
}

// 删除活动
export function apiActivityDelete(id, success) {
    internalDelete(`/api/activities/${id}`, {
        'Authorization': "Bearer " + takeAccessToken()
    }, success);
}

// 报名活动
export function apiActivityRegister(id,success) {
    internalPost(`/api/activities/${id}/register`, {}, {
        'Authorization': "Bearer " + takeAccessToken()
    }, success);
}

// 取消报名
export function apiActivityCancel(id, success) {
    internalPost(`/api/activities/${id}/cancel`, {}, {
        'Authorization': "Bearer " + takeAccessToken()
    }, success);
}

// 检查用户是否已报名
export function apiActivityCheckRegistration(id, success) {
    internalGet(`/api/activities/${id}/check`, {
        'Authorization': "Bearer " + takeAccessToken()
    }, data => success({ code: 200, data: data }));
} 