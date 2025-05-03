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

function logout(success,failure=defaultFailure){
    internalGet("/api/auth/logout",{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        deleteAccessToken()
        success()
    },failure)
}

function register(data,success,failure=defaultFailure){
    internalPost("/api/auth/register",data,{
        'Content-Type':'application/x-www-form-urlencoded',
    },()=>{
        success()
    },failure)
}


function getCode(email,coldTime,success,failure){
    coldTime.value=60
    internalGet(`/api/auth/getCode?email=${email}`,{
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

function getTopics(page,success,failure){
    internalGet(`/index/all-topics?current=${page}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure())
}

function getTopicsByType(data,success,failure){
    internalGet(`/index/type-topics?current=${data.page}&type=${data.type}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure)
}

function getTopicLikeCollect(topicId,success,failure){
    internalGet(`/index/user-like-collect?topicId=${topicId}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    },()=>failure())
}

function changeLike(topicId,like,success,failure){
    internalGet(`/index/like?topicId=${topicId}&like=${like}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        success()
    },()=>failure())
}

function changeCollect(topicId,collect,success){
    internalGet(`/index/collect?topicId=${topicId}&collect=${collect}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        success()
    })
}

function getCollects(success){
    internalGet(`/account/info/collectList`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

function getLikes(success){
    internalGet(`/account/info/likeList`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

function getMyTopics(success){
    internalGet(`/account/info/myTopics`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        success(data)
    })
}

function creatTopic(data,success,failure){
    internalPost("/creat-topic",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success(),()=>failure())
}

function getTopicDetails(topicId,success,failure){
    internalGet(`/getTopicDetails?topicId=${topicId}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),()=>failure())
}

function creatCommend(data,success,failure){
    internalPost("/creat-commend",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success(),()=>failure())
}

function getComments(tid,success,failure){
    internalGet(`/getComments?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),failure)
}

function getAccount(success){
    internalGet("/account/info/details",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),(err)=>{console.log(err)})
}

function getInfo(success){
    internalGet("account/info/getInfo",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function updateInfo(data,success){
    internalPost("/account/info/update",data,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}

function isUnauthorized(){
    return !takeAccessToken()
}

function isRoleAdmin(){
    return takeAccessRole() ==='admin'
}

function getAccessToken(){
   return  {
        'Authorization': `Bearer ${takeAccessToken()}`
    }
}

function getTypeList(success){
    internalGet("/getTypes",{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data),(err)=>{console.log(err)})
}

function apiUserList(page,size,success){
    internalGet(`/api/admin/user/list?page=${page}&size=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}


function apiUserDetailTotal(id,success){
    internalGet(`/api/admin/user/detail?id=${id}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiUserSave(temp,success){
    internalPost('/api/admin/user/save',temp,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}

function replyCommentList(cid,page,size,success){
    internalGet(`/replyCommentList?cid=${cid}&pageNum=${page}&pageSize=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiForumWeather(longitude, latitude,success){
    internalGet(`/weather?longitude=${longitude}&latitude=${latitude}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiTopicList(page,size,success){
    internalGet(`/api/admin/user/topicList?page=${page}&size=${size}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiDelTopic(tid,success){
    internalGet(`/api/admin/user/delete?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}

function apiSetTop(tid,success){
    internalGet(`/api/admin/user/setTop?tid=${tid}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },success)
}

function apiNotificationList(success){
    internalGet('/api/notification/list',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiNotificationDelete(id,success){
    internalGet(`/api/notification/delete?id=${id}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}

function apiNotificationDeleteAll(success){
    internalGet('/api/notification/delete-all',{
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>success())
}

function apiLastSeven(success){
    internalGet('/api/admin/user/LastSeven',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiTypesNum(success){
    internalGet('/api/admin/user/TypeTopicNum',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function apiGender(success){
    internalGet('/api/admin/user/gender/count',{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>success(data))
}

function searchList(keyword,type,success){
    internalGet(`api/search/topics?keyword=${keyword}`,{
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
    searchList}