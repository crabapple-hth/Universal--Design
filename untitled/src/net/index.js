import axios from "axios";
import {ElMessage} from "element-plus";

//本地存储token的key
const authItemName="access_token"

//默认的失败请求处理
const defaultFailure = (message,code,url)=>{
    console.warn(`请求地址:${url},状态码:${code},错误信息:${message}`)
    ElMessage.warning(message)
}

//默认错误请求处理
const defaultError=(err)=>{
    console.error(err)
    ElMessage.error("发生了一些错误")
}

//获取本地存储的token
function takeAccessToken(){
    const str=localStorage.getItem(authItemName) ||sessionStorage.getItem(authItemName)
    if(!str) return null;
    const authObj=JSON.parse(str)
    if(new Date(authObj.expire.replace(" ", "T")) <= new Date()){
        deleteAccessToken()
        ElMessage.warning("登录状态已过期")
        return null
    }
    return authObj.token
}


//获取角色
function takeAccessRole(){
    const str=localStorage.getItem(authItemName) ||sessionStorage.getItem(authItemName)
    if(!str) return null;
    const authObj=JSON.parse(str)
    if(new Date(authObj.expire.replace(" ", "T")) <= new Date()){
        deleteAccessToken()
        ElMessage.warning("登录状态已过期")
        return null
    }
    return authObj.role
}

//将token存储到本地
function storeAccessToken(remember,token,expire,role){
    const authObj={
        token,
        role,
        expire,
    }
    const str=JSON.stringify(authObj)
    if (remember)
        localStorage.setItem(authItemName,str)
    else
        sessionStorage.setItem(authItemName,str)
}

//删除本地存储的token
function deleteAccessToken(){
    sessionStorage.removeItem(authItemName)
    localStorage.removeItem(authItemName)
}

//post请求封装
function internalPost(url,data,header,success,failure,error=defaultError){
    axios.post(url,data,{headers:header}).then(({data})=>{
        if (data.code===200){
            success(data.data)
        }else {
            failure(data.message,data.code,url)
        }
    }).catch(err =>error(err))
}

//get请求封装
function internalGet(url, headers, success, failure, error = defaultError){
    axios.get(url, { headers: headers }).then(({data}) => {
        if(data.code === 200) {
            success(data.data)
        }else {
            failure(data.message, data.code, url)
        }
    }).catch(err => error(err))
}

//put请求封装
function internalPut(url, data, header, success, failure, error = defaultError) {
    axios.put(url, data, { headers: header }).then(({data}) => {
        if (data.code === 200) {
            success(data.data)
        } else {
            failure(data.message, data.code, url)
        }
    }).catch(err => error(err))
}

//delete请求封装
function internalDelete(url, header, success, failure, error = defaultError) {
    axios.delete(url, { headers: header }).then(({data}) => {
        if (data.code === 200) {
            success(data.data)
        } else {
            failure(data.message, data.code, url)
        }
    }).catch(err => error(err))
}

//登录请求
function login(username,password,remember,success,failure=defaultFailure){
    internalPost('/api/auth/login',{
        username:username,
        password:password
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{
        storeAccessToken(remember,data.token,data.expire,data.role)
        ElMessage.success(`登录成功，欢迎${data.username}`)
        success()
    },failure)
}

//重置密码请求
function resetPassword(data,success){
    internalPost('/api/auth/resetPassword',data,{
        'Content-Type':'application/x-www-form-urlencoded',
    },()=>{
        success()
    })
}

//退出登录
function logout(success,failure=defaultFailure){
    internalGet("/api/auth/logout",{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        deleteAccessToken()
        success()
    },failure)
}

//发送注册请求
function register(data,success,failure=defaultFailure){
    internalPost("/api/auth/register",data,{
        'Content-Type':'application/x-www-form-urlencoded',
    },()=>{
        success()
    },failure)
}

//获取验证码
function getCode(email,type,coldTime,success,failure){
    coldTime.value=60
    internalGet(`/api/auth/getCode?email=${email}&&type=${type}`,{
        'Content-Type':'application/json',
    },(data)=>{
        success()
        const handle= setInterval(()=>{
            coldTime.value--
            if (coldTime.value===0){
                clearInterval(handle)
            }
        },1000)
    },(message)=>{
        failure(message)
        coldTime.value=0
    })
}

//获取帖子列表
function getTopics(page,success,failure){
    internalGet(`/index/all-topics?current=${page}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure())
}

//分类获取帖子
function getTopicsByType(data,success,failure){
    internalGet(`/index/type-topics?current=${data.page}&type=${data.type}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure)
}

//获取帖子收藏点赞信息
function getTopicLikeCollect(topicId,success,failure){
    internalGet(`/index/user-like-collect?topicId=${topicId}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure())
}

//点赞
function changeLike(topicId,like,success,failure){
    internalGet(`/index/like?topicId=${topicId}&like=${like}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        success()
    },()=>failure())
}

//收藏
function changeCollect(topicId,collect,success){
    internalGet(`/index/collect?topicId=${topicId}&collect=${collect}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        success()
    })
}

//获取用户收藏帖子列表
function getCollects(success){
    internalGet(`/account/info/collectList`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

//获取用户点赞帖子列表
function getLikes(success){
    internalGet(`/account/info/likeList`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

//获取用户发表帖子列表
function getMyTopics(success){
    internalGet(`/account/info/myTopics`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

//发表帖子
function creatTopic(data,success,failure){
    internalPost("/creat-topic",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success(),()=>failure())
}

//获取帖子详细信息
function getTopicDetails(topicId,success,failure){
    internalGet(`/getTopicDetails?topicId=${topicId}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),()=>failure())
}

//发表评论
function creatCommend(data,success,failure){
    internalPost("/creat-commend",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success(),()=>failure())
}

//获取评论列表
function getComments(tid,success,failure){
    internalGet(`/getComments?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),failure)
}

//获取详细信息
function getAccount(success){
    internalGet("/account/info/details",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),(err)=>{console.log(err)})
}

//获取用户信息
function getInfo(success){
    internalGet("account/info/getInfo",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//更新用户信息
function updateInfo(data,success){
    internalPost("/account/info/update",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}

//用户验证
function isUnauthorized(){
    return !takeAccessToken()
}

//用户角色判断
function isRoleAdmin(){
    return takeAccessRole() ==='admin'
}

//请求头封装
function getAccessToken(){
   return  {
        'Authorization': `Bearer ${takeAccessToken()}`
    }
}

//获取帖子类型列表
function getTypeList(success){
    internalGet("/getTypes",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),(err)=>{console.log(err)})
}

//获取用户列表
function apiUserList(page,size,success){
    internalGet(`/api/admin/user/list?page=${page}&size=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//获取用户详细信息
function apiUserDetailTotal(id,success){
    internalGet(`/api/admin/user/detail?id=${id}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}


//保存用户信息
function apiUserSave(temp,success){
    internalPost('/api/admin/user/save',temp,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}

//获取回复评论列表
function replyCommentList(cid,page,size,success){
    internalGet(`/replyCommentList?cid=${cid}&pageNum=${page}&pageSize=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//获取天气信息
function apiForumWeather(longitude, latitude,success){
    internalGet(`/weather?longitude=${longitude}&latitude=${latitude}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),()=>{
        ElMessage.warning("位置信息获取失败，请稍后再试")
    })
}


//获取帖子列表
function apiTopicList(page,size,success){
    internalGet(`/api/admin/user/topicList?page=${page}&size=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}


//删除帖子
function apiDelTopic(tid,success){
    internalGet(`/api/admin/user/delete?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}


//置顶
function apiSetTop(tid,success){
    internalGet(`/api/admin/user/setTop?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}

//获取通知列表
function apiNotificationList(success){
    internalGet('/api/notification/list',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//清除单个消息通知
function apiNotificationDelete(id,success){
    internalGet(`/api/notification/delete?id=${id}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}


//清除通知消息
function apiNotificationDeleteAll(success){
    internalGet('/api/notification/delete-all',{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}


//获取七天内帖子发表数量
function apiLastSeven(success){
    internalGet('/api/admin/user/LastSeven',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//获取各类型帖子数量
function apiTypesNum(success){
    internalGet('/api/admin/user/TypeTopicNum',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//获取性别
function apiGender(success){
    internalGet('/api/admin/user/gender/count',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//获取搜索结果列表
function searchList(keyword,type,success){
    internalGet(`api/search/topics?keyword=${keyword}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}


//获取网站通知
function getAnnounce(success){
    internalGet('/api/admin/forum/announce',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

//删除帖子
function authDelete(tid,success){
    internalGet(`delete?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}



//编辑帖子
function editor(data,success,failure){
    internalPost('editor',data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}


export {takeAccessToken,login,logout,getCode,
    register,getTopics,getTopicsByType,getTopicLikeCollect,
    changeLike,changeCollect,getCollects,getLikes,
    getMyTopics,creatTopic,getTopicDetails,creatCommend,
    getComments,getAccount,getInfo,updateInfo,isUnauthorized,
    getAccessToken,getTypeList,isRoleAdmin,apiUserList,apiUserDetailTotal,
    apiUserSave,replyCommentList,apiForumWeather,apiTopicList,apiDelTopic,
    apiSetTop,apiNotificationDelete,apiNotificationDeleteAll,apiNotificationList,
    internalGet,internalPost,internalPut,internalDelete,apiLastSeven,apiGender,apiTypesNum,
    searchList,getAnnounce,resetPassword,authDelete,editor}