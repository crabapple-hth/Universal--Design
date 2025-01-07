import axios from "axios";
import {ElMessage} from "element-plus";

//本地存储token的key
const authItemName="access_token"

//默认的失败请求处理
const defaultFailure = (message,code,url)=>{
    console.warn(`请求地址:${url},状态码:${code},错误信息:${message}`)
    ElMessage.warning("出现了一些错误")
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
    if(authObj.expire <= new Date()){
        deleteAccessToken()
        ElMessage.warning("登录状态已过期")
        return null
    }
    return authObj.token
}

//将token存储到本地
function storeAccessToken(remember,token,expire){
    const authObj={token:token,expire:expire}
    const str=JSON.stringify(authObj)
    if (remember)
        localStorage.setItem(authItemName,str)
    else
        sessionStorage.setItem(authItemName,str)
}

//删除本地存储的token
function deleteAccessToken(){
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
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
function internalGet(url,header,success,failure,error=defaultError){
    axios.get(url,{headers:header}).then(({data})=>{
        if (data.code===200){
            success(data.data)
        }else {
            failure(data.message,data.code,url)
        }
    }).catch(err =>error(err))
}

//登录请求
function login(username,password,remember,success,failure=defaultFailure){
    internalPost('/api/auth/login',{
        username:username,
        password:password
    },{
        'Content-Type':'application/x-www-form-urlencoded'
    },(data)=>{
        storeAccessToken(remember,data.token,data.expired)
        ElMessage.success(`登录成功，欢迎${data.username}`)
        success(data)
    },failure)
}

function logout(success,failure=defaultFailure){
    console.log(takeAccessToken()+111)
    internalGet("/api/auth/logout",{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        deleteAccessToken()
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
    internalGet(`/index/collect?topicId=${topicId}&like=${collect}`,{
        'Content-Type':'application/x-www-form-urlencoded',
        'Authorization':"Bearer "+ takeAccessToken()
    },()=>{
        success()
    })
}

export {login,logout,getCode,register,getTopics,getTopicLikeCollect,changeLike,changeCollect}