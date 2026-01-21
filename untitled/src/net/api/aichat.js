import {internalGet, internalPost, internalPut, internalDelete, takeAccessToken} from "@/net/index.js";




function sendChat(text,success,failure){
    internalGet(`api/chat/ai?message=${text}`,{
        'Authorization':"Bearer "+ takeAccessToken()
    },(data)=>{
        console.log(data)
        success(data)
        },(error)=>failure(error),
    )
}

export {sendChat}
